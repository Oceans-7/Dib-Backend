package com.oceans7.dib.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextManipulatorUtil {
    private static final Pattern URL_PATTERN_IN_HTML = Pattern.compile("href=\"(.*?)\"");
    private static final Pattern URL_PATTERN = Pattern.compile("(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
    private static final Pattern TEL_PATTERN = Pattern.compile("\\d{2,3}-\\d{3,4}-\\d{4}");

    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static String extractUrl(String input) {
        Matcher matcher = URL_PATTERN_IN_HTML.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }

        matcher = URL_PATTERN.matcher(input);
        if (matcher.find()) {
            return input;
        }

        return "";
    }

    public static String extractTel(String input) {
        Matcher matcher = TEL_PATTERN.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String convertDateRangeFormat(String startDate, String endDate) {
        if(ValidatorUtil.isNotEmpty(startDate) && ValidatorUtil.isNotEmpty(endDate)) {
            LocalDate parsedStartDate = LocalDate.parse(startDate, INPUT_DATE_FORMATTER);
            LocalDate parsedEndDate = LocalDate.parse(endDate, INPUT_DATE_FORMATTER);

            String formattedStartDate = parsedStartDate.format(OUTPUT_DATE_FORMATTER);
            String formattedEndDate = parsedEndDate.format(OUTPUT_DATE_FORMATTER);

            String dateFormat = "%s : %s~%s";
            String prefix = "기간";

            return String.format(dateFormat, prefix, formattedStartDate, formattedEndDate);
        }
        return "";
    }

    public static String replaceBrWithNewLine(String input) {
        String brTag = "<br>";
        String newLine = "\n";

        if(ValidatorUtil.isNotEmpty(input)) {
            return input.replace(brTag, newLine);
        }
        return input;
    }

    public static String generateRedisKey(String... args) {
        return String.join("::", args);
    }
}
