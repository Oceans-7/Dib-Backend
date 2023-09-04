package com.oceans7.dib.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "C0000", "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "C0001", "존재하지 않는 리소스 요청입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "C0002", "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "C0003", "해당 요청에 대한 권한이 없습니다."),
    TIME_OUT_EXCEPTION(HttpStatus.BAD_REQUEST, "C0004", "트래픽이 초과되었으므로 잠시 후에 다시 요청해주세요."),
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "C0005", "파일 업로드를 실패했습니다."),

    // Open API
    NOT_FOUND_ITEM_EXCEPTION(HttpStatus.BAD_REQUEST, "O0000", "관광 정보 검색 결과가 없습니다."),
    SOCKET_TIMEOUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "O0001", "Open API 서버 연결에 실패하였습니다."),
    NOT_FOUNT_USER_LOCATION(HttpStatus.BAD_REQUEST, "O0002", "사용자 위치를 찾을 수 없습니다."),

    // Place
    NOT_FOUNT_AREA_NAME(HttpStatus.BAD_REQUEST, "P0000", "지역명을 찾을 수 없습니다."),

    // Auth
    TOKEN_VERIFICATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "A0000", "토큰 검증에 실패했습니다."),
    OPENKEY_NOT_MATCHED(HttpStatus.BAD_REQUEST, "A0001", "일치하는 공개키를 찾을 수 없습니다."),
    NONCE_NOT_MATCHED(HttpStatus.BAD_REQUEST, "A0002", "nonce값이 일치하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "A0003", "인증정보를 찾을 수 없습니다."),

    // Event
    ALREADY_ISSUED_EXCEPTION(HttpStatus.BAD_REQUEST, "E0000", "이미 발급된 쿠폰입니다."),

    // Mypage
    ALREADY_USED_NICKNAME_EXCEPTION(HttpStatus.BAD_REQUEST, "M0000", "이미 사용중인 닉네임입니다."),

    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
