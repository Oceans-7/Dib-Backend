package com.oceans7.dib.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oceans7.dib.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResponse<T> {
    @Schema(description = "Response code", example = "SUCCESS")
    private String code;

    @Schema(description = "Response message", example = "성공")
    private String message;

    @Schema(description = "Response data")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private T data;

    public static ApplicationResponse<Void> ok() {
        return new ApplicationResponse<>("SUCCESS", "성공", null);
    }

    public static <T> ApplicationResponse<T> ok(T data) {
        return new ApplicationResponse<>("SUCCESS", "성공", data);
    }

    public static ApplicationResponse<ErrorCode> error(ErrorCode errorCode) {
        return new ApplicationResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ApplicationResponse<ErrorCode> error(ErrorCode errorCode, String message) {
        return new ApplicationResponse<>(errorCode.getCode(), message, null);
    }
}
