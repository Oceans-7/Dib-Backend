package com.oceans7.dib.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {
    public static <T> boolean isEmpty(T value) {
        if(value == null) {
            return true;
        } else if(value instanceof String) {
            return ((String) value).isEmpty();
        }  else if(value instanceof List<?>) {
            return ((List<?>) value).size() == 0 ? true : false;
        } else {
            return false;
        }
    }

    public static <T> boolean isNotEmpty(T value) {
        if (value instanceof String) {
            return !((String) value).isEmpty() && !((String) value).isBlank();
        } else if(value instanceof List<?>) {
            return ((List<?>) value).size() > 0 ? true : false;
        } else {
            return value != null;
        }
    }

    public static boolean checkAvailability(String value) {
        if(isEmpty(value) || value.contains("없음") || value.contains("불가") || value.contains("금연")) {
            return false;
        } else {
            return true;
        }
    }
}
