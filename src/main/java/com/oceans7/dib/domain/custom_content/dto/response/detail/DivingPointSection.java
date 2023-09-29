package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivingPointSection {
    @JsonProperty("title")
    @Schema(description = "제목", example = "세계 최고의 산호 정원")
    private String title;

    @JsonProperty("imageUrl")
    @Schema(description = "이미지 URL", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String imageUrl;

    @JsonProperty("subTitle")
    @Schema(description = "소제목", example = "서귀포 문섬, 범섬, 섶섬")
    private String subTitle;

    @JsonProperty("content")
    @Schema(description = "제목", example = "문섬, 범섬, 섶섬은 제주도 다이빙의 메카로 손꼽히는 서귀포의 3대 섬입니다.")
    private String content;

    @JsonProperty("keyword")
    @Schema(description = "제목", example = "산호 정원")
    private String keyword;
}
