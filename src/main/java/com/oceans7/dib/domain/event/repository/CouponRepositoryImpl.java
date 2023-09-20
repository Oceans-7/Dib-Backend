package com.oceans7.dib.domain.event.repository;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.oceans7.dib.domain.event.entity.QCoupon.coupon;
import static com.oceans7.dib.domain.event.entity.QCouponGroup.couponGroup;

@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Coupon> findPossibleCouponsOrderByClosingDateAsc(Long userId) {
        return queryFactory
                .selectFrom(coupon)
                .join(coupon.couponGroup, couponGroup)
                .where(coupon.user.id.eq(userId),
                        coupon.status.eq(CouponStatus.UNUSED),
                        couponGroup.closingDate.after(LocalDate.now()))
                .orderBy(couponGroup.closingDate.asc())
                .fetch();
    }

    @Override
    public Long countByPossibleCoupon(Long userId) {
        return queryFactory
                .select(coupon.countDistinct())
                .from(coupon)
                .innerJoin(coupon.couponGroup, couponGroup)
                .where(coupon.user.id.eq(userId),
                        coupon.status.eq(CouponStatus.UNUSED),
                        couponGroup.closingDate.after(LocalDate.now()))
                .fetchFirst();
    }

}
