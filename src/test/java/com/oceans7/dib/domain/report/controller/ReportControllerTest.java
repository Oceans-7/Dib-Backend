package com.oceans7.dib.domain.report.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.domain.report.entity.Report;
import com.oceans7.dib.domain.report.entity.ReportImage;
import com.oceans7.dib.domain.report.reposiroty.ReportImageRepository;
import com.oceans7.dib.domain.report.reposiroty.ReportRepository;
import com.oceans7.dib.domain.report.service.ReportService;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportImageRepository reportImageRepository;

    @Autowired
    ObjectMapper objectMapper;

    private final String TEST_USER_ID = "1";

    private User makeUser() {
        User user = MockEntity.testUser();

        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.save(user)).thenReturn(user);
        return userRepository.save(user);
    }

    private Report makeReport(User user) {
        Report report = MockEntity.testReport(user);

        ReflectionTestUtils.setField(report, "reportId", 1L);

        when(reportRepository.save(report)).thenReturn(report);
        return reportRepository.save(report);
    }

    private List<ReportImage> makeReportImage(Report report) {
        List<ReportImage> reportImageList = MockEntity.testReportImage();

        for(int i = 0; i < reportImageList.size(); i++) {
            reportImageList.get(i).setReport(report);
            ReflectionTestUtils.setField(reportImageList.get(i), "reportImageId", (long) (i+1));
        }
        when(reportImageRepository.saveAllAndFlush(reportImageList)).thenReturn(reportImageList);
        return reportImageRepository.saveAllAndFlush(reportImageList);
    }

    @Test
    @DisplayName("신고")
    @WithMockUser(username = TEST_USER_ID, roles = "USER")
    public void report() throws Exception {
        // given
        User user = makeUser();
        Report report = makeReport(user);
        List<ReportImage> reportImageList = makeReportImage(report);
        String content = objectMapper.writeValueAsString(MockRequest.testReportReq());

        // when
        ResultActions result = mvc.perform(post("/report")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        result.andExpect(status().isOk());
    }
}
