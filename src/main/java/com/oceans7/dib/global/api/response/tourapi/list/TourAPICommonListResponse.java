package com.oceans7.dib.global.api.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class TourAPICommonListResponse {
    @JsonProperty("item")
    private List<TourAPICommonItemResponse> tourAPICommonItemResponseList;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("numOfRows")
    private int pageSize;

    @JsonProperty("pageNo")
    private int page;

    @JsonCreator
    public TourAPICommonListResponse(@JsonProperty("response")JsonNode rootNode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();


        JsonNode itemNode = rootNode.findValue("item");
        JsonNode totalCountNode = rootNode.findValue("totalCount");
        JsonNode pageSizeNode = rootNode.findValue("numOfRows");
        JsonNode pageNode = rootNode.findValue("pageNo");

        this.totalCount = totalCountNode.asInt();
        this.pageSize = pageSizeNode.asInt();
        this.page = pageNode.asInt();
        this.tourAPICommonItemResponseList = Arrays.stream(objectMapper.treeToValue(itemNode, TourAPICommonItemResponse[].class)).toList();
    }
}