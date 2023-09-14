package com.oceans7.dib.domain.location.service;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.weather.dto.FcstType;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.AddressItem;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.CoordinateUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
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

    private final static int NCST_CALLABLE_TIME = 40;
    private final static int FCST_CALLABLE_TIME = 60;

    public LocationResponseDto searchPlace(SearchLocationRequestDto searchLocationRequestDto) {
        int baseX, baseY;
        String addressName;
        String baseDate, baseTime;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        boolean isDay = now.getHour() >= 6 && now.getHour() < 18;

        LocalResponse addressItems = kakaoLocalAPIService.getGeoAddressLocalApi(searchLocationRequestDto.getMapX(), searchLocationRequestDto.getMapY());

        if(ValidatorUtil.isEmpty(addressItems.getAddressItems())) {
            throw new ApplicationException(ErrorCode.NOT_FOUNT_USER_LOCATION);
        }

        AddressItem addressItem = addressItems.getAddressItems().get(0);

        addressName = ValidatorUtil.isNotEmpty(addressItem.getRoadAddress()) ?
                addressItem.getRoadAddress().getAddressName() : addressItem.getAddress().getAddressName();

        CoordinateUtil.LatXLngY grid = CoordinateUtil.convertGRID_GPS(searchLocationRequestDto.getMapX(), searchLocationRequestDto.getMapY());

        baseX = (int)grid.x;
        baseY = (int)grid.y;

        baseDate = calculateBaseDate(now, NCST_CALLABLE_TIME);
        baseTime = calculateBaseTime(now, NCST_CALLABLE_TIME);
        FcstAPICommonListResponse nowCast = vilageFcstAPIService.getNowCast(baseX, baseY, baseDate, baseTime);

        baseDate = calculateBaseDate(now, FCST_CALLABLE_TIME);
        baseTime = calculateBaseTime(now, FCST_CALLABLE_TIME);
        FcstAPICommonListResponse ultraFcst = vilageFcstAPIService.getUltraForecast(baseX, baseY, baseDate, baseTime);

        String nowTime = now.format(DateTimeFormatter.ofPattern("HH00"));

        int sky = getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.SKY, nowTime);
        int precipitation = getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.PTY, nowTime);
        boolean isThunder = (getFcstItem(ultraFcst.getFcstAPICommonItemResponseList(), FcstType.LGT, nowTime) > 0);

        WeatherType weatherType = WeatherType.getWeatherType(sky, precipitation, isThunder, isDay);
        double temperatures = getTemperatures(nowCast.getFcstAPICommonItemResponseList(), FcstType.T1H);

        return LocationResponseDto.of(addressName, weatherType, temperatures);
    }

    private String calculateBaseDate(LocalDateTime now, int callableTime) {
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 자정
        if (now.getHour() == 0 && now.getMinute() < callableTime) {
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate;
    }

    private String calculateBaseTime(LocalDateTime now, int callableTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH00");
        return now.getMinute() < callableTime ?
                now.minusHours(1).format(timeFormatter) : now.format(timeFormatter);
    }

    private int getFcstItem(List<FcstAPICommonItemResponse> items, FcstType category, String nowTime) {
        for(FcstAPICommonItemResponse item : items) {
            if(item.getCategory().equals(category.name()) && item.getFcstTime().equals(nowTime)) {
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
