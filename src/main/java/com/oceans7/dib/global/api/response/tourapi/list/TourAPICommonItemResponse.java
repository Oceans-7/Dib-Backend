package com.oceans7.dib.global.api.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.util.CoordinateUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public TourAPICommonItemResponse(TourAPICommonItemResponse item) {
        this.contentId = item.getContentId();
        this.contentTypeId = item.getContentTypeId();
        this.title = item.getTitle();
        this.tel = item.getTel();
        this.thumbnail = item.getThumbnail();
        this.addr1 = item.getAddr1();
        this.addr2 = item.getAddr2();
        this.address = item.getAddress();
        this.mapX = item.getMapX();
        this.mapY = item.getMapY();
        this.distance = item.getDistance();
        this.areaCode = item.getAreaCode();
        this.sigunguCode = item.getSigunguCode();
        this.modifiedTime = item.getModifiedTime();
    }

    public static TourAPICommonItemResponse fromDivingContentItem(TourAPICommonItemResponse item) {
        TourAPICommonItemResponse tourAPICommonItemResponse = new TourAPICommonItemResponse();

        tourAPICommonItemResponse.contentId = item.getContentId();
        tourAPICommonItemResponse.contentTypeId = ContentType.DIVING.getCode();
        tourAPICommonItemResponse.title = item.getTitle();
        tourAPICommonItemResponse.tel = item.getTel();
        tourAPICommonItemResponse.thumbnail = item.getThumbnail();
        tourAPICommonItemResponse.addr1 = item.getAddr1();
        tourAPICommonItemResponse.addr2 = item.getAddr2();
        tourAPICommonItemResponse.address = item.getAddress();
        tourAPICommonItemResponse.mapX = item.getMapX();
        tourAPICommonItemResponse.mapY = item.getMapY();
        tourAPICommonItemResponse.distance = item.getDistance();
        tourAPICommonItemResponse.areaCode = item.getAreaCode();
        tourAPICommonItemResponse.sigunguCode = item.getSigunguCode();
        tourAPICommonItemResponse.modifiedTime = item.getModifiedTime();

        return tourAPICommonItemResponse;
    }

    public String getAddress() {
        return String.format("%s %s", this.addr1, this.addr2);
    }

    public LocalDateTime parseModifiedTimeToDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(modifiedTime, formatter);
    }

    public double convertDistanceByFilter(double requestX, double requestY) {
        return distance == 0.0 ?
                CoordinateUtil.calculateDistance(requestX, requestY, mapX, mapY) :
                CoordinateUtil.convertMetersToKilometers(distance);
    }
}
