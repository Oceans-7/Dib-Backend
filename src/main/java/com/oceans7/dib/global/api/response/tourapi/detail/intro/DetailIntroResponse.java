package com.oceans7.dib.global.api.response.tourapi.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse.*;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailIntroResponse {

    @Getter
    @AllArgsConstructor
    public static class SpotIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<SpotItemResponse> spotItemResponses;

        @JsonCreator
        public SpotIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.spotItemResponses = Arrays.stream(objectMapper.treeToValue(itemNode, SpotItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CultureIntroResponse  extends DetailIntroResponse {
        @JsonProperty("item")
        private List<CultureItemResponse> cultureItemResponse;

        @JsonCreator
        public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.cultureItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, CultureItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class EventIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<EventItemResponse> eventItemResponse;

        @JsonCreator
        public EventIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.eventItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, EventItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class LeportsIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<LeportsItemResponse> leportsItemResponse;

        @JsonCreator
        public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.leportsItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, LeportsItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class AccommodationIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<AccommodationItemResponse> accommodationItemResponse;

        @JsonCreator
        public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.accommodationItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, AccommodationItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ShoppingIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<ShoppingItemResponse> shoppingItemResponse;

        @JsonCreator
        public ShoppingIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.shoppingItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, ShoppingItemResponse[].class)).toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RestaurantIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private List<RestaurantItemResponse> restaurantItemResponse;

        @JsonCreator
        public RestaurantIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            this.restaurantItemResponse = Arrays.stream(objectMapper.treeToValue(itemNode, RestaurantItemResponse[].class)).toList();
        }
    }
}
