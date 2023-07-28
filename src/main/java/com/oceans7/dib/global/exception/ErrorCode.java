package com.oceans7.dib.global.exception;

import lombok.AllArgsConstructor;
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
    NOT_VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "C0005", "유효성 검사를 실패했습니다."),

    // Open API
    NOT_FOUND_ITEM_EXCEPTION(HttpStatus.BAD_REQUEST, "O0000", "관광 정보 검색 결과가 없습니다."),
    SOCKET_TIMEOUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "O0001", "Open API 서버 연결에 실패하였습니다."),
    NOT_FOUNT_USER_LOCATION(HttpStatus.BAD_REQUEST, "O0002", "사용자 위치를 찾을 수 없습니다."),
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
