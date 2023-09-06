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
import com.oceans7.dib.global.api.response.kakao.LocalResponse.AddressItem;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.AddressItem.*;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.CoordinateUtil;
import com.oceans7.dib.global.util.TextManipulatorUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        tourAPIResponseList.getTourAPICommonItemResponseList().stream()
                .forEach(item ->  item.convertDistanceMetersToKilometers());

        List<SimplePlaceInformationDto> simplePlaceResponse = transformApiResponseToSimplePlace(tourAPIResponseList);

        return PlaceResponseDto.of(simplePlaceResponse, tourAPIResponseList.getTotalCount(), tourAPIResponseList.getPage(), tourAPIResponseList.getPageSize(), request.getArrangeType());
    }

    /**
     * 위치 기반 TOUR API 통신
     * @return 위치 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse getLocationBasedTourApi(GetPlaceRequestDto request, String contentType, String arrangeType) {
        TourAPICommonListResponse tourAPIResponseList = tourAPIService.getLocationBasedTourApi(
                request.getMapX(), request.getMapY(), request.getPage(), request.getPageSize(), contentType, arrangeType);

        notFoundApiItemException(tourAPIResponseList.getTotalCount());

        return tourAPIResponseList;
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

        tourAPIResponseList.getTourAPICommonItemResponseList().stream()
                .forEach(item -> item.calculateDistance(request.getMapX(), request.getMapY()));

        List<SimplePlaceInformationDto> simplePlaceResponse = transformApiResponseToSimplePlace(tourAPIResponseList);

        return PlaceResponseDto.of(simplePlaceResponse, tourAPIResponseList.getTotalCount(), tourAPIResponseList.getPage(), tourAPIResponseList.getPageSize(), request.getArrangeType());
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

        return tourAPIService.getAreaBasedTourApi(
                areaCode, sigunguCode, request.getPage(), request.getPageSize(), contentType, arrangeType);
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

        return SearchPlaceResponseDto.of(request.getKeyword(), simpleDto, isLocationSearch);
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

        apiResponseList.getTourAPICommonItemResponseList().stream()
                .forEach(item -> item.calculateDistance(request.getMapX(), request.getMapY()));

        List<SimplePlaceInformationDto> simpleDto = transformApiResponseToSimplePlace(apiResponseList);

        return SearchPlaceResponseDto.of(request.getKeyword(), simpleDto, isLocationSearch, apiResponseList.getTotalCount(), apiResponseList.getPage(), apiResponseList.getPageSize());
    }

    /**
     * 키워드 기반 TOUR API 통신
     * @return 키워드 기반 TourAPICommonListResponse 타입의 TOUR API Response
     */
    private TourAPICommonListResponse getSearchKeywordTourApi(SearchPlaceRequestDto request) {
        TourAPICommonListResponse apiResponseList = tourAPIService.getSearchKeywordTourApi(request.getKeyword(), request.getPage(), request.getPageSize());

        notFoundApiItemException(apiResponseList.getTotalCount());

        return apiResponseList;
    }

    /**
     * 관광 정보 상세 조회
     */
    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto request) {
        // 공통 정보
        DetailCommonItemResponse commonItem = tourAPIService
                .getCommonApi(request.getContentId(), String.valueOf(request.getContentType().getCode()))
                .getDetailCommonItemResponse().get(0);

        // 소개 정보
        DetailIntroResponse introApiResponse = tourAPIService
                .getIntroApi(request.getContentId(), String.valueOf(request.getContentType().getCode()));

        // 반복 정보
        List<DetailInfoItemResponse> infoItems = null;
        if(request.getContentType() == ContentType.TOURIST_SPOT) {
            infoItems = tourAPIService
                    .getInfoApi(request.getContentId(), String.valueOf(request.getContentType().getCode()))
                    .getDetailInfoItemResponses();
        }

        // 이미지 정보
        List<String> images = null;
        List<DetailImageItemResponse> imageItem =  tourAPIService
                .getImageApi(request.getContentId())
                .getDetailImageItemResponses();

        if(ValidatorUtil.isNotEmpty(imageItem)) {
            images = imageItem.stream()
                    .map(image -> image.getOriginImageUrl())
                    .collect(Collectors.toList());
        }

        return handleApiResponse(introApiResponse, infoItems,
                DetailPlaceInformationResponseDto.of(commonItem, images));
    }

    /**
     * content type에 따라 intro item을 설정한다.
     */
    private DetailPlaceInformationResponseDto handleApiResponse(DetailIntroResponse introApiResponse, List<DetailInfoItemResponse> infoItems, DetailPlaceInformationResponseDto response) {
        String useTime = null;
        String tel = null;
        String restDate = null;
        String reservationUrl = null;
        String eventDate = null;
        List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

        if (introApiResponse instanceof SpotIntroResponse) {
            SpotItemResponse spotItem = ((SpotIntroResponse) introApiResponse).getSpotItemResponses().get(0);

            useTime = TextManipulatorUtil.replaceBrWithNewLine(spotItem.getUseTime());
            tel = TextManipulatorUtil.extractTel(spotItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(spotItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(spotItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(spotItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(spotItem.getCheckPet())));

            if(ValidatorUtil.isNotEmpty(infoItems)) {
                boolean flagOfRestroom = false;
                boolean flagOfDisable = false;

                for(DetailInfoItemResponse item : infoItems) {
                    if(item.getInfoName().contains("화장실")) {
                        flagOfRestroom = true;
                    }
                    if(item.getInfoName().contains("장애인 편의시설")) {
                        flagOfDisable = true;
                    }
                }

                facilityInfo.add(FacilityInfo.of(FacilityType.RESTROOM, flagOfRestroom));
                facilityInfo.add(FacilityInfo.of(FacilityType.DISABLED_PERSON_FACILITY, flagOfDisable));
            }
        } else if (introApiResponse instanceof CultureIntroResponse) {
            CultureItemResponse cultureItem = ((CultureIntroResponse) introApiResponse).getCultureItemResponse().get(0);

            useTime = TextManipulatorUtil.replaceBrWithNewLine(cultureItem.getUseTime());
            tel = TextManipulatorUtil.extractTel(cultureItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(cultureItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(cultureItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(cultureItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(cultureItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(cultureItem.getCheckPet())));

        } else if (introApiResponse instanceof EventIntroResponse) {
            EventItemResponse eventItem = ((EventIntroResponse) introApiResponse).getEventItemResponse().get(0);

            useTime = TextManipulatorUtil.prefix("공연 시간 : ", eventItem.getPlayTime());
            tel = TextManipulatorUtil.extractTel(eventItem.getSponsor1Tel());
            reservationUrl = eventItem.getBookingPlace();
            eventDate = TextManipulatorUtil.convertDateRangeFormat(eventItem.getEventStartDate(), eventItem.getEventEndDate());

        } else if (introApiResponse instanceof LeportsIntroResponse) {
            LeportsItemResponse leportsItem = ((LeportsIntroResponse) introApiResponse).getLeportsItemResponse().get(0);

            useTime = TextManipulatorUtil.replaceBrWithNewLine(leportsItem.getUseTime());
            tel = TextManipulatorUtil.extractTel(leportsItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(leportsItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(leportsItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(leportsItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(leportsItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(leportsItem.getCheckPet())));

        } else if (introApiResponse instanceof AccommodationIntroResponse) {
            AccommodationItemResponse accommodationItem = ((AccommodationIntroResponse) introApiResponse).getAccommodationItemResponse().get(0);

            useTime = TextManipulatorUtil.concatenateStrings(
                    accommodationItem.getCheckInTime(),
                    accommodationItem.getCheckOutTime(), "체크인 : ", ", 체크아웃 : ");
            tel = accommodationItem.getInfoCenter();
            reservationUrl = accommodationItem.getReservationUrl();

            facilityInfo.add(FacilityInfo.of(FacilityType.BARBECUE, ValidatorUtil.checkAvailability(accommodationItem.getCheckBarbecue())));
            facilityInfo.add(FacilityInfo.of(FacilityType.BEVERAGE, ValidatorUtil.checkAvailability(accommodationItem.getCheckBeverage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.COOKING, ValidatorUtil.checkAvailability(accommodationItem.getCheckCooking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(accommodationItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PICK_UP_SERVICE, ValidatorUtil.checkAvailability(accommodationItem.getCheckPickup())));
            facilityInfo.add(FacilityInfo.of(FacilityType.SAUNA, ValidatorUtil.checkAvailability(accommodationItem.getCheckSauna())));

        } else if (introApiResponse instanceof ShoppingIntroResponse) {
            ShoppingItemResponse shoppingItem = ((ShoppingIntroResponse) introApiResponse).getShoppingItemResponse().get(0);

            useTime = TextManipulatorUtil.replaceBrWithNewLine(shoppingItem.getOpenTime());
            tel = TextManipulatorUtil.extractTel(shoppingItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(shoppingItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(shoppingItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(shoppingItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(shoppingItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(shoppingItem.getCheckPet())));

        } else if (introApiResponse instanceof RestaurantIntroResponse) {
            RestaurantItemResponse restaurantItem = ((RestaurantIntroResponse) introApiResponse).getRestaurantItemResponse().get(0);

            useTime = TextManipulatorUtil.replaceBrWithNewLine(restaurantItem.getOpenTime());
            tel = TextManipulatorUtil.extractTel(restaurantItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(restaurantItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(restaurantItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(restaurantItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.KIDS_FACILITY, ValidatorUtil.checkAvailability(restaurantItem.getCheckKidsFacility())));
            facilityInfo.add(FacilityInfo.of(FacilityType.SMOKING, ValidatorUtil.checkAvailability(restaurantItem.getCheckSmoking())));
        }

        response.updateItem(useTime, tel, restDate, reservationUrl, eventDate, facilityInfo);

        return response;
    }

    /**
     * 관광 정보 찜하기
     */
    @Transactional
    public void addPlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        Optional<Dib> findDib = dibRepository.findByUserAndContentId(user, contentId);
        if(findDib.isPresent()) { return; }

        // 공통 정보
        DetailCommonItemResponse commonItem = getCommonItem(contentId);

        dibRepository.save(Dib.of(contentId, commonItem.getContentTypeId(), commonItem.getTitle(), commonItem.getAddress(), commonItem.getTel(), commonItem.getThumbnail(), user));
    }

    private DetailCommonItemResponse getCommonItem(Long contentId) {
        return tourAPIService.getCommonApi(contentId, "").getDetailCommonItemResponse().get(0);
    }

    @Transactional
    public void removePlaceDib(Long userId, Long contentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        dibRepository.deleteByUserAndContentId(user, contentId);
    }


}
