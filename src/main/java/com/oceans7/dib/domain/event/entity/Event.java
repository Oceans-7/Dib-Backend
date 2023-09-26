package com.oceans7.dib.domain.event.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "main_color")
    private String mainColor;

    @Column(name = "sub_color")
    private String subColor;

    @Column(name = "banner_url", length = 2100)
    private String bannerUrl;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponGroup> couponGroups;

    public static Event of(String mainColor, String subColor, String bannerUrl) {
        Event event = new Event();

        event.mainColor = mainColor;
        event.subColor = subColor;
        event.bannerUrl = bannerUrl;
        event.couponGroups = new ArrayList<>();

        return event;
    }

    public void addCouponGroup(final CouponGroup couponGroup) {
        couponGroups.add(couponGroup);
        couponGroup.setEvent(this);
    }

    public void removeCouponGroup(final CouponGroup couponGroup) {
        couponGroups.remove(couponGroup);
        couponGroup.setEvent(null);
    }
}
