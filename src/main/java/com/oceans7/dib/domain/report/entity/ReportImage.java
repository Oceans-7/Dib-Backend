package com.oceans7.dib.domain.report.entity;

import com.oceans7.dib.global.base_entity.BaseImageEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_image_id")
    private Long reportImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public static ReportImage of(String url) {
        ReportImage reportImage = new ReportImage();

        reportImage.setUrl(url);

        return reportImage;
    }

    public void setReport(Report report) {
        report.getReportImageList().add(this);
        this.report = report;
    }
}
