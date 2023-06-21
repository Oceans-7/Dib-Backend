package com.oceans7.dib.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.openapi.dto.response.LocationBasedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class TourAPIService extends AbstractOpenAPIService {

    @Value("${open-api.data-go-kr.service-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.callback-url}")
    private String callbackUrl;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.mobile-app}")
    private String mobileApp;

    // 위치 기반 서비스 API 호출
    public String fetchDataFromLocationBasedApi(double mapX, double mapY,
                                                String contentTypeId) {
        String api = "/locationBasedList1";
        int radius = 20000;

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&mapX=" + mapX +
                "&mapY=" + mapY +
                "&radius=" + radius +
                "&contentTypeId=" + contentTypeId;

        try {
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

            if (stream != null) {
                stream.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    @Override
    LocationBasedList parsingJsonObject(String json) {
        LocationBasedList result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(json, LocationBasedList.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
