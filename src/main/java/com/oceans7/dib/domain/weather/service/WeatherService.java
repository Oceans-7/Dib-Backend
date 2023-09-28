package com.oceans7.dib.domain.weather.service;

import com.oceans7.dib.domain.weather.dto.FcstType;
import com.oceans7.dib.domain.weather.dto.ObsCode;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.dto.request.GetLocationWeatherRequestDto;
import com.oceans7.dib.domain.weather.dto.response.GetCurrentWeatherResponseDto;
import com.oceans7.dib.domain.weather.dto.response.TideEvent;
import com.oceans7.dib.domain.weather.dto.response.WeatherInformation;
import com.oceans7.dib.domain.weather.service.vo.CurrentWeatherVO;
import com.oceans7.dib.domain.weather.service.vo.DivingIndicator;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.khoaGoKr.GetCurrentWaveHeightResponse;
import com.oceans7.dib.global.api.response.khoaGoKr.OceanIndexPredictionResponse;
import com.oceans7.dib.global.api.response.khoaGoKr.TidePredictionListResponse;
import com.oceans7.dib.global.api.response.khoaGoKr.WaterTemperatureResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import com.oceans7.dib.global.api.service.KhoaGoKrAPIService;
import com.oceans7.dib.global.api.service.VilageFcstAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.BaseTimeUtil;
import com.oceans7.dib.global.util.CoordinateUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static com.oceans7.dib.global.util.BaseTimeUtil.FCST_CALLABLE_TIME;
import static com.oceans7.dib.global.util.BaseTimeUtil.NCST_CALLABLE_TIME;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final KhoaGoKrAPIService khoaGoKrAPIService;

    private final VilageFcstAPIService vilageFcstAPIService;

    private final KakaoLocalAPIService kakaoLocalAPIService;

    public GetCurrentWeatherResponseDto getWeather(GetLocationWeatherRequestDto getLocationWeatherRequestDto) {

        LocalDateTime localDateTime = LocalDateTime.now();
        double latitude = getLocationWeatherRequestDto.getLatitude();
        double longitude = getLocationWeatherRequestDto.getLongitude();
        String addressName = getAddressName(latitude, longitude);

        try {
            CurrentWeatherVO currentWeather = getCurrentWeather(latitude, longitude, localDateTime);

            if (ValidatorUtil.isEmpty(currentWeather)) {
                return GetCurrentWeatherResponseDto.of(
                        null,
                        null
                );
            }

            WeatherInformation weatherInformation = WeatherInformation.of(
                    LocalDate.now(),
                    currentWeather.getWeatherType(),
                    currentWeather.getAirTemperature(),
                    currentWeather.getWaterTemperature(),
                    currentWeather.getWindSpeed(),
                    currentWeather.getWaveHeight(),
                    currentWeather.getDivingIndicator(),
                    currentWeather.getTideEvents()
            );


            return GetCurrentWeatherResponseDto.of(
                    addressName,
                    weatherInformation
            );

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }

    }

    public CurrentWeatherVO getCurrentWeather(double latitude, double longitude, LocalDateTime now) throws ExecutionException, InterruptedException {

        String baseDate = BaseTimeUtil.calculateBaseDate(now, NCST_CALLABLE_TIME);
        String baseTime = BaseTimeUtil.calculateBaseTime(now, NCST_CALLABLE_TIME);
        String forecastBaseDate = BaseTimeUtil.calculateBaseDate(now, FCST_CALLABLE_TIME);
        String forecastBaseTime = BaseTimeUtil.calculateBaseTime(now, FCST_CALLABLE_TIME);

        ObsCode nearestObsCode = getNearestObsCode(longitude, latitude);

        if (nearestObsCode == null) {
            return null;
        }

        // 초단기 실황
        CompletableFuture<FcstAPICommonListResponse> nowCast = vilageFcstAPIService.getNowCast(nearestObsCode.getNx(), nearestObsCode.getNy(), baseDate, baseTime);

        // 초단기 예측
        CompletableFuture<FcstAPICommonListResponse> ultraFcst = vilageFcstAPIService.getUltraForecast(nearestObsCode.getNx(), nearestObsCode.getNy(), forecastBaseDate, forecastBaseTime);

        // 조석
        CompletableFuture<TidePredictionListResponse> tidePrediction = khoaGoKrAPIService.getTidePrediction(nearestObsCode, baseDate);

        // 수온
        CompletableFuture<WaterTemperatureResponse> waterTemperature = khoaGoKrAPIService.getCurrentWaterTemperature(nearestObsCode, baseDate);

        // 파고
        CompletableFuture<GetCurrentWaveHeightResponse> waveHeight = khoaGoKrAPIService.getCurrentWaveHeight(nearestObsCode, baseDate);

        //다이빙 지수
        CompletableFuture<OceanIndexPredictionResponse> divingIndex = khoaGoKrAPIService.getOceanIndexPrediction();

        // 비동기 blocking
        CompletableFuture.allOf(nowCast, ultraFcst, tidePrediction, waterTemperature, waveHeight, divingIndex).join();

        List<FcstAPICommonItemResponse> items = nowCast.exceptionally(
                throwable -> null
        ).get().getFcstAPICommonItemResponseList();

        // 기온
        Double airTemperature = getCurrentTemperature(items);

        // 하늘 상태
        List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList = ultraFcst.exceptionally(
                throwable -> null
        ).get().getFcstAPICommonItemResponseList();

        WeatherType weatherType = getCurrentWeatherType(fcstAPICommonItemResponseList);

        // 조석
        List<TidePredictionListResponse.TideData> tideEventData = tidePrediction.exceptionally(
                throwable -> null
        ).get().getResult().getData();
        List<TideEvent> currentTideEvent = getCurrentTideEvent(tideEventData);

        // 풍속
        Double currentWindSpeed = getCurrentWindSpeed(items);

        // 파고
        List<GetCurrentWaveHeightResponse.WaveHeight> waveHeightData = waveHeight.exceptionally(
                throwable -> null
        ).get().getResult().getData();
        Double currentWaveHeight = getCurrentWaveHeight(waveHeightData);

        // 수온
        LinkedList<WaterTemperatureResponse.WaterTemperatureData> waterTemperatureData = waterTemperature.exceptionally(
                throwable -> null
        ).get().getResult().getData();
        Double currentWaterTemperature = getCurrentWaterTemperature(waterTemperatureData);

        // 다이빙 지수
        List<OceanIndexPredictionResponse.IndexInfo> indexInfoData = divingIndex.exceptionally(
                throwable -> null
        ).get().getResult().getData();
        DivingIndicator divingIndicator = getCurrentDivingIndex(indexInfoData, latitude, longitude);

        return CurrentWeatherVO.of(
                weatherType,
                airTemperature,
                currentWaterTemperature,
                currentWindSpeed,
                divingIndicator,
                currentTideEvent,
                currentWaveHeight
        );
    }

    private int getFcstItem(List<FcstAPICommonItemResponse> items, FcstType category, String nowTime) {
        for(FcstAPICommonItemResponse item : items) {
            if(item.getCategory().equals(category.name()) && item.getFcstTime().equals(nowTime)) {
                return Integer.parseInt(item.getFcstValue());
            }
        }
        return 0;
    }

    private String getAddressName(double latitude, double longitude) {
        LocalResponse addressItems = kakaoLocalAPIService.getGeoAddressLocalApi(latitude, longitude);
        int firstIndex = 0;
        if (!ValidatorUtil.isEmpty(addressItems.getAddressItems())) {
            return addressItems.getAddressItems().get(firstIndex).getAddressName();
        }

        ObsCode nearestObsCode = getNearestObsCode(longitude, latitude);

        if (!ValidatorUtil.isEmpty(nearestObsCode)) {
            return nearestObsCode.getName();
        }

        return null;
    }

    private List<String> getBaseDateList() {

        return Stream.of(0, 1, 2, 3, 4).map(i -> LocalDate.now().plusDays(i)).map(this::getBaseDate).toList();
    }

    private String getBaseDate(LocalDate localDate) {
        return localDate.toString().replaceAll("-", "");
    }

    private String getBaseTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    private ObsCode getNearestObsCode(double x, double y) {

        // TODO : 최대 거리는 추후에 변경
        double maximumDistance = 26;

        ObsCode obsCode = Stream.of(ObsCode.values()).min((o1, o2) -> {
            double o1Distance = CoordinateUtil.calculateDistance(o1.getX(), o1.getY(), x, y);
            double o2Distance = CoordinateUtil.calculateDistance(o2.getX(), o2.getY(), x, y);

            return Double.compare(o1Distance, o2Distance);
        }).orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));

        double distance = CoordinateUtil.calculateDistance(obsCode.getX(), obsCode.getY(), x, y);

        if (distance > maximumDistance) {
            return null;
        }

        return obsCode;
    }

    private double getDistance(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
    }

    private Double getCurrentTemperature(List<FcstAPICommonItemResponse> items) {
        if (ValidatorUtil.isEmpty(items)) {
            return null;
        }

        FcstAPICommonItemResponse fcstAPICommonItemResponse = items.stream().filter(item ->
                item.getCategory().equals(FcstType.T1H.name())
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUNT_WEATHER_INFO));

        return Double.parseDouble(fcstAPICommonItemResponse.getObsrValue());
    }

    private WeatherType getCurrentWeatherType(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList) {
        LocalDateTime now = LocalDateTime.now();
        int oneHour = 1;
        LocalDateTime forecastTime = now.withMinute(0).withSecond(0).withNano(0).plusHours(oneHour);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        String baseTime = forecastTime.format(formatter);
        boolean isDay = now.getHour() >= 6 && now.getHour() < 18;
        int sky = getFcstItem(fcstAPICommonItemResponseList, FcstType.SKY, baseTime);
        int precipitation = getFcstItem(fcstAPICommonItemResponseList, FcstType.PTY, baseTime);
        boolean isThunder = (getFcstItem(fcstAPICommonItemResponseList, FcstType.LGT, baseTime) > 0);

        return WeatherType.getWeatherType(sky, precipitation, isThunder, isDay);
    }

    private Double getCurrentWindSpeed(List<FcstAPICommonItemResponse> items) {
        if (ValidatorUtil.isEmpty(items)) {
            return null;
        }

        FcstAPICommonItemResponse windSpeedResponse = items.stream().filter(item ->
                item.getCategory().equals(FcstType.WSD.name())
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUNT_WEATHER_INFO));

        return Double.parseDouble(windSpeedResponse.getObsrValue());
    }
    private Double getCurrentWaveHeight(List<GetCurrentWaveHeightResponse.WaveHeight> waveHeightList) {
        if (ValidatorUtil.isEmpty(waveHeightList)) {
            return null;
        }

        int lastIndex = waveHeightList.size() - 1;
        if (ValidatorUtil.isEmpty(waveHeightList.get(lastIndex).getWaveHeight())) {
            return null;
        }
        return Double.parseDouble(waveHeightList.get(lastIndex).getWaveHeight());
    }

    private Double getCurrentWaterTemperature(LinkedList<WaterTemperatureResponse.WaterTemperatureData> waterTemperatureDataList) {
        if (ValidatorUtil.isEmpty(waterTemperatureDataList)) {
            return null;
        }

        return Double.parseDouble(waterTemperatureDataList.getLast().getWaterTemp());
    }

    private DivingIndicator getCurrentDivingIndex(List<OceanIndexPredictionResponse.IndexInfo> indexInfoList, Double latitude, Double longitude) {
        if (ValidatorUtil.isEmpty(indexInfoList)) {
            return null;
        }
        OceanIndexPredictionResponse.IndexInfo indexInfo = indexInfoList.stream().min((o1, o2) -> {
            double o1Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o1.getLat()), Double.parseDouble(o1.getLon()), latitude, longitude);
            double o2Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o2.getLat()), Double.parseDouble(o2.getLat()), latitude, longitude);

            return Double.compare(o1Distance, o2Distance);
        }).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUNT_WEATHER_INFO));

        if (ValidatorUtil.isEmpty(indexInfo)) {
            return null;
        }

        return DivingIndicator.of(indexInfo.getTotalScore());
    }

    private List<TideEvent> getCurrentTideEvent(List<TidePredictionListResponse.TideData> tideEventData) {
        if (ValidatorUtil.isEmpty(tideEventData)) {
            return null;
        }
        return tideEventData.stream().map(
                tideData -> TideEvent.of(LocalDateTime.parse(tideData.getTphTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Double.parseDouble(tideData.getTphLevel()), tideData.getHlCode())
        ).toList();

    }
}
