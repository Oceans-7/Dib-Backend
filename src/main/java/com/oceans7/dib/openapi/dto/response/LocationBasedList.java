package com.oceans7.dib.openapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class LocationBasedList {
    @JsonProperty("item")
    private List<LocationBasedItem> locationBasedItems;

    public LocationBasedList(@JsonProperty("response")JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        this.locationBasedItems = Arrays.stream(objectMapper.treeToValue(itemNode, LocationBasedItem[].class)).toList();
    }
}
