package com.oceans7.dib.global.api.response.tourapi.detail.intro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.domain.place.dto.FacilityType;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.global.util.TextManipulatorUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 소개 정보
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DetailIntroItemResponse {

    public abstract String extractUseTime();
    public abstract String extractTel();
    public abstract String extractRestDate();
    public abstract String extractReservationUrl();
    public abstract String extractEventDate();

    public abstract List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo();

    @Getter
    @AllArgsConstructor // 테스트 mock data 생성을 위한 생성자
    @NoArgsConstructor
    public static class SpotItemResponse extends DetailIntroItemResponse {
        // 문의 및 안내
        @JsonProperty("infocenter")
        private String infoCenter;

        // 시설 정보
        @JsonProperty("chkbabycarriage")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcard")
        private String checkCreditCard;

        @JsonProperty("chkpet")
        private String checkPet;

        @JsonProperty("parking")
        private String checkParking;

        // 쉬는 날
        @JsonProperty("restdate")
        private String restDate;

        // 이용 시간
        @JsonProperty("usetime")
        private String useTime;

        @Override
        public String extractUseTime() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.useTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.infoCenter);
        }

        @Override
        public String extractRestDate() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.restDate);
        }

        @Override
        public String extractReservationUrl() {
            return "";
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(this.checkBabyCarriage)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(this.checkCreditCard)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(this.checkPet)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }

    }

    @Getter
    public static class CultureItemResponse extends DetailIntroItemResponse {
        // 문의 및 안내
        @JsonProperty("infocenterculture")
        private String infoCenter;

        // 시설 정보
        @JsonProperty("parkingculture")
        private String checkParking;

        @JsonProperty("chkbabycarriageculture")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcardculture")
        private String checkCreditCard;

        @JsonProperty("chkpetculture")
        private String checkPet;

        // 쉬는 날
        @JsonProperty("restdateculture")
        private String restDate;

        // 이용 시간
        @JsonProperty("usetimeculture")
        private String useTime;

        @Override
        public String extractUseTime() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.useTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.infoCenter);
        }

        @Override
        public String extractRestDate() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.restDate);
        }

        @Override
        public String extractReservationUrl() {
            return "";
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(this.checkBabyCarriage)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(this.checkCreditCard)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(this.checkPet)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }
    }

    @Getter
    public static class EventItemResponse extends DetailIntroItemResponse {
        // 예매처
        @JsonProperty("bookingplace")
        private String bookingPlace;

        // 행사 홈페이지
        @JsonProperty("eventhomepage")
        private String eventHomepage;

        // 주최자 연락처
        @JsonProperty("sponsor1tel")
        private String sponsor1Tel;

        // 행사 시작일 - 종료일
        @JsonProperty("eventstartdate")
        private String eventStartDate;

        @JsonProperty("eventenddate")
        private String eventEndDate;

        // 공연 시간
        @JsonProperty("playtime")
        private String playTime;

        @Override
        public String extractUseTime() {
            String prefix = "공연 시간";
            String useTimeFormatOfEvent = "%s : %s";
            return String.format(useTimeFormatOfEvent, prefix, this.playTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.sponsor1Tel);
        }

        @Override
        public String extractRestDate() {
            return "";
        }

        @Override
        public String extractReservationUrl() {
            return this.bookingPlace;
        }

        @Override
        public String extractEventDate() {
            return TextManipulatorUtil.convertDateRangeFormat(this.eventStartDate, this.eventEndDate);
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            return new ArrayList<>();
        }
    }

    @Getter
    public static class LeportsItemResponse extends DetailIntroItemResponse {
        // 시설 정보
        @JsonProperty("chkbabycarriageleports")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcardleports")
        private String checkCreditCard;

        @JsonProperty("chkpetleports")
        private String checkPet;

        @JsonProperty("parkingleports")
        private String checkParking;

        // 문의 및 안내
        @JsonProperty("infocenterleports")
        private String infoCenter;

        // 이용 시간
        @JsonProperty("usetimeleports")
        private String useTime;

        // 쉬는날
        @JsonProperty("restdateleports")
        private String restDate;

        @Override
        public String extractUseTime() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.useTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.infoCenter);
        }

        @Override
        public String extractRestDate() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.restDate);
        }

        @Override
        public String extractReservationUrl() {
            return "";
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(this.checkBabyCarriage)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(this.checkCreditCard)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(this.checkPet)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }
    }

    @Getter
    public static class AccommodationItemResponse extends DetailIntroItemResponse {
        // 시설 정보
        @JsonProperty("chkcooking")
        private String checkCooking;

        @JsonProperty("parkinglodging")
        private String checkParking;

        @JsonProperty("pickup")
        private String checkPickup;

        @JsonProperty("barbecue")
        private String checkBarbecue;

        @JsonProperty("beverage")
        private String checkBeverage;

        @JsonProperty("sauna")
        private String checkSauna;

        // 입퇴실 시간
        @JsonProperty("checkintime")
        private String checkInTime;

        @JsonProperty("checkouttime")
        private String checkOutTime;

        // 문의 및 안내
        @JsonProperty("infocenterlodging")
        private String infoCenter;

        // 예약 안내 홈페이지
        @JsonProperty("reservationurl")
        private String reservationUrl;

        @Override
        public String extractUseTime() {
            String firstPrefix = "체크인";
            String secondPrefix = "체크아웃";
            String useTimeFormatOfAccommodation = "%s : %s, %s : %s";

            return String.format(useTimeFormatOfAccommodation, firstPrefix, this.checkInTime, secondPrefix, this.checkOutTime);
        }

        @Override
        public String extractTel() {
            return this.infoCenter;
        }

        @Override
        public String extractRestDate() {
            return "";
        }

        @Override
        public String extractReservationUrl() {
            return this.reservationUrl;
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BARBECUE, ValidatorUtil.checkAvailability(this.checkBarbecue)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BEVERAGE, ValidatorUtil.checkAvailability(this.checkBeverage)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.COOKING, ValidatorUtil.checkAvailability(this.checkCooking)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PICK_UP_SERVICE, ValidatorUtil.checkAvailability(this.checkPickup)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.SAUNA, ValidatorUtil.checkAvailability(this.checkSauna)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }
    }

    @Getter
    public static class ShoppingItemResponse extends DetailIntroItemResponse {
        // 시설 정보
        @JsonProperty("chkbabycarriageshopping")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcardshopping")
        private String checkCreditCard;

        @JsonProperty("chkpetshopping")
        private String checkPet;

        @JsonProperty("parkingshopping")
        private String checkParking;

        @JsonProperty("restroom")
        private String checkRestroom;

        // 문의 및 안내
        @JsonProperty("infocentershopping")
        private String infoCenter;

        // 영업 시간
        @JsonProperty("opentime")
        private String openTime;

        // 쉬는 날
        @JsonProperty("restdateshopping")
        private String restDate;

        @Override
        public String extractUseTime() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.openTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.infoCenter);
        }

        @Override
        public String extractRestDate() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.restDate);
        }

        @Override
        public String extractReservationUrl() {
            return "";
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(this.checkBabyCarriage)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(this.checkCreditCard)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(this.checkPet)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }
    }

    @Getter
    public static class RestaurantItemResponse extends DetailIntroItemResponse {
        // 시설 정보
        @JsonProperty("chkcreditcardfood")
        private String checkCreditCard;

        @JsonProperty("kidsfacility")
        private String checkKidsFacility;

        @JsonProperty("parkingfood")
        private String checkParking;

        // 금연 / 흡연
        @JsonProperty("smoking")
        private String checkSmoking;

        // 문의 및 안내
        @JsonProperty("infocenterfood")
        private String infoCenter;

        // 영업 시간
        @JsonProperty("opentimefood")
        private String openTime;

        // 쉬는 날
        @JsonProperty("restdatefood")
        private String restDate;

        @Override
        public String extractUseTime() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.openTime);
        }

        @Override
        public String extractTel() {
            return TextManipulatorUtil.extractTel(this.infoCenter);
        }

        @Override
        public String extractRestDate() {
            return TextManipulatorUtil.replaceBrWithNewLine(this.restDate);
        }

        @Override
        public String extractReservationUrl() {
            return "";
        }

        @Override
        public String extractEventDate() {
            return "";
        }

        @Override
        public List<DetailPlaceInformationResponseDto.FacilityInfo> getFacilityAvailabilityInfo() {
            List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();

            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.KIDS_FACILITY, ValidatorUtil.checkAvailability(this.checkKidsFacility)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(this.checkCreditCard)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.SMOKING, ValidatorUtil.checkAvailability(this.checkSmoking)));
            facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PARKING, ValidatorUtil.checkAvailability(this.checkParking)));

            return facilityInfo;
        }
    }
}
