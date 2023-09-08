package com.oceans7.dib.domain.place.service;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.FacilityType;
import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto.FacilityInfo;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.AddressItem.*;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemFactoryImpl;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.CoordinateUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlaceService {

    private final DataGoKrAPIService tourAPIService;
    private final KakaoLocalAPIService kakaoLocalAPIService;

    private final UserRepository userRepository;
    private final DibRepository dibRepository;

    /**
     * 관광 정보 리스트 조회 (위치 기반/지역 필터링)
     * @return PlaceResponseDto 타입의 Response
     */
    public PlaceResponseDto getPlace(GetPlaceRequestDto request, String contentType, String arrangeType) {
        if(ValidatorUtil.isNotEmpty(request.getArea())) {
            return getAreaBasedPlace(request, contentType, arrangeType);
        } else {
            return getLocationBasedPlace(request, contentType, arrangeType);
        }
    }

    /**
     * 위치 기반 관광 정보 리스트 조회
     * @return 위치 기반 PlaceResponseDto 타입의 Response
     */
    private PlaceResponseDto getLocationBasedPlace(GetPlaceRequestDto request, String contentType, String arrangeType) {
        TourAPICommonListResponse tourAPIResponseList = getLocationBasedTourApi(request, contentType, arrangeType);

        List<SimplePlaceInformationDto> simplePlaceResponse = transformApiResponseToSimplePlace(tourAPIResponseList);

        return PlaceResponseDto.of(
                simplePlaceResponse,
                tourAPIResponseList.getTotalCount(),
                tourAPIResponseList.getPage(),
                simplePlaceResponse.size(),
                request.getArrangeType()
        );
    }

    /**
     * 위치 기반 TOUR API 통신
     * @return 위치 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse getLocationBasedTourApi(GetPlaceRequestDto request, String contentType, String arrangeType) {
        TourAPICommonListResponse tourAPIResponseList = tourAPIService.getLocationBasedTourApi(request.getMapX(), request.getMapY(), request.getPage(), request.getPageSize(), contentType, arrangeType);

        notFoundApiItemException(tourAPIResponseList.getTotalCount());

        return filterAndConvertDistance(tourAPIResponseList);
    }

    /**
     * 거리 단위 변환 및 필터링 수행
     */
    private TourAPICommonListResponse filterAndConvertDistance(TourAPICommonListResponse tourAPIResponseList) {
        List<TourAPICommonItemResponse> tourApiItemList = tourAPIResponseList.getTourAPICommonItemResponseList();
        removeTourCourseTypeData(tourApiItemList);

        // TODO : DISTANCE 초근접 거리는 0.0으로 표시되는 이슈 해결하기
        tourApiItemList.forEach(item -> item.convertDistanceMetersToKilometers());

        return tourAPIResponseList;
    }

    /**
     * TOUR API 통신 데이터에서 Tour Course 콘텐츠 타입의 데이터를 삭제
     */
    private void removeTourCourseTypeData(List<TourAPICommonItemResponse> tourApiItemList) {
        int tourCourseContentTypeId = 25;
        tourApiItemList.removeIf(item -> item.getContentTypeId() == tourCourseContentTypeId);
    }

    /**
     * 예외 처리
     * TOUR API 응답 아이템이 존재하지 않음
     */
    private void notFoundApiItemException(int totalCountOfApiResponse) {
        if(totalCountOfApiResponse == 0) { throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION); }
    }

    /**
     * 지역 기반 관광 정보 리스트 조회
     * @return 지역 기반 PlaceResponseDto 타입의 Response
     */
    private PlaceResponseDto getAreaBasedPlace(GetPlaceRequestDto request, String contentType, String arrangeType) {
        TourAPICommonListResponse tourAPIResponseList = getAreaBasedTourApi(request, contentType, arrangeType);

        List<TourAPICommonItemResponse> tourApiItemList = tourAPIResponseList.getTourAPICommonItemResponseList();
        removeTourCourseTypeData(tourApiItemList);

        tourApiItemList.forEach(item -> item.calculateDistance(request.getMapX(), request.getMapY()));

        List<SimplePlaceInformationDto> simplePlaceResponse = transformApiResponseToSimplePlace(tourAPIResponseList);

        return PlaceResponseDto.of(
                simplePlaceResponse,
                tourAPIResponseList.getTotalCount(),
                tourAPIResponseList.getPage(),
                simplePlaceResponse.size(),
                request.getArrangeType()
        );
    }

    /**
     * 지역 기반 TOUR API 통신
     * @return 지역 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse getAreaBasedTourApi(GetPlaceRequestDto request, String contentType, String arrangeType) {
        String areaCode, sigunguCode = "";

        areaCode = getAreaCodeApi("", request.getArea());
        if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
            sigunguCode = getAreaCodeApi(areaCode, request.getSigungu());
        }

        TourAPICommonListResponse tourAPIResponseList =  tourAPIService.getAreaBasedTourApi(areaCode, sigunguCode, request.getPage(), request.getPageSize(), contentType, arrangeType);

        notFoundApiItemException(tourAPIResponseList.getTotalCount());

        return filterAndCalculateDistance(tourAPIResponseList, request.getMapX(), request.getMapY());
    }

    /**
     * 지역 코드 조회 TOUR API 통신
     * @return 지역 코드
     */
    private String getAreaCodeApi(String areaCode, String areaName) {
        AreaCodeList areaCodeList = tourAPIService.getAreaCodeApi(areaCode);
        String findCode = areaCodeList.getAreaCodeByName(areaName);

        notFoundAreaItemException(findCode);

        return findCode;
    }

    /**
     * TOUR API 지역 코드 조회 응답 아이템 카운트가 0일 때 발생하는 예외 처리
     */
    private void notFoundAreaItemException(String findCode) {
        if (ValidatorUtil.isEmpty(findCode)) { throw new ApplicationException(ErrorCode.NOT_FOUNT_AREA_NAME); }
    }

    /**
     * 거리 계산 및 필터링 수행
     */
    private TourAPICommonListResponse filterAndCalculateDistance(TourAPICommonListResponse tourAPIResponseList, double reqX, double reqY) {
        List<TourAPICommonItemResponse> tourApiItemList = tourAPIResponseList.getTourAPICommonItemResponseList();
        removeTourCourseTypeData(tourApiItemList);

        tourApiItemList.forEach(item -> item.calculateDistance(reqX, reqY));
        return tourAPIResponseList;
    }

    /**
     * TOUR API 응답 리스트를 SimplePlaceInformationDto 리스트로 변환
     */
    private List<SimplePlaceInformationDto> transformApiResponseToSimplePlace (TourAPICommonListResponse tourAPIResponseList) {
        return tourAPIResponseList.getTourAPICommonItemResponseList().stream().map(SimplePlaceInformationDto :: of).collect(Collectors.toList());
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
        boolean isLocationSearch = true;
        LocalResponse localResponse = getSearchAddressLocalApi(request);

        List<SimpleAreaResponseDto> simpleDto = transformApiResponseToSimpleArea(localResponse, request.getMapX(), request.getMapY());

        return SearchPlaceResponseDto.of(
                request.getKeyword(),
                simpleDto,
                isLocationSearch
        );
    }

    /**
     * 지역명 기반 KAKAO LOCAL API 통신
     * @return 지역명 기반 LocalResponse 타입의 KAKAO LOCAL API Response
     */
    private LocalResponse getSearchAddressLocalApi(SearchPlaceRequestDto request) {
        return kakaoLocalAPIService.getSearchAddressLocalApi(request.getKeyword());
    }

    /**
     * KAKAO LOCAL API 응답을 SimpleAreaResponseDto 리스트로 변환
     */
    private List<SimpleAreaResponseDto> transformApiResponseToSimpleArea(LocalResponse localResponse, double reqX, double reqY) {
        String regionAddressType = "REGION";
        return localResponse.getAddressItems().stream()
                .map(item -> {
                    Address addressInfo = item.getAddressType().equals(regionAddressType) ? item.getAddress() : item.getRoadAddress();
                    return SimpleAreaResponseDto.of(
                            addressInfo.getAddressName(), addressInfo.getRegion1depthName(), addressInfo.getRegion2depthName(),
                            item.getX(), item.getY(), CoordinateUtil.calculateDistance(reqX, reqY, item.getX(), item.getY()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 관광 정보 검색
     * @return 키워드 기반 SearchPlaceResponseDto 타입의 Response
     */
    private SearchPlaceResponseDto searchPlaceKeyword(SearchPlaceRequestDto request) {
        boolean isLocationSearch = false;
        TourAPICommonListResponse apiResponseList = getSearchKeywordTourApi(request);

        List<SimplePlaceInformationDto> simpleDto = transformApiResponseToSimplePlace(apiResponseList);

        return SearchPlaceResponseDto.of(
                request.getKeyword(),
                simpleDto,
                isLocationSearch,
                apiResponseList.getTotalCount(),
                apiResponseList.getPage(),
                apiResponseList.getPageSize()
        );
    }

    /**
     * 키워드 기반 TOUR API 통신
     * @return 키워드 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse getSearchKeywordTourApi(SearchPlaceRequestDto request) {
        TourAPICommonListResponse apiResponseList = tourAPIService.getSearchKeywordTourApi(request.getKeyword(), request.getPage(), request.getPageSize());

        notFoundApiItemException(apiResponseList.getTotalCount());

        return filterAndCalculateDistance(apiResponseList, request.getMapX(), request.getMapY());
    }

    /**
     * 관광 정보 상세 조회
     */
    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto request) {
        Long contentId = request.getContentId();
        ContentType contentType = request.getContentType();

        DetailCommonItemResponse commonItem = getCommonItem(contentId, contentType);
        DetailIntroItemResponse introItem = getIntroItem(contentId, contentType);
        List<String> imageUrlList = transformImageUrlToString(getImageItemList(contentId));
        List<DetailInfoItemResponse> infoApiResponseList = getInfoItemList(contentId, contentType);

        List<FacilityInfo> facilityInfoList = getFacilityInfo(introItem, infoApiResponseList);

        return DetailPlaceInformationResponseDto.of(
                request.getContentId(),
                request.getContentType(),
                commonItem.getTitle(),
                commonItem.getAddress(),
                commonItem.getMapX(),
                commonItem.getMapY(),
                commonItem.extractOverview(),
                commonItem.extractHomepageUrl(),
                introItem.extractUseTime(),
                introItem.extractTel(),
                introItem.extractRestDate(),
                introItem.extractReservationUrl(),
                introItem.extractEventDate(),
                facilityInfoList,
                imageUrlList);
    }

    /**
     * 공통 정보 조회 TOUR API 통신
     * @return DetailCommonListResponse 타입의 TOUR API Response
     */
    private DetailCommonListResponse getCommonApi(Long contentId, String contentType) {
        return tourAPIService.getCommonApi(contentId, contentType);
    }

    /**
     * 공통 정보 Item 가져오기
     * @return DetailCommonItemResponse
     */
    private DetailCommonItemResponse getCommonItem(Long contentId, ContentType contentType) {
        return getCommonApi(contentId, String.valueOf(contentType.getCode())).getDetailCommonItemResponse();
    }

    /**
     * 소개 정보 조회 TOUR API 통신
     * @return DetailIntroResponse 타입의 TOUR API Response
     */
    private DetailIntroResponse getIntroApi(Long contentId, String contentType) {
        return tourAPIService.getIntroApi(contentId, contentType);
    }

    /**
     * 소개 정보 Item 가져오기
     * @return DetailIntroItemResponse 타입
     */
    private DetailIntroItemResponse getIntroItem(Long contentId, ContentType contentType) {
        DetailIntroItemFactoryImpl detailIntroItemFactory = new DetailIntroItemFactoryImpl();
        DetailIntroResponse introApiResponse = getIntroApi(contentId, String.valueOf(contentType.getCode()));
        return detailIntroItemFactory.getIntroItem(contentType, introApiResponse);
    }

    /**
     * 반복 정보 조회 TOUR API 통신
     * @return DetailInfoListResponse 타입의 TOUR API Response
     */
    private DetailInfoListResponse getInfoApi(Long contentId, String contentType) {
        return tourAPIService.getInfoApi(contentId, contentType);
    }

    /**
     * 반복 정보 Item 가져오기 (TOURIST_SPOT 타입 한정)
     * @return DetailInfoItemResponse 타입의 List
     */
    private List<DetailInfoItemResponse> getInfoItemList(Long contentId, ContentType contentType) {
        return (contentType != ContentType.TOURIST_SPOT) ?
                null : getInfoApi(contentId, String.valueOf(contentType.getCode())).getDetailInfoItemResponses();
    }

    /**
     * 이미지 정보 조회 TOUR API 통신
     * @return DetailImageListResponse 타입의 TOUR API Response
     */
    private DetailImageListResponse getImageApi(Long contentId) {
        return tourAPIService.getImageApi(contentId);
    }

    /**
     * 이미지 정보 Item 가져오기
     * @return DetailImageItemResponse 타입의 List
     */
    private List<DetailImageItemResponse> getImageItemList(Long contentId) {
        return getImageApi(contentId).getDetailImageItemResponses();
    }

    /**
     * TOUR API 이미지 응답 리스트에서 URL만 추출하여 String 리스트로 변환
     * @return 이미지 URL String 타입의 List
     */
    private List<String> transformImageUrlToString(List<DetailImageItemResponse> imageApiResponseItemList) {
        return (ValidatorUtil.isEmpty(imageApiResponseItemList)) ?
                null : imageApiResponseItemList.stream().map(image -> image.getOriginImageUrl()).collect(Collectors.toList());
    }

    /**
     * 편의 시설 정보 조회
     * @return FacilityInfo 타입의 List
     */
    private List<FacilityInfo> getFacilityInfo(DetailIntroItemResponse introItem, List<DetailInfoItemResponse> infoItemList) {
        List<FacilityInfo> facilityInfoList = introItem.getFacilityAvailabilityInfo();

        addFacilityInfoByInfoItem(facilityInfoList, infoItemList);

        return facilityInfoList;
    }

    /**
     * Intro API 응답에서 편의 시설 정보 유무 조회
     * @return FacilityInfo 타입의 List
     */
    private void addFacilityInfoByInfoItem(List<FacilityInfo> facilityInfoList, List<DetailInfoItemResponse> infoItemList) {
        String restroomName = "화장실";
        String disableName = "장애인 편의시설";

        if(ValidatorUtil.isNotEmpty(infoItemList)) {
            boolean flagOfRestroom = false;
            boolean flagOfDisable = false;

            for(DetailInfoItemResponse infoItem : infoItemList) {
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
        DetailCommonItemResponse commonItem = getCommonApi(contentId, "").getDetailCommonItemResponse();

        dibRepository.save(Dib.of(contentId, commonItem.getContentTypeId(), commonItem.getTitle(), commonItem.getAddress(), commonItem.getTel(), commonItem.getThumbnail(), user));
    }

    /**
     * 관광 정보 찜 삭제
     */
    @Transactional
    public void removePlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        dibRepository.deleteByUserAndContentId(user, contentId);
    }


}
