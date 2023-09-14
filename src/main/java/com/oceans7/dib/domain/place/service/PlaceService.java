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
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemFactoryImpl;
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


@Service
@RequiredArgsConstructor
public class PlaceService {
    private final DataGoKrAPIService tourAPIService;
    private final KakaoLocalAPIService kakaoLocalAPIService;

    private final UserRepository userRepository;
    private final DibRepository dibRepository;

    private final static String[] KEYWORD_FOR_DIVING_FILTER = { "다이빙", "다이브" };
    private final static int PAGE_FOR_DIVING_FILTER = 1;
    private final static int PAGE_SIZE_FOR_DIVING_FILTER = 4;
    private final static int RADIUS_KM = 20;
    private final static int REMOVE_TARGET_CONTENT_TYPE_ID = 25;

    /**
     * 관광 정보 리스트 조회 (위치 기반/지역 필터링)
     * @return PlaceResponseDto 타입의 Response
     */
    public PlaceResponseDto getPlace(GetPlaceRequestDto request, PlaceFilterOptions placeFilterOptions) {
        switch(placeFilterOptions.getFilterType()) {
            case DIVING -> { return getDivingFilteredPlace(request, placeFilterOptions); }
            case LOCATION_BASED -> { return getLocationBasedPlace(request, placeFilterOptions); }
            case AREA_BASED -> { return getAreaBasedPlace(request, placeFilterOptions); }
            default -> { return null; }
        }
    }

    /**
     * 다이빙 필터 기반 관광 정보 리스트 조회
     * @return 다이빙 필터 기반의 PlaceResponseDto 타입의 Response
     */
    public PlaceResponseDto getDivingFilteredPlace(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = fetchDivingFilteredTourAPI(request, filterOption);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList());

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

        for (String keyword : KEYWORD_FOR_DIVING_FILTER) {
            SearchPlaceRequestDto searchRequest = SearchPlaceRequestDto.builder()
                    .keyword(keyword)
                    .mapX(request.getMapX())
                    .mapY(request.getMapY())
                    .page(PAGE_FOR_DIVING_FILTER)
                    .pageSize(PAGE_SIZE_FOR_DIVING_FILTER)
                    .build();
            TourAPICommonListResponse tourAPIResponse = fetchSearchKeywordTourAPI(searchRequest);
            tourAPIItemList.addAll(tourAPIResponse.getTourAPICommonItemResponseList());
        }

        validateTourAPIResponse(tourAPIItemList.size());
        applyFilterOption(tourAPIItemList, request, filterOption);

        List<TourAPICommonItemResponse> paginateTourAPIItemList = paginateItems(tourAPIItemList, request);

        return TourAPICommonListResponse.builder()
                .tourAPICommonItemResponseList(paginateTourAPIItemList)
                .totalCount(tourAPIItemList.size())
                .page(request.getPageSize())
                .pageSize(request.getPage())
                .build();
    }

    /**
     * filterOption에 따라 필터를 적용
     */
    private void applyFilterOption(List<TourAPICommonItemResponse> tourAPIItemList, GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        if(filterOption.isEmptyArea()) {
            filterItemsByLocation(tourAPIItemList, request);
        }

        if(!filterOption.isEmptyArea()) {
            filterItemsByArea(tourAPIItemList, request);
        }

        if(!filterOption.isEmptyArrangeType()) {
            filterItemsByArrangeType(tourAPIItemList, filterOption);
        }
    }

    /**
     * 지역 기반 조회의 다이빙 필터 데이터에서 지역명을 제외한 데이터를 삭제
     */
    private void filterItemsByArea(List<TourAPICommonItemResponse> tourApiItemList, GetPlaceRequestDto request) {
        tourApiItemList.removeIf(item -> !(item.getAddress().contains(request.getArea())));

        if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
            tourApiItemList.removeIf(item -> !(item.getAddress().contains(request.getSigungu())));
        }
    }

    /**
     * 위치 기반 조회의 다이빙 필터 데이터에서 거리 계산 후 정렬
     */
    private void filterItemsByLocation(List<TourAPICommonItemResponse> tourApiItemList, GetPlaceRequestDto request) {
        tourApiItemList.forEach(item -> item.calculateDistance(request.getMapX(), request.getMapY()));
        tourApiItemList.removeIf(item -> item.getDistance() > RADIUS_KM);
        tourApiItemList.sort(Comparator.comparingDouble(TourAPICommonItemResponse::getDistance));
    }

    /**
     * 다이빙 필터 콘텐츠를 사용자 요구사항에 맞추어 정렬
     */
    private void filterItemsByArrangeType(List<TourAPICommonItemResponse> tourApiItemList, PlaceFilterOptions filterOption) {
        ArrangeType arrangeType = ArrangeType.valueOf(filterOption.getArrangeType());
        switch(arrangeType) {
            case A -> tourApiItemList.sort(Comparator.comparing(TourAPICommonItemResponse::getTitle));
            case C -> tourApiItemList.sort(Comparator.comparing(TourAPICommonItemResponse::parseModifiedTimeToDateTime).reversed());
        }
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
    private PlaceResponseDto getLocationBasedPlace(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        TourAPICommonListResponse tourAPIResponse = fetchLocationBasedTourAPI(request, filterOption);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList());

        return PlaceResponseDto.of(
                simplePlaceResponse,
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
        if(filterOption.isEmptyContentType()) {
            removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());
        }

        convertDistanceForTourItem(tourAPIResponse.getTourAPICommonItemResponseList());

        return tourAPIResponse;
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
     * 거리 단위 변환
     */
    private void convertDistanceForTourItem(List<TourAPICommonItemResponse> tourAPIItemList) {
        tourAPIItemList.forEach(item -> item.convertDistanceMetersToKilometers());
    }

    /**
     * TOUR API 통신 데이터에서 Tour Course 콘텐츠 타입의 데이터를 삭제
     */
    private void removeTourCourse(List<TourAPICommonItemResponse> tourAPIItemList) {
        tourAPIItemList.removeIf(item -> item.getContentTypeId() == REMOVE_TARGET_CONTENT_TYPE_ID);
    }

    /**
     * 지역 기반 관광 정보 리스트 조회
     * @return 지역 기반 PlaceResponseDto 타입의 Response
     */
    private PlaceResponseDto getAreaBasedPlace(GetPlaceRequestDto request, PlaceFilterOptions filterOption) {
        String areaCode = fetchAreaCodeAPI("", filterOption.getArea());
        String sigunguCode = filterOption.isEmptySigungu() ?
                "" : fetchAreaCodeAPI(areaCode, filterOption.getSigungu());

        PlaceFilterOptions areaFilterOption = filterOption.withAreaCodeAndSigunguCode(areaCode, sigunguCode);

        TourAPICommonListResponse tourAPIResponse = fetchAreaBasedTourAPI(request, areaFilterOption);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList());

        return PlaceResponseDto.of(
                simplePlaceResponse,
                tourAPIResponse.getTotalCount(),
                tourAPIResponse.getPage(),
                tourAPIResponse.getPageSize(),
                request.getArrangeType()
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
        if(filterOption.isEmptyContentType()) {
            removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());
        }

        calculateDistanceForTourItem(tourAPIResponse.getTourAPICommonItemResponseList(), request.getMapX(), request.getMapY());

        return tourAPIResponse;
    }

    /**
     * 거리 계산 및 필터링 수행
     */
    private void calculateDistanceForTourItem(List<TourAPICommonItemResponse> tourAPIItemList, double reqX, double reqY) {
        tourAPIItemList.forEach(item -> item.calculateDistance(reqX, reqY));
    }

    /**
     * TOUR API 응답 리스트를 SimplePlaceInformationDto 리스트로 변환
     */
    private List<SimplePlaceInformationDto> convertTourAPIItemsToSimpleInfo (List<TourAPICommonItemResponse> tourAPIItemList) {
        return tourAPIItemList.stream().map(SimplePlaceInformationDto :: of).collect(Collectors.toList());
    }

    /**
     * 관광 정보 키워드 검색
     */
    public SearchPlaceResponseDto searchKeyword(SearchPlaceRequestDto request) {
        if(isLocationKeyword(request.getKeyword())) {
            return searchAreaKeyword(request);
        } else {
            return searchPlaceKeyword(request);
        }
    }

    /**
     * 키워드의 지역명 여부
     */
    private boolean isLocationKeyword(String keyword) {
        LocalResponse localResponse = kakaoLocalAPIService.getSearchAddressLocalApi(keyword);
        return ValidatorUtil.isNotEmpty(localResponse.getAddressItems());
    }

    /**
     * 지역명 검색
     * @return 지역명 기반 SearchPlaceResponseDto 타입의 Response
     */
    private SearchPlaceResponseDto searchAreaKeyword(SearchPlaceRequestDto request) {
        // 지역 기반 검색 여부 확인
        boolean isAreaSearch = true;
        LocalResponse localAPIResponse = fetchSearchAddressLocalAPI(request);

        List<SimpleAreaResponseDto> simplePlaceResponse = convertLocalAPIItemsToSimpleInfo(localAPIResponse.getAddressItems());

        return SearchPlaceResponseDto.of(
                request.getKeyword(),
                simplePlaceResponse,
                isAreaSearch
        );
    }

    /**
     * 지역명 기반 KAKAO LOCAL API 통신
     * @return 지역명 기반 LocalResponse 타입의 KAKAO LOCAL API Response
     */
    private LocalResponse fetchSearchAddressLocalAPI(SearchPlaceRequestDto request) {
        LocalResponse localAPIResponse = kakaoLocalAPIService.getSearchAddressLocalApi(request.getKeyword());

        calculateDistanceForLocalAPIItem(localAPIResponse.getAddressItems(), request.getMapX(), request.getMapY());

        return localAPIResponse;
    }

    /**
     * KAKAO LOCAL API 응답을 SimpleAreaResponseDto 리스트로 변환
     */
    private List<SimpleAreaResponseDto> convertLocalAPIItemsToSimpleInfo(List<AddressItem> localAPIItemList) {
        String regionAddressType = "REGION";

        return localAPIItemList.stream().map(item -> {
                    Address addressInfo = item.getAddressType().equals(regionAddressType) ? item.getAddress() : item.getRoadAddress();

                    return SimpleAreaResponseDto.of(
                            addressInfo.getAddressName(),
                            addressInfo.getRegion1depthName(),
                            addressInfo.getRegion2depthName(),
                            item.getX(),
                            item.getY(),
                            item.getDistance());
                })
                .collect(Collectors.toList());
    }

    /**
     * 거리 계산
     */
    private void calculateDistanceForLocalAPIItem(List<AddressItem> localAPIItemList, double reqX, double reqY) {
        localAPIItemList.forEach(item -> item.calculateDistance(reqX, reqY));
    }

    /**
     * 관광 정보 검색
     * @return 키워드 기반 SearchPlaceResponseDto 타입의 Response
     */
    private SearchPlaceResponseDto searchPlaceKeyword(SearchPlaceRequestDto request) {
        boolean isAreaSearch = false;
        TourAPICommonListResponse tourAPIResponse = fetchSearchKeywordTourAPI(request);

        List<SimplePlaceInformationDto> simplePlaceResponse = convertTourAPIItemsToSimpleInfo(tourAPIResponse.getTourAPICommonItemResponseList());

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
        removeTourCourse(tourAPIResponse.getTourAPICommonItemResponseList());

        calculateDistanceForTourItem(tourAPIResponse.getTourAPICommonItemResponseList(), request.getMapX(), request.getMapY());

        return tourAPIResponse;
    }

    /**
     * 관광 정보 상세 조회
     */
    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto request) {
        Long contentId = request.getContentId();
        ContentType contentType = request.getContentType();

        DetailCommonItemResponse commonAPIItem = getCommonItem(contentId, contentType);
        DetailIntroItemResponse introAPIItem = getIntroItem(contentId, contentType);
        List<String> imageUrlList = transformImageUrlToString(getImageItemList(contentId));
        List<DetailInfoItemResponse> infoAPIResponse = getInfoItemList(contentId, contentType);

        List<FacilityInfo> facilityInfoList = getFacilityInfo(introAPIItem, infoAPIResponse);

        return DetailPlaceInformationResponseDto.of(
                request.getContentId(),
                request.getContentType(),
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
        DetailIntroItemFactoryImpl detailIntroItemFactory = new DetailIntroItemFactoryImpl();
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
     * 반복 정보 Item 가져오기 (TOURIST_SPOT 타입 한정)
     * @return DetailInfoItemResponse 타입의 List
     */
    private List<DetailInfoItemResponse> getInfoItemList(Long contentId, ContentType contentType) {
        return (contentType != ContentType.TOURIST_SPOT) ?
                null : fetchInfoAPI(contentId, String.valueOf(contentType.getCode())).getDetailInfoItemResponses();
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
     * 편의 시설 정보 조회
     * @return FacilityInfo 타입의 List
     */
    private List<FacilityInfo> getFacilityInfo(DetailIntroItemResponse introAPIItem, List<DetailInfoItemResponse> infoAPIItemList) {
        List<FacilityInfo> facilityInfoList = introAPIItem.getFacilityAvailabilityInfo();

        addFacilityInfoByInfoItem(facilityInfoList, infoAPIItemList);

        return facilityInfoList;
    }

    /**
     * Intro API 응답에서 편의 시설 정보 유무 조회
     * @return FacilityInfo 타입의 List
     */
    private void addFacilityInfoByInfoItem(List<FacilityInfo> facilityInfoList, List<DetailInfoItemResponse> infoAPIItemList) {
        String restroomName = "화장실";
        String disableName = "장애인 편의시설";

        if(ValidatorUtil.isNotEmpty(infoAPIItemList)) {
            boolean flagOfRestroom = false;
            boolean flagOfDisable = false;

            for(DetailInfoItemResponse infoItem : infoAPIItemList) {
                if(infoItem.getInfoName().contains(restroomName)) { flagOfRestroom = true; }
                if(infoItem.getInfoName().contains(disableName)) { flagOfDisable = true; }
            }

            facilityInfoList.add(FacilityInfo.of(FacilityType.RESTROOM, flagOfRestroom));
            facilityInfoList.add(FacilityInfo.of(FacilityType.DISABLED_PERSON_FACILITY, flagOfDisable));
        }
    }

    /**
     * 관광 정보 찜 등록
     */
    @Transactional
    public void addPlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        Optional<Dib> findDib = dibRepository.findByUserAndContentId(user, contentId);
        if(findDib.isPresent()) { return; }

        // 공통 정보
        DetailCommonItemResponse commonItem = fetchCommonAPI(contentId, "").getDetailCommonItemResponse();

        dibRepository.save(Dib.of(contentId, commonItem.getContentTypeId(), commonItem.getTitle(), commonItem.getAddress(), commonItem.getTel(), commonItem.getThumbnail(), user));
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
