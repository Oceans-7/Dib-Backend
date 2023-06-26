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
public abstract class AbstractDetailIntroResponse {

    @Getter
    public class SpotIntroResponse extends AbstractDetailIntroResponse {

        @JsonProperty("item")
        private SpotItemResponse spotItemResponses;

        @JsonCreator
        public SpotIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<SpotItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, SpotItemResponse[].class)).toList();
            this.spotItemResponses = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class CultureIntroResponse  extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private CultureItemResponse cultureItemResponse;

        @JsonCreator
        public CultureIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<CultureItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, CultureItemResponse[].class)).toList();
            this.cultureItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class EventIntroResponse extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private EventItemResponse eventItemResponse;

        @JsonCreator
        public EventIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<EventItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, EventItemResponse[].class)).toList();
            this.eventItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class TourCourseIntroResponse extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private TourCourseItemResponse tourCourseItemResponse;

        @JsonCreator
        public TourCourseIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<TourCourseItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, TourCourseItemResponse[].class)).toList();
            this.tourCourseItemResponse =  tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class LeportsIntroResponse extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private LeportsItemResponse leportsItemResponse;

        @JsonCreator
        public LeportsIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<LeportsItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, LeportsItemResponse[].class)).toList();
            this.leportsItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class AccommodationIntroResponse extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private AccommodationItemResponse accommodationItemResponse;

        @JsonCreator
        public AccommodationIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<AccommodationItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, AccommodationItemResponse[].class)).toList();
            this.accommodationItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

    @Getter
    public class ShoppingIntroResponse extends AbstractDetailIntroResponse {
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

    @Getter
    public class RestaurantIntroResponse extends AbstractDetailIntroResponse {
        @JsonProperty("item")
        private RestaurantItemResponse restaurantItemResponse;

        @JsonCreator
        public RestaurantIntroResponse(@JsonProperty("response") JsonNode rootNode) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode itemNode = rootNode.findValue("item");
            List<RestaurantItemResponse> tmp = Arrays.stream(objectMapper.treeToValue(itemNode, RestaurantItemResponse[].class)).toList();
            this.restaurantItemResponse = tmp != null && !tmp.isEmpty() ? tmp.get(0) : null;
        }
    }

}
