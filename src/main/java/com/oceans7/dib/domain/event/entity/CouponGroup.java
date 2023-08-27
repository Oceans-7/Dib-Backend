package com.oceans7.dib.domain.event.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_group_id")
    private Long couponGroupId;

    @Column(name = "name")
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "category")
    private String category;

    @Column(name = "check_code", length = 4)
    private String checkCode;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;
}
