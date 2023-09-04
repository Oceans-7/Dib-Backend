package com.oceans7.dib.domain.event.repository;

import com.oceans7.dib.domain.event.entity.Coupon;

import java.util.List;

public interface CouponRepositoryCustom {
    List<Coupon> findPossibleCouponsOrderByClosingDateAsc(Long userId);
    Long countByPossibleCoupon(Long userId);
}
