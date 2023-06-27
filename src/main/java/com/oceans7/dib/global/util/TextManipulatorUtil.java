package com.oceans7.dib.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextManipulatorUtil {
    private static final Pattern URL_PATTERN = Pattern.compile("href=\"(.*?)\"");
    private static final Pattern TEL_PATTERN = Pattern.compile("\\d{2,3}-\\d{3,4}-\\d{4}");

    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static String extractFirstUrl(String input) {
        Matcher matcher = URL_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String extractTel(String input) {
        Matcher matcher = TEL_PATTERN.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String concatenateStrings(String str1, String str2, String prefix) {
        return str1 + prefix + str2;
    }

    public static String concatenateStrings(String str1, String str2, String prefix1, String prefix2) {
        return prefix1 + str1 + prefix2 + str2;
    }

    public static String convertDateRangeFormat(String startDate, String endDate) {
        LocalDate parsedStartDate = LocalDate.parse(startDate, INPUT_DATE_FORMATTER);
        LocalDate parsedEndDate = LocalDate.parse(endDate, INPUT_DATE_FORMATTER);

        String formattedStartDate = parsedStartDate.format(OUTPUT_DATE_FORMATTER);
        String formattedEndDate = parsedEndDate.format(OUTPUT_DATE_FORMATTER);

        return concatenateStrings(formattedStartDate, formattedEndDate, "~");
    }

    public static String replaceBrWithNewLine(String input) {
        return input.replace("<br>", "\n");
    }
}
