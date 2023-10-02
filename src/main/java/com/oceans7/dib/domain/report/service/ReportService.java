package com.oceans7.dib.domain.report.service;

import com.oceans7.dib.domain.report.dto.request.ReportRequestDto;
import com.oceans7.dib.domain.report.entity.Report;
import com.oceans7.dib.domain.report.entity.ReportImage;
import com.oceans7.dib.domain.report.reposiroty.ReportImageRepository;
import com.oceans7.dib.domain.report.reposiroty.ReportRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportImageRepository reportImageRepository;

    private final ImageAssetUrlProcessor imageAssetUrlProcessor;

    private final static int MAX_IMAGE_LIMIT = 3;

    @Transactional
    public void report(Long userId, ReportRequestDto request) {
        validateImageLimit(request.getImageUrlList());

        Report report = createReport(request, getUserById(userId));

        createReportImages(report, request.getImageUrlList());
    }

    private void validateImageLimit(List<String> imageUrlList) {
        if(imageUrlList.size() > MAX_IMAGE_LIMIT) {
            throw new ApplicationException(ErrorCode.IMAGE_LIMIT_EXCEEDED);
        }
    }
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    private Report createReport(ReportRequestDto request, User user) {
        return reportRepository.save(Report.of(
                request.getOrganismName(),
                request.getFoundLocation(),
                user)
        );
    }

    private void createReportImages(Report report, List<String> imageUrlList) {
        List<ReportImage> reportImageList = imageUrlList.stream()
                .map(imageUrl -> ReportImage.of(imageAssetUrlProcessor.extractUrlPath(imageUrl)))
                .collect(Collectors.toList());

        reportImageList.forEach(reportImage -> {
            reportImage.setReport(report);
            reportImageRepository.save(reportImage);
        });
    }

}
