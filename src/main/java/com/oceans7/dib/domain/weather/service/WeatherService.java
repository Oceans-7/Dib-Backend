package com.oceans7.dib.domain.weather.service;

import com.oceans7.dib.domain.weather.WaterTemperatureObsCode;
import com.oceans7.dib.domain.weather.dto.FcstType;
import com.oceans7.dib.domain.weather.dto.ObsCode;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.domain.weather.dto.request.GetLocationWeatherRequestDto;
import com.oceans7.dib.domain.weather.dto.response.GetCurrentWeatherResponseDto;
import com.oceans7.dib.domain.weather.dto.response.GetForeCastWeatherResponseDto;
import com.oceans7.dib.domain.weather.dto.response.TideEvent;
import com.oceans7.dib.domain.weather.dto.response.WeatherInformation;
import com.oceans7.dib.domain.weather.service.vo.WeatherVO;
import com.oceans7.dib.domain.weather.service.vo.DivingIndicator;
import com.oceans7.dib.global.api.response.BaseAPiResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.khoaGoKr.*;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oceans7.dib.global.util.BaseTimeUtil.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final KhoaGoKrAPIService khoaGoKrAPIService;

    private final VilageFcstAPIService vilageFcstAPIService;

    private final KakaoLocalAPIService kakaoLocalAPIService;

    public GetCurrentWeatherResponseDto getCurrentWeather(GetLocationWeatherRequestDto getLocationWeatherRequestDto) {

        LocalDateTime localDateTime = LocalDateTime.now();
        double latitude = getLocationWeatherRequestDto.getLatitude();
        double longitude = getLocationWeatherRequestDto.getLongitude();
        String addressName = getAddressName(latitude, longitude);

        WeatherVO currentWeather = getCurrentWeatherInfo(latitude, longitude, localDateTime);

        if (ValidatorUtil.isEmpty(currentWeather)) {
            return GetCurrentWeatherResponseDto.of(
                    addressName,
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

    }

    public WeatherVO getCurrentWeatherInfo(double latitude, double longitude, LocalDateTime now) {

        String baseDate = BaseTimeUtil.calculateBaseDate(now, NCST_CALLABLE_TIME);
        String baseTime = BaseTimeUtil.calculateBaseTime(now, NCST_CALLABLE_TIME);
        String ultraForecastBaseDate = BaseTimeUtil.calculateBaseDate(now, FCST_CALLABLE_TIME);
        String ultraForecastBaseTime = BaseTimeUtil.calculateBaseTime(now, FCST_CALLABLE_TIME);
        ForecastBaseDateTime forecastBaseDateTime = getForecastBaseDateTime(now);
        String forecastBaseDate = forecastBaseDateTime.getForecastBaseDate();
        String forecastBaseTime = forecastBaseDateTime.getForecastBaseTime();

        ObsCode nearestObsCode = getNearestObsCode(longitude, latitude);
        WaterTemperatureObsCode nearestWaterTemperatureObsCode = getNearestWaterTemperatureObsCode(longitude, latitude);

        if (nearestObsCode == null) {
            return null;
        }

        // 초단기 실황
        CompletableFuture<FcstAPICommonListResponse> nowCast = vilageFcstAPIService.getNowCast(nearestObsCode.getNx(), nearestObsCode.getNy(), baseDate, baseTime);

        // 초단기 예보
        CompletableFuture<FcstAPICommonListResponse> ultraFcst = vilageFcstAPIService.getUltraForecast(nearestObsCode.getNx(), nearestObsCode.getNy(), ultraForecastBaseDate, ultraForecastBaseTime);

        // 단기 예측
        CompletableFuture<FcstAPICommonListResponse> commonFcst = vilageFcstAPIService.getForecastInfo(nearestObsCode.getNx(), nearestObsCode.getNy(), forecastBaseDate, forecastBaseTime);

        // 조석
        CompletableFuture<TidePredictionListResponse> tidePrediction = khoaGoKrAPIService.getTidePrediction(nearestObsCode, baseDate);

        // 수온
        CompletableFuture<WaterTemperatureResponse> waterTemperature = khoaGoKrAPIService.getCurrentWaterTemperature(nearestWaterTemperatureObsCode, baseDate);

        //다이빙 지수
        CompletableFuture<OceanIndexPredictionResponse> divingIndex = khoaGoKrAPIService.getOceanIndexPrediction();

        // 비동기 blocking
        CompletableFuture.allOf(nowCast, ultraFcst, commonFcst, tidePrediction, waterTemperature, divingIndex).join();

        // 초단기 실황
        List<FcstAPICommonItemResponse> items = nowCast.exceptionally(
                throwable -> null
        ).join().getFcstAPICommonItemResponseList();

        // 단기 예보
        List<FcstAPICommonItemResponse> villageFcstAPICommonItemResponseList = commonFcst.exceptionally(
                throwable -> null
        ).join().getFcstAPICommonItemResponseList();

        // 기온
        Double airTemperature = getCurrentTemperature(items);

        // 하늘 상태
        List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList = ultraFcst.exceptionally(
                throwable -> null
        ).join().getFcstAPICommonItemResponseList();

        WeatherType weatherType = getWeatherType(fcstAPICommonItemResponseList);

        // 조석
        List<TidePredictionListResponse.TideData> tideEventData = tidePrediction.exceptionally(
                throwable -> null
        ).join().getResult().getData();
        List<TideEvent> currentTideEvent = getCurrentTideEvent(tideEventData);

        // 풍속
        Double currentWindSpeed = getCurrentWindSpeed(items);

        // 파고
        Double currentWaveHeight = getCurrentWaveHeight(villageFcstAPICommonItemResponseList);

        // 수온
        LinkedList<WaterTemperatureResponse.WaterTemperatureData> waterTemperatureData = waterTemperature.exceptionally(
                throwable -> null
        ).join().getResult().getData();
        Double currentWaterTemperature = getCurrentWaterTemperature(waterTemperatureData);

        // 다이빙 지수
        List<OceanIndexPredictionResponse.IndexInfo> indexInfoData = divingIndex.exceptionally(
                throwable -> null
        ).join().getResult().getData();
        DivingIndicator divingIndicator = getCurrentDivingIndex(indexInfoData, latitude, longitude);

        return WeatherVO.of(
                now.toLocalDate(),
                weatherType,
                airTemperature,
                currentWaterTemperature,
                currentWindSpeed,
                divingIndicator,
                currentTideEvent,
                currentWaveHeight
        );
    }

    public GetForeCastWeatherResponseDto getForecastWeather(GetLocationWeatherRequestDto getLocationWeatherRequestDto) {

        LocalDateTime localDateTime = LocalDateTime.now();
        double latitude = getLocationWeatherRequestDto.getLatitude();
        double longitude = getLocationWeatherRequestDto.getLongitude();
        String addressName = getAddressName(latitude, longitude);

        List<WeatherVO> forecastWeather = getForecastWeatherInfo(latitude, longitude, localDateTime);

        if (ValidatorUtil.isEmpty(forecastWeather)) {
            return GetForeCastWeatherResponseDto.of(
                    addressName,
                    null
            );
        }

        List<WeatherInformation> weatherInformationList = forecastWeather.stream().map(
                weatherVO -> WeatherInformation.of(
                        weatherVO.getDate(),
                        weatherVO.getWeatherType(),
                        weatherVO.getAirTemperature(),
                        weatherVO.getWaterTemperature(),
                        weatherVO.getWindSpeed(),
                        weatherVO.getWaveHeight(),
                        weatherVO.getDivingIndicator(),
                        weatherVO.getTideEvents()
                )
        ).collect(Collectors.toList());


        return GetForeCastWeatherResponseDto.of(
                addressName,
                weatherInformationList
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
        LocalResponse addressItems = kakaoLocalAPIService.getGeoAddressLocalApi(longitude, latitude);
        int firstIndex = 0;
        if (!ValidatorUtil.isEmpty(addressItems.getAddressItems())
                && !ValidatorUtil.isEmpty(addressItems.getAddressItems().get(0).getAddress().getAddressName())) {
            return addressItems.getAddressItems().get(firstIndex).getAddress().getAddressName();
        }

        ObsCode nearestObsCode = getNearestObsCode(longitude, latitude);

        if (!ValidatorUtil.isEmpty(nearestObsCode)) {
            return nearestObsCode.getName();
        }

        return null;
    }

    private List<LocalDate> getForecastDateList(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        return Stream.of(1, 2, 3).map(localDate::plusDays).toList();
    }

    private String getBaseDate(LocalDate localDate) {
        return localDate.toString().replaceAll("-", "");
    }

    private ObsCode getNearestObsCode(double x, double y) {

        double maximumDistance = 26;

        ObsCode obsCode = Stream.of(ObsCode.values())
                .min(Comparator.comparingDouble(o -> CoordinateUtil.calculateDistance(o.getX(), o.getY(), x, y)))
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));

        double distance = CoordinateUtil.calculateDistance(obsCode.getX(), obsCode.getY(), x, y);

        if (distance > maximumDistance) {
            return null;
        }

        return obsCode;
    }

    private WaterTemperatureObsCode getNearestWaterTemperatureObsCode(double x, double y) {

        WaterTemperatureObsCode obsCode = Stream.of(WaterTemperatureObsCode.values())
                .min(Comparator.comparingDouble(o -> CoordinateUtil.calculateDistance(o.getX(), o.getY(), x, y)))
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));

        return obsCode;
    }

    private Double getCurrentTemperature(List<FcstAPICommonItemResponse> items) {
        if (ValidatorUtil.isEmpty(items)) {
            return null;
        }

        FcstAPICommonItemResponse fcstAPICommonItemResponse = items.stream().filter(item ->
                item.getCategory().equals(FcstType.T1H.name())
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO));

        return Double.parseDouble(fcstAPICommonItemResponse.getObsrValue());
    }

    private WeatherType getWeatherType(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList) {
        LocalDateTime now = LocalDateTime.now();
        String baseTime = calculateUltraFcstTime(now);
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
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO));

        return Double.parseDouble(windSpeedResponse.getObsrValue());
    }
    private Double getCurrentWaveHeight(List<FcstAPICommonItemResponse> items) {

        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH00"));
        FcstAPICommonItemResponse fcstAPICommonItemResponse = items.stream().filter(
                item -> item.getCategory().equals(FcstType.WAV.name()) && item.getFcstTime().equals(nowTime)
        ).findFirst().orElseGet(() -> null);

        if (ValidatorUtil.isEmpty(fcstAPICommonItemResponse)) {
            return null;
        }

        return Double.parseDouble(fcstAPICommonItemResponse.getFcstValue());

    }

    private Double getCurrentWaterTemperature(LinkedList<WaterTemperatureResponse.WaterTemperatureData> waterTemperatureDataList) {
        if (ValidatorUtil.isEmpty(waterTemperatureDataList)) {
            return null;
        }

        return Double.parseDouble(waterTemperatureDataList.getLast().getWaterTemp());
    }

    private DivingIndicator getCurrentDivingIndex(List<OceanIndexPredictionResponse.IndexInfo> indexInfoList, Double latitude, Double longitude) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int noon = 12;
        if (ValidatorUtil.isEmpty(indexInfoList)) {
            return null;
        }

        String name = indexInfoList.stream().min((o1, o2) -> {
            double o1Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o1.getLat()), Double.parseDouble(o1.getLon()), latitude, longitude);
            double o2Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o2.getLat()), Double.parseDouble(o2.getLat()), latitude, longitude);

            return Double.compare(o1Distance, o2Distance);
        }).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO)).getName();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String timeOfDay = localDateTime.getHour() >= noon ? "오후" : "오전";

        OceanIndexPredictionResponse.IndexInfo indexInfo = indexInfoList.stream().filter(index ->
                index.getName().equals(name) && index.getDate().equals(date) && index.getTimeType().equals(timeOfDay)
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO));


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

    private List<WeatherVO> getForecastWeatherInfo(double latitude, double longitude, LocalDateTime now) {

        ForecastBaseDateTime forecastBaseDateTime = getForecastBaseDateTime(now);
        String forecastBaseDate = forecastBaseDateTime.getForecastBaseDate();
        String forecastBaseTime = forecastBaseDateTime.getForecastBaseTime();
        List<LocalDate> forcastDateList = getForecastDateList(now);

        ObsCode nearestObsCode = getNearestObsCode(longitude, latitude);


        if (nearestObsCode == null) {
            return null;
        }

        List<CompletableFuture<? extends BaseAPiResponse>> futures = new ArrayList<>();

        // 단기 예측
        CompletableFuture<FcstAPICommonListResponse> commonFcst = vilageFcstAPIService.getForecastInfo(nearestObsCode.getNx(), nearestObsCode.getNy(), forecastBaseDate, forecastBaseTime);
        futures.add(commonFcst);

        // 조석
        List<CompletableFuture<TidePredictionListResponse>> tidePredictionList
                = forcastDateList.stream().map(date -> khoaGoKrAPIService.getTidePrediction(nearestObsCode, getBaseDate(date))).toList();
        futures.addAll(tidePredictionList);

        // 수온
        CompletableFuture<WaterTemperatureForecastResponse> waterTemperatureForecast = khoaGoKrAPIService.getForecastWaterTemperature(nearestObsCode);
        futures.add(waterTemperatureForecast);

        //다이빙 지수
        CompletableFuture<OceanIndexPredictionResponse> divingIndex = khoaGoKrAPIService.getOceanIndexPrediction();
        futures.add(divingIndex);

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 단기 예측
        List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList = commonFcst.exceptionally(
                throwable -> null
        ).join().getFcstAPICommonItemResponseList();

        // 다이빙 지수
        List<OceanIndexPredictionResponse.IndexInfo> indexInfoList = divingIndex.exceptionally(
                throwable -> null
        ).join().getResult().getData();

        // 수온
        List<WaterTemperatureForecastResponse.WaterTemperatureData> waterTemperatureData = waterTemperatureForecast.exceptionally(
                throwable -> null
        ).join().getResult().getData();

        // 조석
        List<TidePredictionListResponse> tidePredictionListResponsesList = tidePredictionList.stream().map(
                tidePrediction -> tidePrediction.exceptionally(
                        throwable -> null
                ).join()
        ).toList();

        AtomicInteger index = new AtomicInteger(0);
        return forcastDateList.stream().map(
                date -> {
                    double averageAirTemperature = getAverageAirTemperature(fcstAPICommonItemResponseList, date);

                    double averageWindSpeed = getAverageWindSpeed(fcstAPICommonItemResponseList, date);

                    double averageWaveHeight = getAverageWaveHeight(fcstAPICommonItemResponseList, date);

                    double averageWaterTemperature = getAverageWaterTemperature(waterTemperatureData, date);

                    WeatherType weatherType = getNoonWeatherType(fcstAPICommonItemResponseList, date);

                    DivingIndicator divingIndicator = getDivingIndex(indexInfoList, latitude, longitude, date);

                    List<TideEvent> tideEvent = getTideEvent(tidePredictionListResponsesList.get(index.getAndIncrement()).getResult().getData());


                    return WeatherVO.of(
                            date,
                            weatherType,
                            averageAirTemperature,
                            averageWaterTemperature,
                            averageWindSpeed,
                            divingIndicator,
                            tideEvent,
                            averageWaveHeight
                    );
                }
        ).toList();
    }

    private double getAverageAirTemperature(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList, LocalDate date) {
        return fcstAPICommonItemResponseList.stream().filter(
                        item -> item.getFcstDate().equals(getBaseDate(date)) && item.getCategory().equals(FcstType.TMP.name())
                ).mapToDouble(item -> Double.parseDouble(item.getFcstValue())).average()
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));
    }

    private double getAverageWindSpeed(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList, LocalDate date) {
        return fcstAPICommonItemResponseList.stream().filter(
                        item -> item.getFcstDate().equals(getBaseDate(date)) && item.getCategory().equals(FcstType.WSD.name())
                ).mapToDouble(item -> Double.parseDouble(item.getFcstValue())).average()
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));
    }

    private double getAverageWaveHeight(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList, LocalDate date) {
        return fcstAPICommonItemResponseList.stream().filter(
                        item -> item.getFcstDate().equals(getBaseDate(date)) && item.getCategory().equals(FcstType.WAV.name())
                ).mapToDouble(item -> Double.parseDouble(item.getFcstValue())).average()
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));
    }

    private double getAverageWaterTemperature(List<WaterTemperatureForecastResponse.WaterTemperatureData> waterTemperatureData, LocalDate date) {
        return waterTemperatureData.stream().filter(
                        item -> item.getDate().equals(getBaseDate(date))
                ).mapToDouble(item -> Double.parseDouble(item.getTemperature())).average()
                .orElseThrow(() -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION));
    }

    private WeatherType getNoonWeatherType(List<FcstAPICommonItemResponse> fcstAPICommonItemResponseList, LocalDate date) {
        LocalDate now = LocalDate.now();
        int dateDiff = (int) ChronoUnit.DAYS.between(now, date);
        int threeDateDiff = 3;

        // 3일 이내라면 12시, 아니라면 0시
        String forecastTime = dateDiff < threeDateDiff ? "1200" : "0000";

        List<FcstAPICommonItemResponse> itemResponseList = fcstAPICommonItemResponseList.stream().filter(
                item -> item.getFcstDate().equals(getBaseDate(date)) && item.getFcstTime().equals(forecastTime)
        ).toList();

        boolean isDay = true;
        int sky = getFcstItem(fcstAPICommonItemResponseList, FcstType.SKY, forecastTime);
        int precipitation = getFcstItem(fcstAPICommonItemResponseList, FcstType.PTY, forecastTime);
        boolean isThunder = (getFcstItem(fcstAPICommonItemResponseList, FcstType.LGT, forecastTime) > 0);

        return WeatherType.getWeatherType(sky, precipitation, isThunder, isDay);
    }

    private DivingIndicator getDivingIndex(List<OceanIndexPredictionResponse.IndexInfo> indexInfoList, Double latitude, Double longitude, LocalDate date) {

        if (ValidatorUtil.isEmpty(indexInfoList)) {
            return null;
        }

        String name = indexInfoList.stream().min((o1, o2) -> {
            double o1Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o1.getLon()), Double.parseDouble(o1.getLat()), longitude, latitude);
            double o2Distance = CoordinateUtil.calculateDistance(Double.parseDouble(o2.getLon()), Double.parseDouble(o2.getLat()), longitude, latitude);

            return Double.compare(o1Distance, o2Distance);
        }).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO)).getName();

        DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String targetDate = date.toString();

        OceanIndexPredictionResponse.IndexInfo indexInfo = indexInfoList.stream().filter(index ->
                index.getName().equals(name) && index.getDate().equals(targetDate)
        ).findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_WEATHER_INFO));


        if (ValidatorUtil.isEmpty(indexInfo)) {
            return null;
        }

        return DivingIndicator.of(indexInfo.getTotalScore());
    }

    private List<TideEvent> getTideEvent(List<TidePredictionListResponse.TideData> tideEventData) {
        if (ValidatorUtil.isEmpty(tideEventData)) {
            return null;
        }
        return tideEventData.stream().map(
                tideData -> TideEvent.of(LocalDateTime.parse(tideData.getTphTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), Double.parseDouble(tideData.getTphLevel()), tideData.getHlCode())
        ).toList();

    }
}
