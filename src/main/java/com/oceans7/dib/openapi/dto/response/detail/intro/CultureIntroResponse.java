package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.CultureItemResponse;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CultureIntroResponse implements DetailIntroInterface {
    @JsonProperty("item")
    private CultureItemResponse cultureItemResponse;

    @JsonCreator
    public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<CultureItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, CultureItemResponse[].class)).toList();
        this.cultureItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
    }
}
