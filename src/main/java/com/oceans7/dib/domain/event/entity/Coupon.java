package com.oceans7.dib.domain.event.entity;

import com.oceans7.dib.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_group_id", nullable = false)
    private CouponGroup couponGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    public static Coupon of(LocalDate issuedDate, CouponStatus status, CouponGroup couponGroup, User user) {
        Coupon coupon = new Coupon();

        coupon.issuedDate = issuedDate;
        coupon.status = status;
        coupon.setCouponGroup(couponGroup);
        coupon.setUser(user);

        return coupon;
    }

    private void setCouponGroup(CouponGroup couponGroup) {
        this.couponGroup = couponGroup;
        couponGroup.getCouponList().add(this);
    }

    private void setUser(User user) {
        this.user = user;
        user.getCouponList().add(this);
    }
}
