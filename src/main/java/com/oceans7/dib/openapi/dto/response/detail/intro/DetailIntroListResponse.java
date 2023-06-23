package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class DetailIntroListResponse {
    @JsonProperty("item")
    private DetailIntroItemResponse detailIntroItemResponse;

    @JsonCreator
    public DetailIntroListResponse(@JsonProperty("response")JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<DetailIntroItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse[].class)).toList();
        this.detailIntroItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
    }
}
