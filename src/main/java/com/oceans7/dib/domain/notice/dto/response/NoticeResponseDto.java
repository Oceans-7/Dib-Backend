package com.oceans7.dib.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponseDto {
    @Schema(description = "해양 공지 아이디", example = "0")
    private Long noticeId;

    @Schema(description = "제목", example = "[특보] 보름달물해파리 경남 주의단계")
    private String title;

    @Schema(description = "생성 날짜/시각", example = "2023.06.29∙12:00")
    @JsonFormat(pattern = "yyyy.MM.dd ∙ HH:mm")
    private LocalDateTime createDateTime;

    @Schema(description = "내용", example = "안녕하세요, DIB 입니다.\n\n6월 22일부터 28일 간 총 16건의 해파리 웹 신고가 들어왔으며, 그 중 8건이 보름달물해파리였습니다. 해파리 발견 지역은 강원 2건, 경남 7건, 경북 1건, 전남 5건, 전북 1건, 제주 2건이었습니다.")
    private String content;

    public static NoticeResponseDto from(MarineNotice marineNotice) {
        NoticeResponseDto noticeResponseDto = new NoticeResponseDto();

        noticeResponseDto.noticeId = marineNotice.getNoticeId();
        noticeResponseDto.title = String.format("[%s] %s", marineNotice.getCategory(), marineNotice.getTitle());
        noticeResponseDto.createDateTime = marineNotice.getCreatedAt();
        noticeResponseDto.content = marineNotice.getContent();

        return noticeResponseDto;
    }
}
