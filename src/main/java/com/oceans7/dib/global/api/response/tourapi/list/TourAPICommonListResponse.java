package com.oceans7.dib.global.api.response.tourapi.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class TourAPICommonListResponse {
    @JsonProperty("item")
    private List<TourAPICommonItemResponse> tourAPICommonItemResponseList;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("pageNo")
    private int page;

    @JsonProperty("numOfRows")
    private int pageSize;

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
        this.tourAPICommonItemResponseList = new ArrayList<>(Arrays.asList(objectMapper.treeToValue(itemNode, TourAPICommonItemResponse[].class)));
    }

    @Builder
    public TourAPICommonListResponse(List<TourAPICommonItemResponse> tourAPICommonItemResponseList, int totalCount, int page, int pageSize)  {
        this.tourAPICommonItemResponseList = tourAPICommonItemResponseList;
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
    }
}
