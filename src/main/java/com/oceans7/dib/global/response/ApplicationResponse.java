package com.oceans7.dib.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResponse<T> {
    @Schema(description = "Response code", example = "200")
    private String code;

    @Schema(description = "Response message", example = "요청 성공")
    private String message;

    @Schema(description = "Response data")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private T data;

    public static ApplicationResponse<String> ok() {
        return new ApplicationResponse<>("200", "요청 성공", "");
    }

    public static <T> ApplicationResponse<T> ok(T data) {
        return new ApplicationResponse<>("200", "요청 성공", data);
    }

}
