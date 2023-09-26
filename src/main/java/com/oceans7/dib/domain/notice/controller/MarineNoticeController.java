package com.oceans7.dib.domain.notice.controller;

import com.oceans7.dib.domain.notice.dto.response.NoticeResponseDto;
import com.oceans7.dib.domain.notice.service.MarineNoticeService;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "notice", description = "해양 공지 관련 API")
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class MarineNoticeController {

    private final MarineNoticeService marineNoticeService;

    @Operation(
            summary = "해양 공지 리스트 조회",
            description = "해양 공지 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping
    public ApplicationResponse<List<NoticeResponseDto>> getAllMarineNotice() {
        return ApplicationResponse.ok(marineNoticeService.getAllMarineNotice());
    }
}
