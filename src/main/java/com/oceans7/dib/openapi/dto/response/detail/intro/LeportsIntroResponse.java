package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.LeportsItemResponse;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class LeportsIntroResponse implements DetailIntroInterface {
    @JsonProperty("item")
    private LeportsItemResponse leportsItemResponse;

    @JsonCreator
    public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<LeportsItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, LeportsItemResponse[].class)).toList();
        this.leportsItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
    }
}
