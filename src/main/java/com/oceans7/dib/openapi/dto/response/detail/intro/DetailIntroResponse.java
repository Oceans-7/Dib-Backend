package com.oceans7.dib.openapi.dto.response.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.detail.intro.DetailIntroItemResponse.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailIntroResponse {

    @Getter
    public static class SpotIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private SpotItemResponse spotItemResponses;

        @JsonCreator
        public SpotIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<SpotItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, SpotItemResponse[].class)).toList();
                this.spotItemResponses = tmp.get(0);
            } else {
                this.spotItemResponses = new SpotItemResponse();
            }
        }
    }

    @Getter
    public static class CultureIntroResponse  extends DetailIntroResponse {
        @JsonProperty("item")
        private CultureItemResponse cultureItemResponse;

        @JsonCreator
        public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<CultureItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, CultureItemResponse[].class)).toList();
                this.cultureItemResponse = tmp.get(0);
            } else {
                this.cultureItemResponse = new CultureItemResponse();
            }
        }
    }

    @Getter
    public static class EventIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private EventItemResponse eventItemResponse;

        @JsonCreator
        public EventIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<EventItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, EventItemResponse[].class)).toList();
                this.eventItemResponse = tmp.get(0);
            } else {
                this.eventItemResponse = new EventItemResponse();
            }
        }
    }

    @Getter
    public static class LeportsIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private LeportsItemResponse leportsItemResponse;

        @JsonCreator
        public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<LeportsItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, LeportsItemResponse[].class)).toList();
                this.leportsItemResponse = tmp.get(0);
            } else {
                this.leportsItemResponse = new LeportsItemResponse();
            }
        }
    }

    @Getter
    public static class AccommodationIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private AccommodationItemResponse accommodationItemResponse;

        @JsonCreator
        public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<AccommodationItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, AccommodationItemResponse[].class)).toList();
                this.accommodationItemResponse = tmp.get(0);
            } else {
                this.accommodationItemResponse = new AccommodationItemResponse();
            }
        }
    }

    @Getter
    public static class ShoppingIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private ShoppingItemResponse shoppingItemResponse;

        @JsonCreator
        public ShoppingIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<ShoppingItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, ShoppingItemResponse[].class)).toList();
                this.shoppingItemResponse = tmp.get(0);
            } else {
                this.shoppingItemResponse = new ShoppingItemResponse();
            }
        }
    }

    @Getter
    public static class RestaurantIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private RestaurantItemResponse restaurantItemResponse;

        @JsonCreator
        public RestaurantIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<RestaurantItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, RestaurantItemResponse[].class)).toList();
                this.restaurantItemResponse = tmp.get(0);
            } else {
                this.restaurantItemResponse = new RestaurantItemResponse();
            }
        }
    }

}
