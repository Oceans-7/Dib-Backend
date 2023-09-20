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
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DetailIntroResponse {

    <T> T findFirstItemResponse(JsonNode rootNode, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode itemNode = rootNode.findValue("item");

        if (itemNode != null && itemNode.isArray()) {
            return objectMapper.treeToValue(itemNode.get(0), valueType);
        } else {
            return objectMapper.treeToValue(itemNode, valueType);
        }}

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpotIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private SpotItemResponse spotItemResponse;

        @JsonCreator
        public SpotIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.spotItemResponse = findFirstItemResponse(rootNode, SpotItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CultureIntroResponse  extends DetailIntroResponse {
        @JsonProperty("item")
        private CultureItemResponse cultureItemResponse;

        @JsonCreator
        public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.cultureItemResponse = findFirstItemResponse(rootNode, CultureItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private EventItemResponse eventItemResponse;

        @JsonCreator
        public EventIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.eventItemResponse = findFirstItemResponse(rootNode, EventItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeportsIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private LeportsItemResponse leportsItemResponse;

        @JsonCreator
        public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.leportsItemResponse = findFirstItemResponse(rootNode, LeportsItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccommodationIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private AccommodationItemResponse accommodationItemResponse;

        @JsonCreator
        public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.accommodationItemResponse = findFirstItemResponse(rootNode, AccommodationItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShoppingIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private ShoppingItemResponse shoppingItemResponse;

        @JsonCreator
        public ShoppingIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.shoppingItemResponse = findFirstItemResponse(rootNode, ShoppingItemResponse.class);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private RestaurantItemResponse restaurantItemResponse;

        @JsonCreator
        public RestaurantIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            this.restaurantItemResponse = findFirstItemResponse(rootNode, RestaurantItemResponse.class);
        }
    }
}
