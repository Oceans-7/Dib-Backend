package com.oceans7.dib.domain.event.service;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.CouponStatus;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CouponServiceTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponGroupRepository couponGroupRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void before() {
        testUser = userRepository.save(MockEntity.testUser());
    }

    private Event makeEvent() {
        return eventRepository.save(MockEntity.testEvent());
    }

    private CouponGroup makeCouponGroup(Event event) {
        CouponGroup couponGroup = MockEntity.testCouponGroup();
        couponGroup.setEvent(event);
        return couponGroupRepository.save(couponGroup);
    }

    @Test
    @DisplayName("쿠폰 발급 테스트")
    public void issue() {
        // given
        CouponGroup couponGroup = makeCouponGroup(makeEvent());

        // when
        couponService.issue(testUser.getId(), couponGroup.getCouponGroupId());

        // then
        Coupon issuedCoupon = couponRepository.findByUserAndCouponGroup(testUser, couponGroup).orElseThrow();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.UNUSED);
        assertThat(issuedCoupon.getIssuedDate()).isEqualTo(LocalDateTime.now().format(formatter));
    }
}
