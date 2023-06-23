package com.oceans7.dib.domain.place;

import com.oceans7.dib.domain.place.dto.request.PlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.PlaceResponseDto;
import com.oceans7.dib.domain.place.dto.response.SearchPlaceResponseDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    public PlaceResponseDto getPlace(PlaceRequestDto placeRequestDto) {
        return null;
    }

    public SearchPlaceResponseDto searchPlace(SearchPlaceRequestDto searchPlaceRequestDto) {
        return null;
    }

    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto getPlaceDetailRequestDto) {
        return null;
    }
}
