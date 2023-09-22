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

    @JsonProperty("content")
    @Schema(description = "내용", example = "제주 스쿠버 다이빙 업체,\n 할인 받고 이용하고 싶다면?")
    private String content;

    @JsonProperty("eventId")
    @Schema(description = "이벤트 ID", example = "0")
    private String eventId;

}
