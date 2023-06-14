package com.oceans7.dib.domain.location;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    public LocationResponseDto searchPlace(SearchLocationRequestDto searchLocationRequestDto) {
        return null;
    }
}
