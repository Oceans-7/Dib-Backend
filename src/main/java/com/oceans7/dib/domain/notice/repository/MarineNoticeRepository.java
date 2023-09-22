package com.oceans7.dib.domain.notice.repository;

import com.oceans7.dib.domain.notice.entity.MarineNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarineNoticeRepository extends JpaRepository<MarineNotice, Long> {
    List<MarineNotice> findAllByOrderByCreatedAtDesc();
}
