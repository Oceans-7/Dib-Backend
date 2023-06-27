package com.oceans7.dib.domain.place;

import com.oceans7.dib.domain.place.dto.request.GetPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.PlaceResponseDto;
import com.oceans7.dib.domain.place.dto.response.SearchPlaceResponseDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.response.SimplePlaceInformationDto;
import com.oceans7.dib.global.util.TextManipulatorUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import com.oceans7.dib.openapi.dto.response.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.openapi.dto.response.detail.image.DetailImageItemResponse;
import com.oceans7.dib.openapi.dto.response.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.openapi.dto.response.detail.info.DetailInfoListResponse;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroResponse;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.openapi.dto.response.list.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.list.TourAPICommonListResponse;
import com.oceans7.dib.openapi.service.TourAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceans7.dib.domain.place.ContentType.getContentTypeByCode;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final TourAPIService tourAPIService;

    public PlaceResponseDto getPlace(GetPlaceRequestDto request) {
        TourAPICommonListResponse apiResponse = null;
        String contentTypeId = "";
        String arrangeTypeName = "";

        // 필터링 확인
        if(ValidatorUtil.isNotEmpty(request.getContentType())) {
            contentTypeId = String.valueOf(request.getContentType().getCode());
        }
        if(ValidatorUtil.isNotEmpty(request.getArrangeType())) {
            arrangeTypeName = request.getArrangeType().name();
        }

        if(ValidatorUtil.isEmpty(request.getArea())) {
            // 지역 필터 없다면 위치 기반
            apiResponse = tourAPIService.fetchDataFromLocationBasedApi(request.getMapX(), request.getMapY(),
                    contentTypeId, arrangeTypeName,
                    request.getPage(), request.getPageSize());
        } else {
            // 지역 필터 있다면 지역 기반
            // areaCode 조회
            String areaName = request.getArea();
            AreaCodeList list = tourAPIService.fetchDataFromAreaCodeApi("");
            String areaCode = list.getAreaCodeByName(areaName);
            String sigunguCode = "";

            // sigunguCode 조회
            if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
                String sigunguName = request.getSigungu();
                AreaCodeList sigunguList = tourAPIService.fetchDataFromAreaCodeApi(areaCode);
                sigunguCode = sigunguList.getAreaCodeByName(sigunguName);
            }

            apiResponse = tourAPIService.fetchDataFromAreaBasedApi(areaCode, sigunguCode, contentTypeId, arrangeTypeName);
        }

        SimplePlaceInformationDto[] simpleDto = apiResponse.getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: new)
                .toArray(SimplePlaceInformationDto[]::new);

        return new PlaceResponseDto(simpleDto, apiResponse);
    }

    public SearchPlaceResponseDto searchPlace(SearchPlaceRequestDto request) {
        TourAPICommonListResponse apiResponse = null;
        String contentTypeId = "";
        String arrangeTypeName = "";
        String areaCode = "";
        String sigunguCode = "";

        // 필터링 확인
        if(ValidatorUtil.isNotEmpty(request.getContentType())) {
            contentTypeId = String.valueOf(request.getContentType().getCode());
        }
        if(ValidatorUtil.isNotEmpty(request.getArrangeType())) {
            arrangeTypeName = request.getArrangeType().name();
        }
        if(ValidatorUtil.isNotEmpty(request.getArea())) {
            String areaName = request.getArea();
            AreaCodeList list = tourAPIService.fetchDataFromAreaCodeApi("");
            areaCode = list.getAreaCodeByName(areaName);
        }
        if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
            String sigunguName = request.getSigungu();
            AreaCodeList sigunguList = tourAPIService.fetchDataFromAreaCodeApi(areaCode);
            sigunguCode = sigunguList.getAreaCodeByName(sigunguName);
        }

        apiResponse = tourAPIService.fetchDataFromSearchKeywordApi(request.getKeyword(), areaCode, sigunguCode, contentTypeId, arrangeTypeName);

        SimplePlaceInformationDto[] simpleDto = apiResponse.getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: new)
                .toArray(SimplePlaceInformationDto[]::new);

        return new SearchPlaceResponseDto(request.getKeyword(), simpleDto, apiResponse);
    }

    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto request) {
        // 공통 정보
        DetailCommonItemResponse commonItem = tourAPIService
                .fetchDataFromCommonApi(request.getContentId(), String.valueOf(request.getContentType().getCode()))
                .getDetailCommonItemResponse();

        // 이미지 정보
        List<String> images = null;
        List<DetailImageItemResponse> imageItem =  tourAPIService.fetchImageDataFromApi(request.getContentId())
                .getDetailImageItemResponses();

        if(ValidatorUtil.isNotEmpty(imageItem)) {
            images = imageItem.stream()
                    .map(image -> image.getOriginImageUrl())
                    .collect(Collectors.toList());
        }
        // 소개 정보
        DetailIntroResponse introApiResponse = tourAPIService.fetchDataFromIntroApi(request.getContentId(), String.valueOf(request.getContentType().getCode()));

        // 시설 정보
        List<FacilityType> availableFacilities = new ArrayList<>();

        if(request.getContentType() == ContentType.TOURIST_SPOT) {
            SpotItemResponse introItem = ((SpotIntroResponse) introApiResponse).getSpotItemResponses();

            if(ValidatorUtil.checkAvailability(introItem.getCheckBabyCarriage()) == true) availableFacilities.add(FacilityType.BABY_CARRIAGE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckCreditCard()) == true) availableFacilities.add(FacilityType.CREDIT_CARD);
            if(ValidatorUtil.checkAvailability(introItem.getCheckPet()) == true) availableFacilities.add(FacilityType.PET);

            // 반복 정보
            DetailInfoListResponse infoApiResponse = tourAPIService.fetchDataFromInfoApi(request.getContentId(), String.valueOf(request.getContentType().getCode()));
            checkFacilityTypeAvailability(infoApiResponse, availableFacilities);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.replaceBrWithNewLine(introItem.getUseTime()),
                    TextManipulatorUtil.extractTel(introItem.getInfoCenter()), null, TextManipulatorUtil.replaceBrWithNewLine(introItem.getRestDate()), null,
                    availableFacilities, images);

        } else if(request.getContentType() == ContentType.CULTURAL_SITE) {
            CultureItemResponse introItem = ((CultureIntroResponse) introApiResponse).getCultureItemResponse();

            if(ValidatorUtil.checkAvailability(introItem.getCheckBabyCarriage()) == true) availableFacilities.add(FacilityType.BABY_CARRIAGE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckCreditCard()) == true) availableFacilities.add(FacilityType.CREDIT_CARD);
            if(ValidatorUtil.checkAvailability(introItem.getCheckParking()) == true) availableFacilities.add(FacilityType.PARKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckPet()) == true) availableFacilities.add(FacilityType.PET);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.replaceBrWithNewLine(introItem.getUseTime()),
                    TextManipulatorUtil.extractTel(introItem.getInfoCenter()), null, TextManipulatorUtil.replaceBrWithNewLine(introItem.getRestDate()), null,
                    availableFacilities, images);

        } else if(request.getContentType() == ContentType.EVENT) {
            EventItemResponse introItem = ((EventIntroResponse) introApiResponse).getEventItemResponse();

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()),
                    introItem.getPlayTime(), TextManipulatorUtil.extractTel(introItem.getSponsor1Tel()), introItem.getBookingPlace(), null,
                    TextManipulatorUtil.convertDateRangeFormat(introItem.getEventStartDate(), introItem.getEventEndDate()), availableFacilities, images);
        }
        else if(request.getContentType() == ContentType.LEPORTS) {
            LeportsItemResponse introItem  = ((LeportsIntroResponse) introApiResponse).getLeportsItemResponse();

            if(ValidatorUtil.checkAvailability(introItem.getCheckBabyCarriage()) == true) availableFacilities.add(FacilityType.BABY_CARRIAGE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckCreditCard()) == true) availableFacilities.add(FacilityType.CREDIT_CARD);
            if(ValidatorUtil.checkAvailability(introItem.getCheckParking()) == true) availableFacilities.add(FacilityType.PARKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckPet()) == true) availableFacilities.add(FacilityType.PET);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.replaceBrWithNewLine(introItem.getUseTime()),
                    TextManipulatorUtil.extractTel(introItem.getInfoCenter()), null, TextManipulatorUtil.replaceBrWithNewLine(introItem.getRestDate()), null,
                    availableFacilities, images);

        } else if(request.getContentType() == ContentType.ACCOMMODATION) {
            AccommodationItemResponse introItem = ((AccommodationIntroResponse) introApiResponse).getAccommodationItemResponse();

            if(ValidatorUtil.checkAvailability(introItem.getCheckBarbecue()) == true) availableFacilities.add(FacilityType.BARBECUE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckBarbecue()) == true) availableFacilities.add(FacilityType.BEVERAGE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckCooking()) == true) availableFacilities.add(FacilityType.COOKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckParking()) == true) availableFacilities.add(FacilityType.PARKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckPickup()) == true) availableFacilities.add(FacilityType.PICK_UP_SERVICE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckSauna()) == true) availableFacilities.add(FacilityType.SAUNA);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.concatenateStrings(introItem.getCheckInTime(),
                    introItem.getCheckOutTime(), "체크인 : ", ", 체크아웃 : "),
                    introItem.getInfoCenter(), introItem.getReservationUrl(), null, null,
                    availableFacilities, images);

        } else if(request.getContentType() == ContentType.SHOPPING) {
            ShoppingItemResponse introItem = ((ShoppingIntroResponse) introApiResponse).getShoppingItemResponse();

            if(ValidatorUtil.checkAvailability(introItem.getCheckBabyCarriage()) == true) availableFacilities.add(FacilityType.BABY_CARRIAGE);
            if(ValidatorUtil.checkAvailability(introItem.getCheckCreditCard()) == true) availableFacilities.add(FacilityType.CREDIT_CARD);
            if(ValidatorUtil.checkAvailability(introItem.getCheckParking()) == true) availableFacilities.add(FacilityType.PARKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckPet()) == true) availableFacilities.add(FacilityType.PET);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.replaceBrWithNewLine(introItem.getOpenTime()),
                    introItem.getInfoCenter(), null, TextManipulatorUtil.replaceBrWithNewLine(introItem.getRestDate()), null,
                    availableFacilities, images);

        } else if(request.getContentType() == ContentType.RESTAURANT) {
            RestaurantItemResponse introItem = ((RestaurantIntroResponse) introApiResponse).getRestaurantItemResponse();

            if(ValidatorUtil.checkAvailability(introItem.getCheckCreditCard()) == true) availableFacilities.add(FacilityType.CREDIT_CARD);
            if(ValidatorUtil.checkAvailability(introItem.getCheckParking()) == true) availableFacilities.add(FacilityType.PARKING);
            if(ValidatorUtil.checkAvailability(introItem.getCheckKidsFacility()) == true) availableFacilities.add(FacilityType.KIDS_FACILITY);
            if(ValidatorUtil.checkAvailability(introItem.getCheckSmoking()) == true) availableFacilities.add(FacilityType.SMOKING);

            return new DetailPlaceInformationResponseDto(commonItem.getContentId(), getContentTypeByCode(commonItem.getContentTypeId()),
                    commonItem.getTitle(), TextManipulatorUtil.concatenateStrings(commonItem.getAddress1(), commonItem.getAddress2(), " "),
                    commonItem.getMapX(), commonItem.getMapY(), TextManipulatorUtil.replaceBrWithNewLine(commonItem.getOverview()),
                    TextManipulatorUtil.extractFirstUrl(commonItem.getHomepageUrl()), TextManipulatorUtil.replaceBrWithNewLine(introItem.getOpenTime()),
                    introItem.getInfoCenter(), null, TextManipulatorUtil.replaceBrWithNewLine(introItem.getRestDate()), null,
                    availableFacilities, images);
        }
        return null;
    }


    private void checkFacilityTypeAvailability (DetailInfoListResponse infoListResponse, List<FacilityType> availableFacilities) {
        for(DetailInfoItemResponse item : infoListResponse.getDetailInfoItemResponses()) {
            if(item.getInfoName().contains("화장실")) {
                availableFacilities.add(FacilityType.RESTROOM);
            }
            if(item.getInfoName().contains("장애인 편의시설")) {
                availableFacilities.add(FacilityType.DISABLED_PERSON_FACILITY);
            }
        }
    }

}
