package com.oceans7.dib.domain.report.controller;

import com.oceans7.dib.domain.report.dto.request.ReportRequestDto;
import com.oceans7.dib.domain.report.service.ReportService;
import com.oceans7.dib.global.exception.ErrorResponse;
import com.oceans7.dib.global.response.ApplicationResponse;
import com.oceans7.dib.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "report", description = "유해 생물 신고 관련 API")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "유해 생물 신고",
            description = "유해 생물을 신고한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "C0001", description = "존재하지 않는 리소스 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "C0002", description = "올바르지 않은 요청 값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "R0000", description = "이미지 개수가 최대 허용치를 초과했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/report")
    public ApplicationResponse reportHarmfulOrganism(@Valid @RequestBody ReportRequestDto request) {
        reportService.report(SecurityUtil.getCurrentUsername().get(), request);
        return ApplicationResponse.ok();
    }
}
