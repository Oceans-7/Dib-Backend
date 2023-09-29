package com.oceans7.dib.domain.notice.service;

import com.oceans7.dib.domain.notice.dto.response.NoticeResponseDto;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.notice.repository.MarineNoticeRepository;
import com.oceans7.dib.global.MockEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MarineNoticeServiceTest {
    @Autowired
    private MarineNoticeService marineNoticeService;

    @Autowired
    private MarineNoticeRepository marineNoticeRepository;

    private MarineNotice makeMarineNotice() {
        return marineNoticeRepository.save(MockEntity.testMarineNotice());
    }

    @Test
    @DisplayName("해양 공지 리스트 조회")
    public void getAllMarineNotice() {
        // given
        MarineNotice marineNotice = makeMarineNotice();

        // when
        List<NoticeResponseDto> response = marineNoticeService.getAllMarineNotice();

        // then
        assertThat(response.get(0).getNoticeId()).isEqualTo(marineNotice.getNoticeId());
        assertThat(response.get(0).getTitle()).isEqualTo(String.format("[%s] %s", marineNotice.getCategory(), marineNotice.getTitle()));
        assertThat(response.get(0).getCreateDateTime()).isEqualTo(marineNotice.getCreatedAt());
    }
}
