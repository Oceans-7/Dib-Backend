package com.oceans7.dib.global.api.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.util.CoordinateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
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

    // 대표이미지 (썸네일)
    @JsonProperty("firstimage2")
    private String thumbnail;

    @JsonProperty("addr1")
    private String addr1;

    @JsonProperty("addr2")
    private String addr2;

    // 주소
    private String address;

    // GPS X좌표
    @JsonProperty("mapx")
    private double mapX;

    // GPS Y좌표
    @JsonProperty("mapy")
    private double mapY;

    // 좌표로부터 거리 (m)
    @JsonProperty("dist")
    private double distance;

    // 지역 코드
    @JsonProperty("areacode")
    private String areaCode;

    // 시군구 코드
    @JsonProperty("sigungucode")
    private String sigunguCode;

    // 수정 시각
    @JsonProperty("modifiedtime")
    private String modifiedTime;

    public TourAPICommonItemResponse() {

    }

    public String getAddress() {
        return String.format("%s %s", this.addr1, this.addr2);
    }

    public LocalDateTime parseModifiedTimeToDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(modifiedTime, formatter);
    }

    public void convertDistanceMetersToKilometers() {
        this.distance = CoordinateUtil.convertMetersToKilometers(this.distance);
    }

    public void calculateDistance(double requestX, double requestY) {
        this.distance = CoordinateUtil.calculateDistance(requestX, requestY, this.mapX, this.mapY);
    }
}
