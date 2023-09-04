package com.oceans7.dib.global.util;

public final class CommonUtil {

    public static final String BEARER_PREFIX = "Bearer ";


    public static String parseTokenFromBearer(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }
}
