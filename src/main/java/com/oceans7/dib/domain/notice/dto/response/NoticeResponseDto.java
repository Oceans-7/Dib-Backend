package com.oceans7.dib.domain.notice.dto.response;

import com.oceans7.dib.domain.notice.entity.MarineNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponseDto {
    private Long noticeId;

    private String title;

    private String createDate;

    private String createTime;

    private String content;

    public static NoticeResponseDto from(MarineNotice marineNotice) {
        NoticeResponseDto noticeResponseDto = new NoticeResponseDto();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        noticeResponseDto.noticeId = marineNotice.getNoticeId();
        noticeResponseDto.title = String.format("[%s] %s", marineNotice.getCategory(), marineNotice.getTitle());
        noticeResponseDto.createDate = marineNotice.getCreatedAt().format(dateFormatter);
        noticeResponseDto.createTime = marineNotice.getCreatedAt().format(timeFormatter);
        noticeResponseDto.content = marineNotice.getContent();

        return noticeResponseDto;
    }
}
