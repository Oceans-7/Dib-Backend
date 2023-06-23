package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.AccommodationItemResponse;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class AccommodationIntroResponse implements DetailIntroInterface {
    @JsonProperty("item")
    private AccommodationItemResponse accommodationItemResponse;

    @JsonCreator
    public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<AccommodationItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, AccommodationItemResponse[].class)).toList();
        this.accommodationItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
    }
}
