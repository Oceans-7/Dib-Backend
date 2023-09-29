package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {
    @JsonProperty("title")
    @Schema(description = "제목", example = "보래드 베이커스")
    private String title;

    @JsonProperty("imageUrl")
    @Schema(description = "이미지 URL", example = "http://tong.visitkorea.or.kr/cms/resource/65/2820265_image2_1.jpg")
    private String imageUrl;

    @JsonProperty("content")
    @Schema(description = "내용", example = "풍경과 음식을 모두 사로잡은 인스타 감성 카페 보래드 베이커스는 다양하고 맛있는 베이커리류뿐만 아니라 피자와 맥주 등 점심/저녁으로 먹기 좋은 메뉴도 판매하고 있습니다.")
    private String content;

    @JsonProperty("contentId")
    @Schema(description = "콘텐츠 ID", example = "2839730")
    private Long contentId;
}
