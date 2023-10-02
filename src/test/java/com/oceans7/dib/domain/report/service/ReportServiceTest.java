package com.oceans7.dib.domain.report.service;

import com.oceans7.dib.domain.report.entity.Report;
import com.oceans7.dib.domain.report.reposiroty.ReportRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockRequest;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ImageAssetUrlProcessor imageAssetUrlProcessor;

    private User testUser;

    @BeforeEach
    public void before() {
        testUser = userRepository.save(MockEntity.testUser());
    }

    @Test
    @DisplayName("해양 생물 신고")
    public void report() {
        // when
        reportService.report(testUser.getId(), MockRequest.testReportReq());

        // then
        Report report = reportRepository.findAll().get(0);
        assertThat(report.getUser().getNickname()).isEqualTo(testUser.getNickname());
        assertThat(report.getOrganismName()).isEqualTo(MockRequest.testReportReq().getOrganismName());
        assertThat(report.getFoundLocation()).isEqualTo(MockRequest.testReportReq().getFoundLocation());
        for(int i = 0; i < report.getReportImageList().size(); i++) {
            assertThat(report.getReportImageList().get(i).getUrl())
                    .isEqualTo(imageAssetUrlProcessor.extractUrlPath(MockRequest.testReportReq().getImageUrlList().get(i)));
        }
    }
}
