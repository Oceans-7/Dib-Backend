package com.oceans7.dib.openapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public abstract class AbstractOpenAPIService {

    String connectApi(String urlStr) {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;

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

    /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
    InputStream getNetworkConnection(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setConnectTimeout(3000);
        urlConnection.setReadTimeout(3000);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);

        try {
            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_EXCEPTION);
            }
        } catch (SocketTimeoutException e) {
            throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_EXCEPTION);
        }


        return urlConnection.getInputStream();
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    String readStreamToString(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String readLine;
        while((readLine = br.readLine()) != null) {
            result.append(readLine + "\n\r");
        }

        br.close();

        return result.toString();
    }

    /* json 형태의 문자열을 valueType.class로 parsing 후 반환 */
    <T> T parsingJsonObject(String json, Class<T> valueType) {
        T result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(json, valueType);
        } catch (ValueInstantiationException e) {
            throw new ApplicationException(ErrorCode.INVALID_USER_LOCATION_EXCEPTION);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
