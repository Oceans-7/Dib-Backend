package com.oceans7.dib.domain.place;

import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto.FacilityInfo;
import com.oceans7.dib.domain.place.dto.response.PlaceResponseDto;
import com.oceans7.dib.domain.place.dto.response.SearchPlaceResponseDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.response.SimplePlaceInformationDto;
import com.oceans7.dib.global.util.TextManipulatorUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.openapi.dto.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.openapi.service.DataGoKrAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlaceService {

    private final DataGoKrAPIService tourAPIService;

    public PlaceResponseDto getPlace(GetPlaceRequestDto request) {
        TourAPICommonListResponse apiResponse;
        String contentTypeId = "";
        String arrangeTypeName = "";

        // 필터링 확인
        if(ValidatorUtil.isNotEmpty(request.getContentType())) {
            contentTypeId = String.valueOf(request.getContentType().getCode());
        }
        if(ValidatorUtil.isNotEmpty(request.getArrangeType())) {
            arrangeTypeName = request.getArrangeType().getCode();
        }

        if(ValidatorUtil.isEmpty(request.getArea())) {
            // 지역 필터 없다면 위치 기반
            apiResponse = tourAPIService.getLocationBasedTourApi(request.getMapX(), request.getMapY(),
                    request.getPage(), request.getPageSize(), contentTypeId, arrangeTypeName);
        } else {
            // 지역 필터 있다면 지역 기반
            // areaCode 조회
            String areaName = request.getArea();
            AreaCodeList list = tourAPIService.getAreaCodeApi("");
            String areaCode = list.getAreaCodeByName(areaName);
            String sigunguCode = "";

            // sigunguCode 조회
            if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
                String sigunguName = request.getSigungu();
                AreaCodeList sigunguList = tourAPIService.getAreaCodeApi(areaCode);
                sigunguCode = sigunguList.getAreaCodeByName(sigunguName);
            }

            apiResponse = tourAPIService.getAreaBasedTourApi(areaCode, sigunguCode,
                    request.getPage(), request.getPageSize(), contentTypeId, arrangeTypeName);
        }

        SimplePlaceInformationDto[] simpleDto = apiResponse.getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .toArray(SimplePlaceInformationDto[]::new);

        return PlaceResponseDto.of(simpleDto, apiResponse, request.getArrangeType());
    }

    public SearchPlaceResponseDto searchPlace(SearchPlaceRequestDto request) {
        TourAPICommonListResponse apiResponse;
        String contentTypeId = "";
        String arrangeTypeName = "";
        String areaCode = "";
        String sigunguCode = "";

        // TODO : 키워드가 지역명일 때는 지역 기반 api를 호출하게 할 것임.


        // 필터링 확인
        if(ValidatorUtil.isNotEmpty(request.getContentType())) {
            contentTypeId = String.valueOf(request.getContentType().getCode());
        }

        if(ValidatorUtil.isNotEmpty(request.getArrangeType())) {
            arrangeTypeName = request.getArrangeType().getCode();
        }

        if(ValidatorUtil.isNotEmpty(request.getArea())) {
            areaCode = getAreaCode(request.getArea(), "");
        }

        if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
            sigunguCode = getAreaCode(request.getSigungu(), areaCode);
        }

        apiResponse = tourAPIService.getSearchKeywordTourApi(request.getKeyword(), request.getPage(), request.getPageSize(),
                areaCode, sigunguCode, contentTypeId, arrangeTypeName);

        SimplePlaceInformationDto[] simpleDto = apiResponse.getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .toArray(SimplePlaceInformationDto[]::new);

        return SearchPlaceResponseDto.of(request.getKeyword(), simpleDto, apiResponse, request.getArrangeType());
    }

    private String getAreaCode(String areaName, String areaCode) {
        AreaCodeList list = tourAPIService.getAreaCodeApi(areaCode);
        areaCode = list.getAreaCodeByName(areaName);
        return areaCode;
    }

    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto request) {
        // 공통 정보
        DetailCommonItemResponse commonItem = tourAPIService
                .getCommonApi(request.getContentId(), String.valueOf(request.getContentType().getCode()))
                .getDetailCommonItemResponse();

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
     * @param introApiResponse
     * @param infoItems
     * @param response
     * @return
     */
    private DetailPlaceInformationResponseDto handleApiResponse(DetailIntroResponse introApiResponse, List<DetailInfoItemResponse> infoItems, DetailPlaceInformationResponseDto response) {
        String useTime = null;
        String tel = null;
        String restDate = null;
        String reservationUrl = null;
        String eventDate = null;
        List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

        if (introApiResponse instanceof SpotIntroResponse) {
            SpotItemResponse spotItem = ((SpotIntroResponse) introApiResponse).getSpotItemResponses();

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
            CultureItemResponse cultureItem = ((CultureIntroResponse) introApiResponse).getCultureItemResponse();

            useTime = TextManipulatorUtil.replaceBrWithNewLine(cultureItem.getUseTime());
            tel = TextManipulatorUtil.extractTel(cultureItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(cultureItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(cultureItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(cultureItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(cultureItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(cultureItem.getCheckPet())));

        } else if (introApiResponse instanceof EventIntroResponse) {
            EventItemResponse eventItem = ((EventIntroResponse) introApiResponse).getEventItemResponse();

            useTime = TextManipulatorUtil.prefix("공연 시간 : ", eventItem.getPlayTime());
            tel = TextManipulatorUtil.extractTel(eventItem.getSponsor1Tel());
            reservationUrl = eventItem.getBookingPlace();
            eventDate = TextManipulatorUtil.convertDateRangeFormat(eventItem.getEventStartDate(), eventItem.getEventEndDate());

        } else if (introApiResponse instanceof LeportsIntroResponse) {
            LeportsItemResponse leportsItem = ((LeportsIntroResponse) introApiResponse).getLeportsItemResponse();

            useTime = TextManipulatorUtil.replaceBrWithNewLine(leportsItem.getUseTime());
            tel = TextManipulatorUtil.extractTel(leportsItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(leportsItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(leportsItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(leportsItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(leportsItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(leportsItem.getCheckPet())));

        } else if (introApiResponse instanceof AccommodationIntroResponse) {
            AccommodationItemResponse accommodationItem = ((AccommodationIntroResponse) introApiResponse).getAccommodationItemResponse();

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
            ShoppingItemResponse shoppingItem = ((ShoppingIntroResponse) introApiResponse).getShoppingItemResponse();

            useTime = TextManipulatorUtil.replaceBrWithNewLine(shoppingItem.getOpenTime());
            tel = TextManipulatorUtil.extractTel(shoppingItem.getInfoCenter());
            restDate = TextManipulatorUtil.replaceBrWithNewLine(shoppingItem.getRestDate());

            facilityInfo.add(FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(shoppingItem.getCheckBabyCarriage())));
            facilityInfo.add(FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(shoppingItem.getCheckCreditCard())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(shoppingItem.getCheckParking())));
            facilityInfo.add(FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(shoppingItem.getCheckPet())));

        } else if (introApiResponse instanceof RestaurantIntroResponse) {
            RestaurantItemResponse restaurantItem = ((RestaurantIntroResponse) introApiResponse).getRestaurantItemResponse();

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

}
