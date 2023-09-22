package com.oceans7.dib.domain.notice.service;

import com.oceans7.dib.domain.notice.dto.response.NoticeResponseDto;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.notice.repository.MarineNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarineNoticeService {

    private final MarineNoticeRepository marineNoticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getAllMarineNotice() {
        List<MarineNotice> marineNoticeList = marineNoticeRepository.findAllByOrderByCreatedAtDesc();

        return marineNoticeList.stream().map(NoticeResponseDto :: from).collect(Collectors.toList());
    }
}
