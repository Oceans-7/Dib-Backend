package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponSection {
    @JsonProperty("title")
    @Schema(description = "제목", example = "할인 쿠폰")
    private String title;

    @JsonProperty("couponImageUrl")
    @Schema(description = "쿠폰 이미지 URL", example = "https://dib-file-bucket.s3.ap-northeast-2.amazonaws.com/images/f447306b-2429-447c-889a-a9090ce85a2b02_%E1%84%8F%E1%85%AE%E1%84%91%E1%85%A9%E1%86%AB+%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%91%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8C%E1%85%B5_%E1%84%8F%E1%85%AE%E1%84%91%E1%85%A9%E1%86%AB.png")
    private String couponImageUrl;

    @JsonProperty("eventId")
    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId;

}
