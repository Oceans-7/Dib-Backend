package com.oceans7.dib.domain.place;

public enum FacilityType {
    PARKING("주차장 여부"),
    BABY_CARRIAGE("유모차 대여 여부"),
    CREDIT_CARD("신용 카드 가능 여부"),
    PET("반려 동물 동반 가능 여부"),
    COOKING("취사 여부"),
    PICK_UP_SERVICE("픽업 서비스 여부"),
    BARBECUE("바베큐 가능 여부"),
    BEVERAGE("식음료 여부"),
    SAUNA("사우나 여부"),
    RESTROOM("화장실 여부"),
    KIDS_FACILITY("어린이 놀이방 여부"),
    SMOKING("금연 여부"),
    DISABLED_PERSON_FACILITY("장애인 편의 시설 여부"),
    ;


    private String description;

    FacilityType(String description) {

    }
}
