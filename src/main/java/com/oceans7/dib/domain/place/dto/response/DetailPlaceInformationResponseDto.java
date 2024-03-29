package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.dto.FacilityType;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailPlaceInformationResponseDto {

    @Schema(description = "컨텐츠 아이디, 위치 기반 조회시 확인 가능", example = "126508")
    private Long contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "장소 이름", example = "해남문학관")
    private String title;

    @Schema(description = "장소 주소", example = "전라남도 해남군 해남읍 해남문학로 1")
    private String address;

    @Schema(description = "장소 위도", example = "126.9779692")
    private double mapX;

    @Schema(description = "장소 경도", example = "37.566535")
    private double mapY;

    @Schema(description = "소개")
    private String introduce;

    @Schema(description = "홈페이지", example = "http://www.hnmuseum.or.kr/")
    private String homepageUrl;

    @Schema(description = "이용 시간", example = "09:00~18:00")
    private String useTime;

    @Schema(description = "전화번호", example = "061-532-1000")
    private String tel;

    @Schema(description = "예약 홈페이지", example = "http://www.hnmuseum.or.kr/")
    private String reservationUrl;

    @Schema(description = "쉬는 날", example = "매주 월요일 휴무")
    private String restDate;

    @Schema(description = "축제 기간", example = "2023/07/01~2023/08/09")
    private String eventDate;

    @Schema(description = "찜 여부", example = "true")
    private boolean isDib;

    @ArraySchema(schema = @Schema(description = "시설 이용 정보", implementation = FacilityInfo.class))
    private List<FacilityInfo> facilityInfo;

    @ArraySchema(schema = @Schema(description = "이미지 리스트", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg"))
    private List<String> images;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FacilityInfo {
        @Schema(description = "시설 정보 타입", example = "PARKING")
        private FacilityType type;

        @Schema(description = "시설 이용 가능 여부", example = "true")
        private boolean availability;

        public static FacilityInfo of(FacilityType type, boolean availability) {
            FacilityInfo facilityInfo = new FacilityInfo();
            facilityInfo.type = type;
            facilityInfo.availability = availability;
            return facilityInfo;
        }
    }
    public static DetailPlaceInformationResponseDto of(Long contentId, ContentType contentType, String title, String address,
                                                       double mapX, double mapY, String introduce, String homepageUrl,
                                                       String useTime, String tel, String restDate, String reservationUrl, String eventDate, boolean isDib,
                                                       List<FacilityInfo> facilityInfoList, List<String> imageUrlList) {
        DetailPlaceInformationResponseDto detailPlaceInformation = new DetailPlaceInformationResponseDto();

        detailPlaceInformation.contentId = contentId;
        detailPlaceInformation.contentType = contentType;
        detailPlaceInformation.title = title;
        detailPlaceInformation.address = address;
        detailPlaceInformation.mapX = mapX;
        detailPlaceInformation.mapY = mapY;
        detailPlaceInformation.introduce = introduce;
        detailPlaceInformation.homepageUrl = homepageUrl;
        detailPlaceInformation.useTime = useTime;
        detailPlaceInformation.tel = tel;
        detailPlaceInformation.restDate = restDate;
        detailPlaceInformation.reservationUrl = reservationUrl;
        detailPlaceInformation.eventDate = eventDate;
        detailPlaceInformation.facilityInfo = facilityInfoList;
        detailPlaceInformation.images = imageUrlList;
        detailPlaceInformation.isDib = isDib;

        return detailPlaceInformation;
    }
}
