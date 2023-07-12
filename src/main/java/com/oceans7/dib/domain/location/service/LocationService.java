package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.weather.dto.FcstType;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.util.CoordinateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final KakaoLocalAPIService kakaoLocalAPIService;

    private final VilageFcstAPIService vilageFcstAPIService;

    private String baseDate, baseTime;
    private boolean isDay;

    public LocationResponseDto searchPlace(SearchLocationRequestDto searchLocationRequestDto) {
        int baseX, baseY;
        String addressName;

        LocalResponse addressItems = kakaoLocalAPIService.getGeoAddressLocalApi(searchLocationRequestDto.getMapX(), searchLocationRequestDto.getMapY());
        addressName = addressItems.getAddressItems().get(0).getRoadAddress().getAddressName();

        CoordinateUtil.LatXLngY grid = CoordinateUtil.convertGRID_GPS(searchLocationRequestDto.getMapX(), searchLocationRequestDto.getMapY());

        baseX = (int)grid.x;
        baseY = (int)grid.y;

        setBaseDateTime(40);
        FcstAPICommonListResponse nowCast = vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime);

        setBaseDateTime(60);
        FcstAPICommonListResponse ultraFcst = vilageFcstAPIService.getUltraForecast(baseX, baseY, baseDate, baseTime);

        setBaseDateTime(0);
        int sky = getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.SKY, baseTime);
        int precipitation = getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.PTY, baseTime);
        boolean isThunder = getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.LGT, baseTime) > 0 ? true : false;

        WeatherType weatherType = WeatherType.getWeatherType(sky, precipitation, isThunder, isDay);
        double temperatures = getTemperatures(nowCast.getFcstAPICommonItemResponseList(), FcstType.T1H);

        return LocationResponseDto.of(addressName, weatherType, temperatures);
    }

    private void setBaseDateTime(int callableTime) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");
        this.baseTime = now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);

        // 자정
        if(now.getHour() == 0 && now.getMinute() < callableTime) {
            this.baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        isDay = (now.getHour() >= 6 && now.getHour() < 18) ? true : false;
    }
    private int getFcstItem(List<FcstAPICommonItemResponse> items, FcstType category, String baseTime) {
        for(FcstAPICommonItemResponse item : items) {
            if(item.getCategory().equals(category.name()) && item.getFcstTime().equals(baseTime)) {
                return Integer.parseInt(item.getFcstValue());
            }
        }
        return 0;
    }

    private double getTemperatures(List<FcstAPICommonItemResponse> items, FcstType category) {
        for(FcstAPICommonItemResponse item : items) {
            if(item.getCategory().equals(category.name())) {
                return Double.parseDouble(item.getObsrValue());
            }
        }
        return 0;
    }
}