package com.oceans7.dib.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventResponseDto {
    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId;

    @Schema(description = "배너 이미지 URL", example = "https://picsum.photos/360/530")
    private String bannerImageUrl;

    public static EventResponseDto of(Long eventId, String bannerImageUrl) {
        EventResponseDto eventResponse = new EventResponseDto();

        eventResponse.eventId = eventId;
        eventResponse.bannerImageUrl = bannerImageUrl;

        return eventResponse;
    }
}
