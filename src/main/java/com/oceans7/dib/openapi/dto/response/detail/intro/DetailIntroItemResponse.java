package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 소개 정보
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailIntroItemResponse {
    // 기본 응답
    @JsonProperty("contentid")
    private Long contentId;

    @JsonProperty("contenttypeid")
    private int contentTypeId;

    // 관광지
    public class SpotItemResponse extends DetailIntroItemResponse {
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

        // 이용 기간
        @JsonProperty("useseason")
        private String useSeason;

        // 이용 시간
        @JsonProperty("usetime")
        private String useTime;
    }

    public class EventItemResponse extends DetailIntroItemResponse {
        // 문의 및 안내
        @JsonProperty("infocenterculture")
        private String infoCenter;

        // 시설 정보
        @JsonProperty("parkingculture")
        private String parkingCulture;

        @JsonProperty("chkbabycarriageculture")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcardculture")
        private String checkCreditCard;

        @JsonProperty("chkpetculture")
        private String checkPet;

        // 이용 시간
        @JsonProperty("usetimeculture")
        private String useTime;

        // 예매처
        @JsonProperty("bookingplace")
        private String bookingPlace;

        // 행사 홈페이지
        @JsonProperty("eventhomepage")
        private String eventHomepage;

        // 주최자 연락처
        @JsonProperty("sponsor1tel")
        private String sponsor1Tel;
    }

    public class TourCourseItemResponse extends DetailIntroItemResponse {
        // 문의 및 안내
        @JsonProperty("infocentertourcourse")
        private String infoCenter;

        // TODO : 이용시간과 관련하여 필드 추가 필요
    }

    public class LeportsItemResponse extends DetailIntroItemResponse {
        // 시설 정보
        @JsonProperty("chkbabycarriageleports")
        private String checkBabyCarriage;

        @JsonProperty("chkcreditcardleports")
        private String checkCreditCard;

        @JsonProperty("chkpetculture")
        private String checkPet;

        @JsonProperty("parkingleports")
        private String checkParking;

        // 문의 및 안내
        @JsonProperty("infocenterleports")
        private String infoCenter;

        // 이용 시간
        @JsonProperty("usetimeleports")
        private String useTime;
    }

    public class AccommodationItemResponse extends DetailIntroItemResponse {
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

        // 문의 및 안내
        @JsonProperty("infocenterlodging")
        private String infoCenter;

        // 예약 안내 홈페이지
        @JsonProperty("reservationurl")
        private String reservationUrl;
    }

    public class ShoppingItemResponse extends DetailIntroItemResponse {
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
    }

    public class RestaurantItemResponse extends DetailIntroItemResponse {
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
    }
}
