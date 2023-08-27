package com.oceans7.dib.domain.event.controller;

import com.oceans7.dib.domain.event.dto.response.CouponResponseDto;
import com.oceans7.dib.domain.event.service.CouponService;
import com.oceans7.dib.global.exception.ErrorResponse;
import com.oceans7.dib.global.response.ApplicationResponse;
import com.oceans7.dib.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "coupon", description = "쿠폰 조회/발급 API")
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(
            summary = "쿠폰 발급",
            description = "쿠폰 ID를 입력받고 쿠폰을 발급한다. 쿠폰을 이미 발급받은 경우 예외를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "E0000", description = "이미 발급된 쿠폰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/{couponGroupId}")
    public ApplicationResponse issueCoupon(@PathVariable("couponGroupId") Long couponGroupId) {
        couponService.issue(SecurityUtil.getCurrentUsername().get(), couponGroupId);
        return ApplicationResponse.ok();
    }

    @Operation(
            summary = "쿠폰 조회",
            description = "사용자가 발급했던 쿠폰 이력을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping
    public ApplicationResponse<CouponResponseDto> getCoupon() {
        return ApplicationResponse.ok(couponService.getCoupon(SecurityUtil.getCurrentUsername().get()));
    }
}
