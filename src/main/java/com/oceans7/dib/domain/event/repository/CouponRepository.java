package com.oceans7.dib.domain.event.repository;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom{
    Optional<Coupon> findByUserAndCouponGroup(User user, CouponGroup couponGroup);
}
