package com.oceans7.dib.domain.custom_content.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantSection {
    @JsonProperty("title")
    @Schema(description = "제목", example = "열정 넘치는 다이빙 후 즐기는\n서귀포 맛집 탐방")
    private String title;

    @JsonProperty("keyword")
    @Schema(description = "하이라이트 키워드", example = "서귀포 맛집 탐방")
    private String keyword;

    @JsonProperty("restaurantList")
    @ArraySchema(schema = @Schema(description = "식당/카페 정보", implementation = Restaurant.class))
    private List<Restaurant> restaurantList;
}
