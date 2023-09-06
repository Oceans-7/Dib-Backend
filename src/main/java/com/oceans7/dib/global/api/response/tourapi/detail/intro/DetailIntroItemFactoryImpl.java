package com.oceans7.dib.global.api.response.tourapi.detail.intro;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;

public class DetailIntroItemFactoryImpl implements DetailIntroItemFactory {

    @Override
    public <T extends DetailIntroResponse> Class<T> getClassType(ContentType type) throws ApplicationException {
        switch(type) {
            case TOURIST_SPOT -> { return (Class<T>) DetailIntroResponse.SpotIntroResponse.class; }
            case CULTURAL_SITE -> { return (Class<T>) DetailIntroResponse.CultureIntroResponse.class; }
            case EVENT -> { return (Class<T>) DetailIntroResponse.EventIntroResponse.class; }
            case LEPORTS -> { return (Class<T>) DetailIntroResponse.LeportsIntroResponse.class; }
            case ACCOMMODATION -> { return (Class<T>) DetailIntroResponse.AccommodationIntroResponse.class; }
            case SHOPPING -> { return (Class<T>) DetailIntroResponse.ShoppingIntroResponse.class; }
            case RESTAURANT -> { return (Class<T>) DetailIntroResponse.RestaurantIntroResponse.class; }
            default -> throw new ApplicationException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }

    @Override
    public DetailIntroItemResponse getIntroItem(ContentType type, DetailIntroResponse introApiResponse) {
        switch(type) {
            case TOURIST_SPOT -> { return ((DetailIntroResponse.SpotIntroResponse) introApiResponse).getSpotItemResponse(); }
            case CULTURAL_SITE -> { return ((DetailIntroResponse.CultureIntroResponse) introApiResponse).getCultureItemResponse(); }
            case EVENT -> { return ((DetailIntroResponse.EventIntroResponse) introApiResponse).getEventItemResponse(); }
            case LEPORTS -> { return ((DetailIntroResponse.LeportsIntroResponse) introApiResponse).getLeportsItemResponse(); }
            case ACCOMMODATION -> { return ((DetailIntroResponse.AccommodationIntroResponse) introApiResponse).getAccommodationItemResponse(); }
            case SHOPPING -> { return ((DetailIntroResponse.ShoppingIntroResponse) introApiResponse).getShoppingItemResponse(); }
            case RESTAURANT -> { return ((DetailIntroResponse.RestaurantIntroResponse) introApiResponse).getRestaurantItemResponse(); }
            default -> throw new ApplicationException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }
}
