package com.oceans7.dib.global.api.response.tourapi.detail.intro;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailIntroResponse {

    @Getter
    public static class SpotIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.SpotItemResponse spotItemResponses;

        @JsonCreator
        public SpotIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.SpotItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.SpotItemResponse[].class)).toList();
                this.spotItemResponses = tmp.get(0);
            } else {
                this.spotItemResponses = new DetailIntroItemResponse.SpotItemResponse();
            }
        }
    }

    @Getter
    public static class CultureIntroResponse  extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.CultureItemResponse cultureItemResponse;

        @JsonCreator
        public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.CultureItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.CultureItemResponse[].class)).toList();
                this.cultureItemResponse = tmp.get(0);
            } else {
                this.cultureItemResponse = new DetailIntroItemResponse.CultureItemResponse();
            }
        }
    }

    @Getter
    public static class EventIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.EventItemResponse eventItemResponse;

        @JsonCreator
        public EventIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.EventItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.EventItemResponse[].class)).toList();
                this.eventItemResponse = tmp.get(0);
            } else {
                this.eventItemResponse = new DetailIntroItemResponse.EventItemResponse();
            }
        }
    }

    @Getter
    public static class LeportsIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.LeportsItemResponse leportsItemResponse;

        @JsonCreator
        public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.LeportsItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.LeportsItemResponse[].class)).toList();
                this.leportsItemResponse = tmp.get(0);
            } else {
                this.leportsItemResponse = new DetailIntroItemResponse.LeportsItemResponse();
            }
        }
    }

    @Getter
    public static class AccommodationIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.AccommodationItemResponse accommodationItemResponse;

        @JsonCreator
        public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.AccommodationItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.AccommodationItemResponse[].class)).toList();
                this.accommodationItemResponse = tmp.get(0);
            } else {
                this.accommodationItemResponse = new DetailIntroItemResponse.AccommodationItemResponse();
            }
        }
    }

    @Getter
    public static class ShoppingIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.ShoppingItemResponse shoppingItemResponse;

        @JsonCreator
        public ShoppingIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.ShoppingItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.ShoppingItemResponse[].class)).toList();
                this.shoppingItemResponse = tmp.get(0);
            } else {
                this.shoppingItemResponse = new DetailIntroItemResponse.ShoppingItemResponse();
            }
        }
    }

    @Getter
    public static class RestaurantIntroResponse extends DetailIntroResponse {
        @JsonProperty("item")
        private DetailIntroItemResponse.RestaurantItemResponse restaurantItemResponse;

        @JsonCreator
        public RestaurantIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemRootNode = rootNode.findValue("items");

            if(itemRootNode.has("item")) {
                JsonNode itemNode = rootNode.findValue("item");

                List<DetailIntroItemResponse.RestaurantItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, DetailIntroItemResponse.RestaurantItemResponse[].class)).toList();
                this.restaurantItemResponse = tmp.get(0);
            } else {
                this.restaurantItemResponse = new DetailIntroItemResponse.RestaurantItemResponse();
            }
        }
    }

}
