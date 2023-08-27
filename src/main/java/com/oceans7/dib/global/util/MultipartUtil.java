package com.oceans7.dib.global.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class MultipartUtil {
    private static final String BASE_DIR = "images";

    /**
     * 로컬에서의 사용자 홈 디렉토리 경로를 반환
     */
    public static String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * 파일 고유 ID를 생성
     * @return 36자리의 UUID
     */
    public static String createFileId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Multipart 의 ContentType 값에서 / 이후 확장자 추출
     * @param contentType ex) image/png
     * @return ex) png
     */
    public static String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }

    /**
     * 파일의 전체 경로를 생성
     * @param fileId 생성된 파일 고유 ID
     * @param format 확장자
     */
    public static String createPath(String fileId, String format) {
        return String.format("%s/%s.%s", BASE_DIR, fileId, format);
    }

    /**
     * 파일의 전체 경로를 반환
     */
    public static String getPath(MultipartFile file) {
        String fileId = createFileId();
        String format = getFormat(file.getContentType());
        return createPath(fileId, format);
    }

    public static String getContentType(MultipartFile file) {
        String format = getFormat(file.getContentType());

        switch (format) {
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "txt":
                return "text/plain";
            case "csv":
                return "text/csv";
        }
        return null;
    }
}
