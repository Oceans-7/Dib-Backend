package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivingPointSection {
    @JsonProperty("title")
    private String title;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("subTitle")
    private String subTitle;

    @JsonProperty("content")
    private String content;

    @JsonProperty("keyword")
    private String keyword;
}
