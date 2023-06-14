package com.oceans7.dib.place;

import com.oceans7.dib.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.place.dto.response.SearchPlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    public SearchPlaceResponseDto searchPlace(SearchPlaceRequestDto searchPlaceRequestDto) {
        return null;
    }

    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto getPlaceDetailRequestDto) {
        return null;
    }
}
