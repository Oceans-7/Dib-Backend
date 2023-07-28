package com.oceans7.dib.global.api.response.tourapi.detail.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailCommonListResponse {
    @JsonProperty("item")
    private List<DetailCommonItemResponse> detailCommonItemResponse;

    @JsonCreator
    public DetailCommonListResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        this.detailCommonItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, DetailCommonItemResponse[].class)).toList();
    }

}
