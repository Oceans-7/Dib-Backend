package com.oceans7.dib.global.api.response.tourapi.detail.intro;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.exception.ApplicationException;

public interface DetailIntroItemFactory {
    <T extends DetailIntroResponse> Class<T> getClassType(ContentType type) throws ApplicationException;
    DetailIntroItemResponse getIntroItem(ContentType type, DetailIntroResponse introApiResponse);
}
