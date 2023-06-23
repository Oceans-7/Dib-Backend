package com.oceans7.dib.global.util;

public class ValidatorUtil {
    public static <T> boolean isEmpty(T value) {
        if(value == null) {
            return true;
        } else if(value instanceof String) {
            return ((String) value).isEmpty();
        } else {
            return false;
        }
    }

    public static <T> boolean isNotEmpty(T value) {
        if (value instanceof String) {
            return !((String) value).isEmpty() && !((String) value).isBlank();
        } else {
            return value != null;
        }
    }
}
