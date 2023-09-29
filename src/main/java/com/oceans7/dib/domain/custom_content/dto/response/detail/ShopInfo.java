package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopInfo {
    @JsonProperty("title")
    @Schema(description = "제목", example = "조이다이브 스쿠버다이빙")
    private String title;

    @JsonProperty("imageUrl")
    @Schema(description = "이미지 URL", example = "http://tong.visitkorea.or.kr/cms/resource/70/2755170_image2_1.jpg")
    private String imageUrl;

    @JsonProperty("contentId")
    @Schema(description = "콘텐츠 ID", example = "2755013")
    private Long contentId;
}
