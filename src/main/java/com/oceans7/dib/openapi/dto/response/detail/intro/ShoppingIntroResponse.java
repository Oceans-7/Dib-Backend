package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.ShoppingItemResponse;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ShoppingIntroResponse implements DetailIntroInterface {
    @JsonProperty("item")
    private ShoppingItemResponse shoppingItemResponse;

    @JsonCreator
    public ShoppingIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        List<ShoppingItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, ShoppingItemResponse[].class)).toList();
        this.shoppingItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;

    }
}
