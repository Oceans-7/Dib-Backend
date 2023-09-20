package com.oceans7.dib.domain.home.controller;

import com.oceans7.dib.domain.home.dto.response.event.EventResponseDto;
import com.oceans7.dib.domain.home.service.HomeService;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "home", description = "메인홈 콘텐츠 API")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(
            summary = "이벤트 상세 조회",
            description = "배너 이벤트 내용을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/event/{eventId}")
    public ApplicationResponse<EventResponseDto> getEventDetail(@PathVariable("eventId") Long eventId) {
        return ApplicationResponse.ok(homeService.getEventDetail(eventId));
    }
}
