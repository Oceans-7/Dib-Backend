package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionSection {
    @JsonProperty("title")
    private String title;

    @JsonProperty("imageTopContent")
    private String imageTopContent;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("imageBottomContent")
    private String imageBottomContent;

    @JsonProperty("keyword")
    private String keyword;
}
