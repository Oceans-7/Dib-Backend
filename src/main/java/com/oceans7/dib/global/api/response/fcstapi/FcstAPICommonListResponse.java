package com.oceans7.dib.global.api.response.fcstapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcstAPICommonListResponse {
    @JsonProperty("item")
    private List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList;

    @JsonCreator
    public FcstAPICommonListResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        this.fcstAPICommonItemResponseList = Arrays.stream(objectMapper.treeToValue(itemNode, FcstAPICommonItemResponse[].class)).toList();
    }
}
