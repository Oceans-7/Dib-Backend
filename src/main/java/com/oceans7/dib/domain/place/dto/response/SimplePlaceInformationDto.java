package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.oceans7.dib.domain.place.ContentType.getContentTypeByCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimplePlaceInformationDto {

    @Schema(description = "장소 이름", example = "해남문학관")
    private String title;

    @Schema(description = "장소 주소", example = "전라남도 해남군 해남읍 해남문화로 1")
    private String address;

    @Schema(description = "컨텐츠 아이디", example = "126508")
    private Long contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "거리", example = "21.0")
    private double distance;

    @Schema(description = "장소 위도", example = "126.9779692")
    private double mapX;

    @Schema(description = "장소 경도", example = "37.566535")
    private double mapY;

    @Schema(description = "대표 이미지", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String firstImageUrl;

    @Schema(description = "전화번호", example = "061-532-1000")
    private String tel;

    public static SimplePlaceInformationDto of(TourAPICommonItemResponse item) {
        SimplePlaceInformationDto simplePlaceInformation = new SimplePlaceInformationDto();

        simplePlaceInformation.title = item.getTitle();
        simplePlaceInformation.address = item.getAddress1() + item.getAddress2();
        simplePlaceInformation.contentId = item.getContentId();
        simplePlaceInformation.contentType = getContentTypeByCode(item.getContentTypeId());
        simplePlaceInformation.distance = item.getDist();
        simplePlaceInformation.mapX = item.getMapX();
        simplePlaceInformation.mapY = item.getMapY();
        simplePlaceInformation.firstImageUrl = item.getFirstImage();
        simplePlaceInformation.tel = item.getTel();

        return simplePlaceInformation;
    }
}
