package com.oceans7.dib.domain.notice.controller;

import com.oceans7.dib.domain.notice.dto.response.NoticeResponseDto;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.notice.repository.MarineNoticeRepository;
import com.oceans7.dib.domain.notice.service.MarineNoticeService;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarineNoticeController.class)
public class MarineNoticeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MarineNoticeService marineNoticeService;

    @MockBean
    private MarineNoticeRepository marineNoticeRepository;

    private MarineNotice makeMarineNotice() {
        MarineNotice marineNotice = MockEntity.testMarineNotice();

        ReflectionTestUtils.setField(marineNotice, "noticeId", 1L);
        ReflectionTestUtils.setField(marineNotice, "createdAt", LocalDateTime.now());

        when(marineNoticeRepository.save(marineNotice)).thenReturn(marineNotice);
        return marineNoticeRepository.save(marineNotice);
    }

    @Test
    @DisplayName("해양 공지 조회 테스트")
    @WithMockUser("user1")
    public void getAllMarineNotice() throws Exception {
        // given
        MarineNotice marineNotice = makeMarineNotice();
        List<NoticeResponseDto> mockResponse = MockResponse.testMarineNoticeRes(marineNotice);
        when(marineNoticeService.getAllMarineNotice())
                .thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/notice"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].noticeId").value(mockResponse.get(0).getNoticeId()))
                .andExpect(jsonPath("$.data[0].title").value(mockResponse.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].createDate").value(mockResponse.get(0).getCreateDate()))
                .andExpect(jsonPath("$.data[0].createTime").value(mockResponse.get(0).getCreateTime()))
                .andExpect(jsonPath("$.data[0].content").value(mockResponse.get(0).getContent()));
    }
}
