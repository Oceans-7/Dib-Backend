package com.oceans7.dib.domain.place.service;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.dto.FacilityType;
import com.oceans7.dib.domain.place.dto.PlaceFilterOptions;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto.FacilityInfo;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.api.response.kakao.Address;
import com.oceans7.dib.global.api.response.kakao.AddressItem;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemFactory;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ValidatorUtil;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class PlaceService {
    private final DataGoKrAPIService tourAPIService;
    private final KakaoLocalAPIService kakaoLocalAPIService;
    private final DetailIntroItemFactory detailIntroItemFactory;

    private final UserRepository userRepository;
    private final DibRepository dibRepository;

    private final static int RADIUS_KM = 20;
    private final static int REMOVE_TARGET_CONTENT_TYPE_ID = 25;

    /**
     * 관광 정보 리스트 조회 (위치 기반/지역 필터링)
     * @return PlaceResponseDto 타입의 Response
     */
    public PlaceResponseDto getPlace(Long userId, GetPlaceRequestDto request, PlaceFilterOptions placeFilterOptions) {
        switch(placeFilterOptions.getFilterType()) {
            case DIVING -> { return getDivingFilteredPlace(userId, request, placeFilterOptions); }
            case LOCATION_BASED -> { return getLocationBasedPlace(userId, request, placeFilterOptions); }
            case AREA_BASED -> { return getAreaBasedPlace(userId, request, placeFilterOptions); }
            default -> { return null; }
        }
    }

    /**
     * 다이빙 필터 기반 관광 정보 리스트 조회
     * @return 다이빙 필터 기반의 PlaceResponseDto 타입의 Response
     */
    public PlaceResponseDto getDivingFilteredPlace(Long userId, GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = fetchDivingFilteredTourAPI(request, filterOption);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList(), userId, request.getMapX(), request.getMapY());

        return PlaceResponseDto.of(
                simplePlaceResponse,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize(),
                request.getArrangeType()
        );
    }

    private TourAPICommonListResponse fetchDivingFilteredTourAPI(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        List<TourAPICommonItemResponse> tourAPIItemList = new ArrayList<>();

        DivingContent.getAllContentIds().forEach(contentId -> {
            TourAPICommonItemResponse tourAPIItem = getCommonItem(contentId, ContentType.LEPORTS);
            tourAPIItemList.add(TourAPICommonItemResponse.fromDivingContentItem(tourAPIItem));
        });

        validateTourAPIResponse(tourAPIItemList.size());

        List<TourAPICommonItemResponse> applyFilteredTourAPIItemList = applyFilterOption(tourAPIItemList, request, filterOption);
        List<TourAPICommonItemResponse> paginateTourAPIItemList = paginateItems(applyFilteredTourAPIItemList, request);

        return TourAPICommonListResponse.of(
                        paginateTourAPIItemList,
                        paginateTourAPIItemList.size(),
                        request.getPage(),
                        request.getPageSize()
                );
    }

    /**
     * filterOption에 따라 필터를 적용
     */
    private  List<TourAPICommonItemResponse> applyFilterOption(List<TourAPICommonItemResponse> tourAPIItemList, GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        if(filterOption.isEmptyArea()) {
            return filterItemsByLocation(tourAPIItemList, request);
        }

        if(!filterOption.isEmptyArea()) {
            return filterItemsByArea(tourAPIItemList, request);
        }

        if(!filterOption.isEmptyArrangeType()) {
            return filterItemsByArrangeType(tourAPIItemList, filterOption);
        }
        return null;
    }

    /**
     * 지역 기반 조회의 다이빙 필터 데이터에서 지역명을 제외한 데이터를 삭제
     */
    private List<TourAPICommonItemResponse> filterItemsByArea(List<TourAPICommonItemResponse> tourAPIItemList, GetPlaceRequestDto request) {
        List<TourAPICommonItemResponse> filteredItemList = new ArrayList<>(tourAPIItemList);

        filteredItemList.removeIf(item -> !(item.getAddress().contains(request.getArea())));

        if (ValidatorUtil.isNotEmpty(request.getSigungu())) {
            filteredItemList.removeIf(item -> !(item.getAddress().contains(request.getSigungu())));
        }

        return filteredItemList;
    }

    /**
     * 위치 기반 조회의 다이빙 필터 데이터에서 거리 계산 후 정렬
     */
    private List<TourAPICommonItemResponse> filterItemsByLocation(List<TourAPICommonItemResponse> tourAPIItemList, GetPlaceRequestDto request) {
        List<TourAPICommonItemResponse> filteredItemList = new ArrayList<>(tourAPIItemList);

        filteredItemList.removeIf(item -> item.convertDistanceByFilter(request.getMapX(), request.getMapY()) > RADIUS_KM);
        filteredItemList.sort(Comparator.comparingDouble((TourAPICommonItemResponse item) -> item.convertDistanceByFilter(request.getMapX(), request.getMapY())));

        return filteredItemList;
    }

    /**
     * 다이빙 필터 콘텐츠를 사용자 요구사항에 맞추어 정렬
     */
    private List<TourAPICommonItemResponse> filterItemsByArrangeType(List<TourAPICommonItemResponse> tourAPIItemList, PlaceFilterOptions filterOption) {
        List<TourAPICommonItemResponse> filteredItemList = new ArrayList<>(tourAPIItemList);
        ArrangeType arrangeType = ArrangeType.valueOf(filterOption.getArrangeType());

        switch(arrangeType) {
            case A -> filteredItemList.sort(Comparator.comparing(TourAPICommonItemResponse::getTitle));
            case C -> filteredItemList.sort(Comparator.comparing(TourAPICommonItemResponse::parseModifiedTimeToDateTime).reversed());
        }

        return filteredItemList;
    }

    /**
     * 다이빙 필터 콘텐츠를 사용자 요구사항에 맞춰 페이지네이션
     */
    private List<TourAPICommonItemResponse> paginateItems(List<TourAPICommonItemResponse> tourAPIItemList, GetPlaceRequestDto request) {
        int startIndex = (request.getPage() - 1) * request.getPageSize();
        int endIndex = Math.min(startIndex + request.getPageSize(), tourAPIItemList.size());

        if (startIndex >= tourAPIItemList.size()) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION);
        }

        return tourAPIItemList.subList(startIndex, endIndex);
    }

    /**
     * 위치 기반 관광 정보 리스트 조회
     * @return 위치 기반 PlaceResponseDto 타입의 Response
     */
    private PlaceResponseDto getLocationBasedPlace(Long userId, GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = fetchLocationBasedTourAPI(request, filterOption);

        return PlaceResponseDto.of(
                convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList(), userId, request.getMapX(), request.getMapY()),
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize(),
                request.getArrangeType()
        );
    }

    /**
     * 위치 기반 TOUR API 통신
     * @return 위치 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse fetchLocationBasedTourAPI(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = tourAPIService.getLocationBasedTourApi(
                request.getMapX(),
                request.getMapY(),
                request.getPage(),
                request.getPageSize(),
                filterOption.getContentType(),
                filterOption.getArrangeType()
        );
        validateTourAPIResponse(tourAPIResponse.getTotalCount());

        List<TourAPICommonItemResponse> removedTourCourseTourAPIItemList = new ArrayList<>(tourAPIResponse.getTourAPICommonItemResponseList());
        if(filterOption.isEmptyContentType()) {
            removedTourCourseTourAPIItemList = removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());
        }
        List<TourAPICommonItemResponse> removedDivingContentTourAPIItemList = removeDivingContent(removedTourCourseTourAPIItemList);

        return TourAPICommonListResponse.of(
                removedDivingContentTourAPIItemList,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize()
        );
    }

    /**
     * 예외 처리 : API 응답 아이템이 존재하지 않음.
     */
    private void validateTourAPIResponse(int totalCount) {
        if (totalCount == 0) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION);
        }
    }

    /**
     * TOUR API 통신 데이터에서 Tour Course 콘텐츠 타입의 데이터를 삭제
     */
    private List<TourAPICommonItemResponse> removeTourCourse(List<TourAPICommonItemResponse> tourAPIItemList) {
        List<TourAPICommonItemResponse> filteredItemList = new ArrayList<>(tourAPIItemList);

        filteredItemList.removeIf(item -> item.getContentTypeId() == REMOVE_TARGET_CONTENT_TYPE_ID);

        return filteredItemList;
    }

    /**
     * TOUR API 통신 데이터에서 Diving Content 콘텐츠 타입의 데이터를 제외
     */
    private List<TourAPICommonItemResponse> removeDivingContent(List<TourAPICommonItemResponse> tourAPIItemList) {
        List<TourAPICommonItemResponse> filteredItemList = new ArrayList<>(tourAPIItemList);

        filteredItemList.removeIf(item -> DivingContent.isDivingContent(item.getContentId()));

        return filteredItemList;
    }

    /**
     * 지역 기반 관광 정보 리스트 조회
     * @return 지역 기반 PlaceResponseDto 타입의 Response
     */
    private PlaceResponseDto getAreaBasedPlace(Long userId, GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        String areaCode = fetchAreaCodeAPI("", filterOption.getArea());
        String sigunguCode = filterOption.isEmptySigungu() ?
                "" : fetchAreaCodeAPI(areaCode, filterOption.getSigungu());

        // 초기 area, sigungu 값 초기화가 ""
        LocalResponse localResponse = fetchSearchAddressLocalAPI(String.format("%s %s", filterOption.getArea(), filterOption.getSigungu()));

        PlaceFilterOptions areaFilterOption = filterOption.withAreaCodeAndSigunguCode(areaCode, sigunguCode);

        TourAPICommonListResponse tourAPIResponse = fetchAreaBasedTourAPI(request, areaFilterOption);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList(), userId, request.getMapX(), request.getMapY());

        return PlaceResponseDto.of(
                simplePlaceResponse,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize(),
                request.getArrangeType(),
                localResponse.getAddressItems().get(0).getX(),
                localResponse.getAddressItems().get(0).getY()
        );
    }

    /**
     * 지역 코드 조회 TOUR API 통신
     * @return 지역 코드
     */
    private String fetchAreaCodeAPI(String areaCode, String areaName) {
        AreaCodeList areaCodeList = tourAPIService.getAreaCodeApi(areaCode);
        String findCode = areaCodeList.getAreaCodeByName(areaName);

        validateAreaCode(findCode);

        return findCode;
    }

    /**
     * 예외 처리 : Area Code를 찾을 수 없음
     */
    private void validateAreaCode(String findCode) {
        if (ValidatorUtil.isEmpty(findCode)) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION);
        }
    }

    /**
     * 지역 기반 TOUR API 통신
     * @return 지역 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse fetchAreaBasedTourAPI(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = tourAPIService.getAreaBasedTourApi(
                filterOption.getAreaCode(),
                filterOption.getSigunguCode(),
                request.getPage(),
                request.getPageSize(),
                filterOption.getContentType(),
                filterOption.getArrangeType()
        );

        validateTourAPIResponse(tourAPIResponse.getTotalCount());

        List<TourAPICommonItemResponse> removedTourCourseTourAPIItemList = new ArrayList<>(tourAPIResponse.getTourAPICommonItemResponseList());
        if(filterOption.isEmptyContentType()) {
            removedTourCourseTourAPIItemList = removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());
        }
        List<TourAPICommonItemResponse> removedDivingContentTourAPIItemList = removeDivingContent(removedTourCourseTourAPIItemList);

        return TourAPICommonListResponse.of(
                removedDivingContentTourAPIItemList,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize()
        );
    }

    /**
     * TOUR API 응답 리스트를 SimplePlaceInformationDto 리스트로 변환
     */
    private List<SimplePlaceInformationDto> convertTourAPIItemsToSimpleInfo(List<TourAPICommonItemResponse> tourAPIItemList, Long userId, double reqX, double reqY) {
        return tourAPIItemList.stream()
                .map(item -> createSimpleInfo(item, userId, reqX, reqY))
                .collect(Collectors.toList());
    }

    /**
     * SimplePlaceInformationDto 생성
     */
    private SimplePlaceInformationDto createSimpleInfo(TourAPICommonItemResponse tourAPIItem, Long userId, double reqX, double reqY) {
        boolean existsDib = isDibbedByUser(tourAPIItem.getContentId(), userId);
        return SimplePlaceInformationDto.of(
                tourAPIItem.getTitle(),
                tourAPIItem.getAddress(),
                tourAPIItem.getContentId(),
                ContentType.getContentTypeByCode(tourAPIItem.getContentTypeId()),
                tourAPIItem.convertDistanceByFilter(reqX, reqY),
                tourAPIItem.getMapX(),
                tourAPIItem.getMapY(),
                tourAPIItem.getThumbnail(),
                tourAPIItem.getTel(),
                existsDib
        );
    }

    /**
     * 관광 정보 키워드 검색
     */
    public SearchPlaceResponseDto searchKeyword(Long userId, SearchPlaceRequestDto request) {
        if(isLocationKeyword(request.getKeyword())) {
            return searchAreaKeyword(request);
        } else {
            return searchPlaceKeyword(userId, request);
        }
    }

    /**
     * 키워드의 지역명 여부
     */
    private boolean isLocationKeyword(String keyword) {
        LocalResponse localResponse = fetchSearchAddressLocalAPI(keyword);
        return ValidatorUtil.isNotEmpty(localResponse.getAddressItems());
    }

    private LocalResponse fetchSearchAddressLocalAPI(String keyword) {
        return kakaoLocalAPIService.getSearchAddressLocalApi(keyword);
    }

    /**
     * 지역명 검색
     * @return 지역명 기반 SearchPlaceResponseDto 타입의 Response
     */
    private SearchPlaceResponseDto searchAreaKeyword(SearchPlaceRequestDto request) {
        // 지역 기반 검색 여부 확인
        boolean isAreaSearch = true;
        LocalResponse localAPIResponse = fetchSearchAddressLocalAPI(request);

        List<SimpleAreaResponseDto> simplePlaceResponse = convertLocalAPIItemsToSimpleInfo(localAPIResponse.getAddressItems(), request.getMapX(), request.getMapY());

        return SearchPlaceResponseDto.of(
                request.getKeyword(),
                simplePlaceResponse,
                isAreaSearch,
                simplePlaceResponse.size()
        );
    }

    /**
     * 지역명 기반 KAKAO LOCAL API 통신
     * @return 지역명 기반 LocalResponse 타입의 KAKAO LOCAL API Response
     */
    private LocalResponse fetchSearchAddressLocalAPI(SearchPlaceRequestDto request) {
        LocalResponse localAPIResponse = kakaoLocalAPIService.getSearchAddressLocalApi(request.getKeyword());

        return localAPIResponse;
    }

    /**
     * KAKAO LOCAL API 응답을 SimpleAreaResponseDto 리스트로 변환
     */
    private List<SimpleAreaResponseDto> convertLocalAPIItemsToSimpleInfo(List<AddressItem> localAPIItemList, double reqX, double reqY) {
        String regionAddressType = "REGION";

        return localAPIItemList.stream().map(item -> {
                    Address addressInfo = item.getAddressType().equals(regionAddressType) ? item.getAddress() : item.getRoadAddress();

                    return SimpleAreaResponseDto.of(
                            addressInfo.getAddressName(),
                            addressInfo.getRegion1depthName(),
                            addressInfo.getRegion2depthName(),
                            item.getX(),
                            item.getY(),
                            item.convertDistance(reqX, reqY));
                })
                .collect(Collectors.toList());
    }

    /**
     * 관광 정보 검색
     * @return 키워드 기반 SearchPlaceResponseDto 타입의 Response
     */
    private SearchPlaceResponseDto searchPlaceKeyword(Long userId, SearchPlaceRequestDto request) {
        boolean isAreaSearch = false;
        TourAPICommonListResponse tourAPIResponse = fetchSearchKeywordTourAPI(request);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList(), userId, request.getMapX(), request.getMapY());

        return SearchPlaceResponseDto.of(
                request.getKeyword(),
                simplePlaceResponse,
                isAreaSearch,
                tourAPIResponse.getTotalCount(),
                request.getPage(),
                request.getPageSize()
        );
    }

    /**
     * 키워드 기반 TOUR API 통신
     * @return 키워드 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse fetchSearchKeywordTourAPI(SearchPlaceRequestDto request) {
        TourAPICommonListResponse tourAPIResponse = tourAPIService.getSearchKeywordTourApi(request.getKeyword(), request.getPage(), request.getPageSize());

        validateTourAPIResponse(tourAPIResponse.getTotalCount());
        List<TourAPICommonItemResponse> removedTourCourseTourAPIItemList = removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());
        List<TourAPICommonItemResponse> removedDivingContentTourAPIItemList = removeDivingContent(removedTourCourseTourAPIItemList);

        return TourAPICommonListResponse.of(
                removedDivingContentTourAPIItemList,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize()
        );
    }

    /**
     * 관광 정보 상세 조회
     */
    public DetailPlaceInformationResponseDto getPlaceDetail(Long userId, GetPlaceDetailRequestDto request) {
        Long contentId = request.getContentId();
        ContentType contentType = request.getContentType();
        if(contentType == ContentType.DIVING) {
            // 다이빙 필터로 요청한 경우, API 통신은 LEPORTS로
            contentType = ContentType.LEPORTS;
        }

        DetailCommonItemResponse commonAPIItem = getCommonItem(contentId, contentType);
        DetailIntroItemResponse introAPIItem = getIntroItem(contentId, contentType);
        List<String> imageUrlList = transformImageUrlToString(getImageItemList(contentId));
        List<DetailInfoItemResponse> infoAPIResponse = getInfoItemList(contentId, contentType);

        List<FacilityInfo> facilityInfoByIntro = getFacilityInfo(introAPIItem);
        List<FacilityInfo> facilityInfoByInfo = getFacilityInfo(infoAPIResponse);

        List<FacilityInfo> facilityInfoList = Stream.concat(facilityInfoByIntro.stream(), facilityInfoByInfo.stream())
                .collect(Collectors.toList());

        boolean isDib = isDibbedByUser(contentId, userId);

        return DetailPlaceInformationResponseDto.of(
                request.getContentId(),
                DivingContent.isDivingContent(contentId) ? ContentType.DIVING : request.getContentType(),
                commonAPIItem.getTitle(),
                commonAPIItem.getAddress(),
                commonAPIItem.getMapX(),
                commonAPIItem.getMapY(),
                commonAPIItem.extractOverview(),
                commonAPIItem.extractHomepageUrl(),
                introAPIItem.extractUseTime(),
                introAPIItem.extractTel(),
                introAPIItem.extractRestDate(),
                introAPIItem.extractReservationUrl(),
                introAPIItem.extractEventDate(),
                isDib,
                facilityInfoList,
                imageUrlList
        );
    }

    /**
     * 공통 정보 Item 가져오기
     * @return DetailCommonItemResponse
     */
    private DetailCommonItemResponse getCommonItem(Long contentId, ContentType contentType) {
        return fetchCommonAPI(contentId, String.valueOf(contentType.getCode())).getDetailCommonItemResponse();
    }

    /**
     * 공통 정보 조회 TOUR API 통신
     * @return DetailCommonListResponse 타입의 TOUR API Response
     */
    private DetailCommonListResponse fetchCommonAPI(Long contentId, String contentType) {
        DetailCommonListResponse commonAPIResponse = tourAPIService.getCommonApi(contentId, contentType);

        validateDetailTourAPIResponse(commonAPIResponse.getTotalCount());

        return commonAPIResponse;
    }

    /**
     * 예외 처리 : 존재 하지 없는 contentId
     */
    private void validateDetailTourAPIResponse(int totalCount) {
        if(totalCount == 0) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_TOUR_PLACE);
        }
    }

    /**
     * 소개 정보 Item 가져오기
     * @return DetailIntroItemResponse 타입
     */
    private DetailIntroItemResponse getIntroItem(Long contentId, ContentType contentType) {
        DetailIntroResponse introAPIResponse = fetchIntroAPI(contentId, String.valueOf(contentType.getCode()));

        return detailIntroItemFactory.getIntroItem(contentType, introAPIResponse);
    }

    /**
     * 소개 정보 조회 TOUR API 통신
     * @return DetailIntroResponse 타입의 TOUR API Response
     */
    private DetailIntroResponse fetchIntroAPI(Long contentId, String contentType) {
        return tourAPIService.getIntroApi(contentId, contentType);
    }

    /**
     * 반복 정보 조회 TOUR API 통신
     * @return DetailInfoListResponse 타입의 TOUR API Response
     */
    private DetailInfoListResponse fetchInfoAPI(Long contentId, String contentType) {
        return tourAPIService.getInfoApi(contentId, contentType);
    }

    /**
     * 반복 정보 Item 가져오기
     * @return DetailInfoItemResponse 타입의 List
     */
    private List<DetailInfoItemResponse> getInfoItemList(Long contentId, ContentType contentType) {
        return fetchInfoAPI(contentId, String.valueOf(contentType.getCode())).getDetailInfoItemResponses();
    }

    /**
     * 이미지 정보 조회 TOUR API 통신
     * @return DetailImageListResponse 타입의 TOUR API Response
     */
    private DetailImageListResponse fetchImageAPI(Long contentId) {
        return tourAPIService.getImageApi(contentId);
    }

    /**
     * 이미지 정보 Item 가져오기
     * @return DetailImageItemResponse 타입의 List
     */
    private List<DetailImageItemResponse> getImageItemList(Long contentId) {
        return fetchImageAPI(contentId).getDetailImageItemResponses();
    }

    /**
     * TOUR API 이미지 응답 리스트에서 URL만 추출하여 String 리스트로 변환
     * @return 이미지 URL String 타입의 List
     */
    private List<String> transformImageUrlToString(List<DetailImageItemResponse> imageAPIItemList) {
        return (ValidatorUtil.isEmpty(imageAPIItemList)) ?
                null : imageAPIItemList.stream().map(image -> image.getOriginImageUrl()).collect(Collectors.toList());
    }

    /**
     * 편의 시설 정보 조회 (intro item)
     * @return FacilityInfo 타입의 List
     */
    private List<FacilityInfo> getFacilityInfo(DetailIntroItemResponse introAPIItem) {
        return introAPIItem.getFacilityAvailabilityInfo();
    }

    /**
     * 편의 시설 정보 조회 (info item)
     * @return FacilityInfo 타입의 List
     */
    private List<FacilityInfo> getFacilityInfo(List<DetailInfoItemResponse> infoAPIItemList) {
        List<FacilityInfo> updatedFacilityInfoList = new ArrayList<>();
        String restroomName = "화장실";
        String disableName = "장애인 편의시설";

        boolean flagOfRestroom = false;
        boolean flagOfDisable = false;

        for(DetailInfoItemResponse infoItem : infoAPIItemList) {
            if(infoItem.getInfoName().contains(restroomName)) { flagOfRestroom = true; }
            if(infoItem.getInfoName().contains(disableName)) { flagOfDisable = true; }
        }

        updatedFacilityInfoList.add(FacilityInfo.of(FacilityType.RESTROOM, flagOfRestroom));
        updatedFacilityInfoList.add(FacilityInfo.of(FacilityType.DISABLED_PERSON_FACILITY, flagOfDisable));

        return updatedFacilityInfoList;
    }

    /**
     * 관광 정보 찜 등록
     */
    @Transactional
    public void addPlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        if(dibRepository.existsByUserAndContentId(user, contentId)) {
            return;
        }

        // 공통 정보
        DetailCommonItemResponse commonItem = fetchCommonAPI(contentId, "").getDetailCommonItemResponse();

        dibRepository.save(Dib.of(
                contentId,
                DivingContent.isDivingContent(contentId) ? ContentType.DIVING.getCode() : commonItem.getContentTypeId(),
                commonItem.getTitle(),
                commonItem.getAddress(),
                commonItem.getTel(),
                commonItem.getThumbnail(),
                user
        ));
    }

    @Transactional
    boolean isDibbedByUser(Long contentId, Long userId) {
        if(ValidatorUtil.isNotEmpty(userId)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
            return dibRepository.existsByUserAndContentId(user, contentId);
        }
        return false;
    }

    /**
     * 관광 정보 찜 삭제
     */
    @Transactional
    public void removePlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        dibRepository.deleteByUserAndContentId(user, contentId);
    }
}
