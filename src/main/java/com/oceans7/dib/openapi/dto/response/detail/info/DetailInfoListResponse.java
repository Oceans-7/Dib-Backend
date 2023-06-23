package com.oceans7.dib.openapi.dto.response.detail.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class DetailInfoListResponse {
    @JsonProperty("item")
    private List<DetailInfoItemResponse> detailInfoItemResponses;

    @JsonCreator
    public DetailInfoListResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        this.detailInfoItemResponses = Arrays.stream(objectMapper.treeToValue(itemNode, DetailInfoItemResponse[].class)).toList();
    }
}
