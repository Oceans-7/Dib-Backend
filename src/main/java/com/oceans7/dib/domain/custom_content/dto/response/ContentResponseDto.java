package com.oceans7.dib.domain.custom_content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentResponseDto {
    @Schema(description = "자체 컨텐츠 아이디", example = "0")
    private Long customContentId;

    @Schema(description = "대표 이미지", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String firstImageUrl;

    @Schema(description = "제목", example = "제주 서귀포")
    private String title;

    @Schema(description = "서브 제목", example = "다이빙 명소 및 관광지 파헤치기")
    private String subTitle;

    public static ContentResponseDto of(Long customContentId, String firstImage, String title, String subTitle) {
        ContentResponseDto contentResponseDto = new ContentResponseDto();

        contentResponseDto.customContentId = customContentId;
        contentResponseDto.firstImageUrl = firstImage;
        contentResponseDto.title = title;
        contentResponseDto.subTitle = subTitle;

        return contentResponseDto;
    }
}
