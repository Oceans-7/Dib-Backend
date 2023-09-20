package com.oceans7.dib.domain.event.entity;

public enum CouponType {
    ACCOMMODATION("숙박"),
    SCUBA_DIVING("스쿠버 다이빙"),
    ;

    private String keyword;

    CouponType(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
