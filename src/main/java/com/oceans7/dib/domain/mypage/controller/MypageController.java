package com.oceans7.dib.domain.mypage.controller;

import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.mypage.dto.response.DibResponseDto;
import com.oceans7.dib.domain.mypage.dto.response.MypageResponseDto;
import com.oceans7.dib.domain.mypage.service.MypageService;
import com.oceans7.dib.global.exception.ErrorResponse;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "mypage", description = "마이페이지 API")
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @Operation(
            summary = "사용자 정보 조회",
            description = "사용자의 프로필 사진, 닉네임, 쿠폰과 찜 수를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "C0001", description = "존재하지 않는 리소스 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    public ApplicationResponse<MypageResponseDto> getMyProfile() {
        return ApplicationResponse.ok(mypageService.getMyProfile((long)1));
    }

    @Operation(
            summary = "사용자 찜하기 목록 조회",
            description = "사용자의 찜하기 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "C0001", description = "존재하지 않는 리소스 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/dib")
    public ApplicationResponse<List<DibResponseDto>> getMyDibs() {
        return ApplicationResponse.ok(mypageService.getMyDibs((long)1));
    }

    @Operation(
            summary = "사용자 프로필 수정",
            description = "사용자의 프로필 정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "C0005", description = "파일 업로드를 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "M0000", description = "이미 사용중인 닉네임입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping
    public ApplicationResponse updateMyProfile(@ModelAttribute UpdateProfileRequestDto updateProfileRequestDto) {
        mypageService.updateMyProfile((long)1, updateProfileRequestDto);
        return ApplicationResponse.ok();
    }
}
