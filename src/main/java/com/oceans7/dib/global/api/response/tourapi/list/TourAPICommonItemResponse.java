package com.oceans7.dib.global.api.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourAPICommonItemResponse {
    // 콘텐츠 ID
    @JsonProperty("contentid")
    private Long contentId;

    // 콘텐츠 타입 ID
    @JsonProperty("contenttypeid")
    private int contentTypeId;

    // 제목
    @JsonProperty("title")
    private String title;

    // 전화번호
    @JsonProperty("tel")
    private String tel;

    // 대표이미지 (원본)
    @JsonProperty("firstimage")
    private String firstImage;

    // 대표이미지 (썸네일)
    @JsonProperty("firstimage2")
    private String firstImage2;

    // 주소
    @JsonProperty("addr1")
    private String address1;

    // 상세 주소
    @JsonProperty("addr2")
    private String address2;

    // 저작권 유형
    /**
     * Type1 : 제 1유형(출처표시-권장)
     * Type3 : 제 3유형(제 1유형 + 변경금지)
     */
    @JsonProperty("cpyrhtDivCd")
    private String copyrightDivCd;

    // GPS X좌표
    @JsonProperty("mapx")
    private double mapX;

    // GPS Y좌표
    @JsonProperty("mapy")
    private double mapY;

    // 콘텐츠 최초 등록일
    @JsonProperty("createdtime")
    private String createdTime;

    // 콘텐츠 수정일
    @JsonProperty("modifiedtime")
    private String modifiedTime;

    // 위치 기반 : 중심 좌표로부터 거리 (m)
    @JsonProperty("dist")
    private double dist;

    // 지역 기반 : 우편번호
    @JsonProperty("zipcode")
    private String zipcode;

    // 교과서 속 여행지 여부
    @JsonProperty("booktour")
    private String bookTour;

    // 시군구 코드
    @JsonProperty("sigungucode")
    private String sigunguCode;

    // 지역 코드
    @JsonProperty("areacode")
    private String areaCode;

    // 대분류
    @JsonProperty("cat1")
    private String cat1;

    // 중분류
    @JsonProperty("cat2")
    private String cat2;

    // 소분류
    @JsonProperty("cat3")
    private String cat3;

    public void updateDistance(double dist) {
        this.dist = dist;
    }
}
