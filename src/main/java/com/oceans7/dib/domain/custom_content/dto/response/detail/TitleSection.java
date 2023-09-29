package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TitleSection {
    @JsonProperty("thumbnailUrl")
    @Schema(description = "썸네일 URL", example = "http://tong.visitkorea.or.kr/cms/resource/06/2510606_image2_1.jpg")
    private String thumbnailUrl;

    @JsonProperty("title")
    @Schema(description = "제목", example = "제주 서귀포 명소,\n주변 관광지 파헤치기!")
    private String title;

    @JsonProperty("subTitle")
    @Schema(description = "소제목", example = "다이빙 명소와 제주도 맛집, 주변 관광지까지!\n다양한 정보를 확인하세요~")
    private String subTitle;

    @JsonProperty("region")
    @Schema(description = "지역명", example = "제주 서귀포")
    private String region;
}
