package com.oceans7.dib.domain.event.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "coupon_type")
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(name = "check_code", length = 4)
    private String checkCode;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "coupon_image_url", length = 2100)
    private String couponImageUrl;

    @Column(name = "partner_image_url", length = 2100)
    private String partnerImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "couponGroup")
    private List<Coupon> couponList = new ArrayList<>();

    public static CouponGroup of(String name, String region, CouponType couponType, String checkCode,
                                 int discountPercentage, LocalDate startDate, LocalDate closingDate, String couponImageUrl, String partnerImageUrl, Event event) {
        CouponGroup couponGroup = new CouponGroup();
        couponGroup.name = name;
        couponGroup.region = region;
        couponGroup.couponType = couponType;
        couponGroup.checkCode = checkCode;
        couponGroup.discountPercentage = discountPercentage;
        couponGroup.startDate = startDate;
        couponGroup.closingDate = closingDate;
        couponGroup.couponImageUrl = couponImageUrl;
        couponGroup.partnerImageUrl = partnerImageUrl;
        couponGroup.setEvent(event);

        return couponGroup;
    }

    private void setEvent(Event event) {
        this.event = event;
        event.getCouponGroupList().add(this);
    }
}
