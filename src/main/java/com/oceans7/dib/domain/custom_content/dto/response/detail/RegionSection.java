package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionSection {
    @JsonProperty("title")
    @Schema(description = "제목", example = "제주 서귀포는?")
    private String title;

    @JsonProperty("imageTopContent")
    @Schema(description = "이미지 상단 내용", example = "국내 여행지로 가는 제주도는 국내 최고의 다이빙 명소로 알려져 있습니다.")
    private String imageTopContent;

    @JsonProperty("imageUrl")
    @Schema(description = "이미지 URL", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String imageUrl;

    @JsonProperty("imageBottomContent")
    @Schema(description = "이미지 하단 내용", example = "이 다이빙 명소들을 어루고 있는 제주 서귀포를 중심으로 다이빙 명소 및 주변 관광지를 추천해드리고자 합니다.")
    private String imageBottomContent;

    @JsonProperty("keyword")
    @Schema(description = "하이라이트 키워드", example = "다이빙 명소 및 주변 관광지를 추천")
    private String keyword;
}
