package com.oceans7.dib.openapi.dto.response.detail.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class DetailImageListResponse {
    @JsonProperty("item")
    private List<DetailImageItemResponse> detailImageItemResponses;

    @JsonCreator
    public DetailImageListResponse(@JsonProperty("response")JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemRootNode = rootNode.findValue("items");

        if(itemRootNode.has("item")) {
            JsonNode itemNode = rootNode.findValue("item");
            this.detailImageItemResponses = Arrays.stream(objectMapper.treeToValue(itemNode, DetailImageItemResponse[].class)).toList();
        } else {
            this.detailImageItemResponses = new ArrayList<>();
        }
    }
}
