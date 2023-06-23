package com.oceans7.dib.domain.place;

import com.oceans7.dib.domain.place.dto.request.PlaceRequestDto;
import com.oceans7.dib.domain.place.dto.request.SearchPlaceRequestDto;
import com.oceans7.dib.domain.place.dto.response.DetailPlaceInformationResponseDto;
import com.oceans7.dib.domain.place.dto.response.PlaceResponseDto;
import com.oceans7.dib.domain.place.dto.response.SearchPlaceResponseDto;
import com.oceans7.dib.domain.place.dto.request.GetPlaceDetailRequestDto;
import com.oceans7.dib.domain.place.dto.response.SimplePlaceInformationDto;
import com.oceans7.dib.global.util.ValidatorUtil;
import com.oceans7.dib.openapi.dto.response.simple.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.simple.TourAPICommonListResponse;
import com.oceans7.dib.openapi.service.TourAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final TourAPIService tourAPIService;

    public PlaceResponseDto getPlace(PlaceRequestDto request) {
        TourAPICommonListResponse apiResponse = null;
        String contentTypeCode = "";
        String arrangeTypeName = "";

        // 필터링 확인
        if(ValidatorUtil.isNotEmpty(request.getContentType())) {
            contentTypeCode = String.valueOf(request.getContentType().getCode());
        }
        if(ValidatorUtil.isNotEmpty(request.getArrangeType())) {
            arrangeTypeName = request.getArrangeType().name();
        }

        if(ValidatorUtil.isEmpty(request.getArea())) {
            // 지역 필터 없다면 위치 기반
            apiResponse = tourAPIService.fetchDataFromLocationBasedApi(request.getMapX(), request.getMapY(),
                    contentTypeCode, arrangeTypeName,
                    request.getPage(), request.getPageSize());
        } else {
            // 지역 필터 있다면 지역 기반
            // areaCode 조회
            String areaName = request.getArea();
            AreaCodeList list = tourAPIService.fetchDataFromAreaCodeApi("");
            String areaCode = list.getAreaCodeByName(areaName);
            System.out.println(areaCode);
            String sigunguCode = "";

            // sigunguCode 조회
            if(ValidatorUtil.isNotEmpty(request.getSigungu())) {
                String sigunguName = request.getSigungu();
                AreaCodeList sigunguList = tourAPIService.fetchDataFromAreaCodeApi(areaCode);
                sigunguCode = sigunguList.getAreaCodeByName(sigunguName);
            }

            apiResponse = tourAPIService.fetchDataFromAreaBasedApi(areaCode, sigunguCode, contentTypeCode, arrangeTypeName);
        }

        SimplePlaceInformationDto[] simpleDto = apiResponse.getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: new)
                .toArray(SimplePlaceInformationDto[]::new);

        return new PlaceResponseDto(simpleDto, apiResponse);
    }

    public SearchPlaceResponseDto searchPlace(SearchPlaceRequestDto searchPlaceRequestDto) {
        return null;
    }

    public DetailPlaceInformationResponseDto getPlaceDetail(GetPlaceDetailRequestDto getPlaceDetailRequestDto) {
        return null;
    }
}
