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

    @Column(name = "main_color", length = 10)
    private String mainColor;

    @Column(name = "sub_color", length = 10)
    private String subColor;

    @Column(name = "first_image_url", length = 2100,  nullable = false)
    private String firstImageUrl;

    @Column(name = "banner_image_url", length = 2100,  nullable = false)
    private String bannerImageUrl;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponGroup> couponGroupList;

    public static Event of(String mainColor, String subColor, String firstImageUrl, String bannerImageUrl) {
        Event event = new Event();

        event.mainColor = mainColor;
        event.subColor = subColor;
        event.firstImageUrl = firstImageUrl;
        event.bannerImageUrl = bannerImageUrl;
        event.couponGroupList = new ArrayList<>();

        return event;
    }
}
