package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.TourCourseItemResponse;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class TourCourseIntroResponse implements DetailIntroInterface {
    @JsonProperty("item")
    private TourCourseItemResponse tourCourseItemResponse;

    @JsonCreator
    public TourCourseIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<TourCourseItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, TourCourseItemResponse[].class)).toList();
        this.tourCourseItemResponse =  tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
    }
}
