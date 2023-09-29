package com.oceans7.dib.domain.report.reposiroty;

import com.oceans7.dib.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
