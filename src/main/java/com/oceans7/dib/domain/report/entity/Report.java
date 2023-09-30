package com.oceans7.dib.domain.report.entity;

import com.oceans7.dib.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "organism_name", length = 60, nullable = false)
    private String organismName;

    @Column(name = "found_location", nullable = false)
    private String foundLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "report")
    private List<ReportImage> reportImageList = new ArrayList<>();

    public static Report of(String organismName, String foundLocation, User user) {
        Report report = new Report();

        report.organismName = organismName;
        report.foundLocation = foundLocation;
        report.user = user;

        return report;
    }
}
