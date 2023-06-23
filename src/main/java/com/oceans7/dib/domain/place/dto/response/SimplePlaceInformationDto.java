package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.ServiceType;
import com.oceans7.dib.openapi.dto.response.simple.TourAPICommonItemResponse;
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

    @Schema(description = "컨텐츠 아이디, 위치 기반 조회시 확인 가능", example = "126508")
    private Long contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "서비스 분류", example = "LITERATURE_ART_TOUR")
    private ServiceType serviceType;

    @Schema(description = "거리", example = "21.0")
    private float distance;

    @Schema(description = "대표 이미지", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String firstImage;

    @Schema(description = "전화번호", example = "061-532-1000")
    private String tel;

    public SimplePlaceInformationDto (TourAPICommonItemResponse item) {
        this.title = item.getTitle();
        this.address = item.getAddress1() + item.getAddress2();
        this.contentId = item.getContentId();
        this.contentType = getContentTypeByCode(item.getContentTypeId());
        // TODO : 서비스 분류 추가
        this.distance = item.getDist();
        this.firstImage = item.getFirstImage();
        this.tel = item.getTel();
    }


}
