package com.oceans7.dib.global.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncoderUtil {
    public static String toURLEncodeUtf8(String str){
        if (str == null || str.trim().equals("")) return "";
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch(UnsupportedEncodingException ex){
            return null;
        }
    }
}
