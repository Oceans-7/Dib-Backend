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

    @Column(name = "use_status")
    @Enumerated(EnumType.STRING)
    private UseStatus useStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_group_id", nullable = false)
    private CouponGroup couponGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    public static Coupon of(LocalDate issuedDate, CouponGroup couponGroup, User user, UseStatus useStatus) {
        Coupon coupon = new Coupon();
        coupon.issuedDate = issuedDate;
        coupon.couponGroup = couponGroup;
        coupon.user = user;
        coupon.useStatus = useStatus;

        return coupon;
    }
}
