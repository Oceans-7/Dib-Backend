package com.oceans7.dib.global.api.response.tourapi.detail.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class DetailCommonListResponse {
    @JsonProperty("item")
    private DetailCommonItemResponse detailCommonItemResponse;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonCreator
    public DetailCommonListResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        JsonNode totalCountNode = rootNode.findValue("totalCount");
        this.totalCount = totalCountNode.asInt();
        if (itemNode != null && itemNode.isArray()) {
            // "item"이 배열이라면 배열의 첫 번째 아이템을 사용
            this.detailCommonItemResponse = objectMapper.treeToValue(itemNode.get(0), DetailCommonItemResponse.class);
        } else {
            this.detailCommonItemResponse = objectMapper.treeToValue(itemNode, DetailCommonItemResponse.class);
        }
    }

}
