package com.oceans7.dib.domain.custom_content.controller;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.dto.response.detail.DetailContentResponseDto;
import com.oceans7.dib.domain.custom_content.service.CustomContentService;
import com.oceans7.dib.global.exception.ErrorResponse;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "custom content", description = "자체 콘텐츠 관련 API")
@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class CustomContentController {

    private final CustomContentService customContentService;

    @Operation(
            summary = "자체 콘텐츠 리스트 조회",
            description = "메인 홈 상단 자체 콘텐츠 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping
    public ApplicationResponse<List<ContentResponseDto>> getAllCustomContent() {
        return ApplicationResponse.ok(customContentService.getAllCustomContent());
    }

    @Operation(
            summary = "자체 콘텐츠 상세 조회",
            description = "자체 콘텐츠 내용을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "C0001", description = "존재하지 않는 리소스 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{customContentId}")
    public ApplicationResponse<DetailContentResponseDto> getDetailCustomContent(@PathVariable("customContentId") Long customContentId) {
        return ApplicationResponse.ok(customContentService.getDetailCustomContent(customContentId));
    }

}
