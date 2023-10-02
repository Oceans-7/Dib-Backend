package com.oceans7.dib.global.util;

public final class CommonUtil {

    public static final String BEARER_PREFIX = "Bearer ";


    public static String parseTokenFromBearer(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }

    public static Double round(Double value, int precision) {
        if (value == null) {
            return null;
        }
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
