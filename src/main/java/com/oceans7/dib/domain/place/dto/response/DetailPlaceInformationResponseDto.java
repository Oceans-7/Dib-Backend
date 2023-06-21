package com.oceans7.dib.domain.place.dto.response;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.FacilityType;
import com.oceans7.dib.domain.place.ServiceType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailPlaceInformationResponseDto {

    @Schema(description = "장소 이름", example = "해남문학관")
    private String title;

    @Schema(description = "장소 주소", example = "전라남도 해남군 해남읍 해남문학로 1")
    private String address;

    @Schema(description = "컨텐츠 아이디, 위치 기반 조회시 확인 가능", example = "126508")
    private String contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

    @Schema(description = "서비스 분류", example = "LITERATURE_ART_TOUR")
    private ServiceType serviceType;

    @Schema(description = "거리", example = "21.0")
    private float distance;

    @Schema(description = "대표 이미지", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String firstImage;

    @Schema(description = "영업시작시간", example = "09:00:00")
    private String useStartTime;

    @Schema(description = "영업종료시간", example = "18:00:00")
    private String useEndTime;

    @Schema(description = "소개")
    private String introduce;

    @Schema(description = "전화번호", example = "061-532-1000")
    private String phoneNumber;

    @Schema(description = "홈페이지", example = "http://www.hnmuseum.or.kr/")
    private String homepageUrl;

    @ArraySchema(schema = @Schema(description = "시설", example = "TOILET", implementation = FacilityType.class))
    private FacilityType[] availableFacilities;
}
