package com.oceans7.dib.global.api.service;

import com.oceans7.dib.domain.weather.WaterTemperatureObsCode;
import com.oceans7.dib.domain.weather.dto.ObsCode;
import com.oceans7.dib.global.api.http.KhoaDataType.KhoaDataType;
import com.oceans7.dib.global.api.http.KhoaGoKrApi;
import com.oceans7.dib.global.api.response.khoaGoKr.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KhoaGoKrAPIService extends OpenAPIService{

    private final KhoaGoKrApi khoaGoKrApi;

    @Value("${open-api.khoa-go-kr.service-key}")
    private String serviceKey;

    @Async
    public CompletableFuture<TidePredictionListResponse> getTidePrediction(ObsCode obsCode, String date) {
        String resultType = "json";
        String result = khoaGoKrApi.getValue(KhoaDataType.tideObsPreTab.getValue(), serviceKey, obsCode.getValue(), date, resultType);

        return CompletableFuture.completedFuture(parsingJsonObject(result, TidePredictionListResponse.class));
    }

    @Async
    public CompletableFuture<WaterTemperatureResponse> getCurrentWaterTemperature(WaterTemperatureObsCode obsCode, String date) {
        String dataType = "json";
        String result = khoaGoKrApi.getValue(KhoaDataType.tideObsTemp.getValue(), serviceKey, obsCode.getValue(), date, dataType);

        return CompletableFuture.completedFuture(parsingJsonObject(result, WaterTemperatureResponse.class));
    }

    @Async
    public CompletableFuture<WaterTemperatureForecastResponse> getForecastWaterTemperature(ObsCode obsCode) {
        String dataType = "json";
        String result = khoaGoKrApi.getValue(KhoaDataType.romsTemp.getValue(), serviceKey, obsCode.getX(), obsCode.getY(), dataType);

        return CompletableFuture.completedFuture(parsingJsonObject(result, WaterTemperatureForecastResponse.class));
    }

    @Async
    public CompletableFuture<GetCurrentWaveHeightResponse> getCurrentWaveHeight(ObsCode obsCode, String date) {
        String resultType = "json";
        String result = khoaGoKrApi.getValue(KhoaDataType.obsWaveHight.getValue(), serviceKey, obsCode.getValue(), date, resultType);

        return CompletableFuture.completedFuture(parsingJsonObject(result, GetCurrentWaveHeightResponse.class));
    }

    @Async
    public CompletableFuture<OceanIndexPredictionResponse> getOceanIndexPrediction() {
        String resultType = "json";
        String indexType = "SS";
        String result = khoaGoKrApi.getValue(KhoaDataType.fcIndexOfType.getValue(), serviceKey, indexType, resultType);

        return CompletableFuture.completedFuture(parsingJsonObject(result, OceanIndexPredictionResponse.class));
    }


}
