package com.oceans7.dib.location;

import com.oceans7.dib.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.location.dto.response.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    public LocationResponseDto searchPlace(SearchLocationRequestDto searchLocationRequestDto) {
        return null;
    }
}
