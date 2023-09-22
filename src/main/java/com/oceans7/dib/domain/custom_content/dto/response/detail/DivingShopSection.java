package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivingShopSection {
    @JsonProperty("title")
    @Schema(description = "제목", example = "서귀포 스쿠포다이빙 체험 업체")
    private String title;

    @JsonProperty("keyword")
    @Schema(description = "제목", example = "스쿠포다이빙 체험 업체")
    private String keyword;

    @JsonProperty("firstShopInfo")
    @Schema(description = "1번 다이빙 업체 업체 정보", implementation = ShopInfo.class)
    private ShopInfo firstShopInfo;

    @JsonProperty("secondShopInfo")
    @Schema(description = "제목", implementation = ShopInfo.class)
    private ShopInfo secondShopInfo;

}
