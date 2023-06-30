package com.oceans7.dib.openapi.dto.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class AreaCodeList {
    @JsonProperty("item")
    private List<AreaCodeItem> areaCodeItems;

    @JsonCreator
    public AreaCodeList(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode itemNode = rootNode.findValue("item");
        this.areaCodeItems = Arrays.stream(objectMapper.treeToValue(itemNode, AreaCodeItem[].class)).toList();
    }

    public String getAreaCodeByName(String name) {
        for (AreaCodeItem areaCode : areaCodeItems) {
            if (areaCode.getName().equals(name)) {
                return areaCode.getCode();
            }
        }
        return null;
    }
}
