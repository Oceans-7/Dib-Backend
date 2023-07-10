package com.oceans7.dib.global;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;

public class MockRequest {
    public static SearchLocationRequestDto testSearchLocationReq() {
        return new SearchLocationRequestDto(126.9779692, 37.566535);
    }
}
