package com.oceans7.dib.domain.event.repository;

import com.oceans7.dib.domain.event.entity.CouponGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponGroupRepository extends JpaRepository<CouponGroup, Long> {
}
