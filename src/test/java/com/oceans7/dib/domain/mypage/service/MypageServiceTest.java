package com.oceans7.dib.domain.mypage.service;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.CouponStatus;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.mypage.dto.response.*;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MypageServiceTest {
    @Autowired
    private MypageService mypageService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CouponGroupRepository couponGroupRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private DibRepository dibRepository;

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
        CouponGroup couponGroup = MockEntity.testCouponGroup(event);
        return couponGroupRepository.save(couponGroup);
    }

    private Coupon issueCoupon(CouponGroup couponGroup) {
        Coupon coupon = MockEntity.testCoupon(couponGroup, testUser);
        return couponRepository.save(coupon);
    }

    private Dib makeDib() {
        return dibRepository.save(MockEntity.testDib(testUser));
    }

    @Test
    @DisplayName("프로필 조회")
    public void getMyProfile() {
        // given
        issueCoupon(makeCouponGroup(makeEvent()));
        makeDib();

        // when
        MypageResponseDto response = mypageService.getMyProfile(testUser.getId());

        // then
        assertThat(response.getNickname()).isEqualTo(testUser.getNickname());
        assertThat(response.getProfileUrl()).isEqualTo(testUser.getProfileUrl());
        assertThat(response.getCouponCount()).isEqualTo(1);
        assertThat(response.getDibCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("찜 목록 조회")
    public void getMyDibs() {
        // given
        Dib dib = makeDib();

        // when
        DibResponseDto response = mypageService.getMyDibs(testUser.getId());

        assertThat(response.getCount()).isEqualTo(1);

        // then
        for(DetailDibResponseDto dibResponseDto : response.getDibList()) {
            assertThat(dibResponseDto.getContentId()).isEqualTo(dib.getContentId());
            assertThat(dibResponseDto.getContentType()).isEqualTo(ContentType.getContentTypeByCode(dib.getContentTypeId()));
            assertThat(dibResponseDto.getTitle()).isEqualTo(dib.getTitle());
            assertThat(dibResponseDto.getTel()).isEqualTo(dib.getTel());
            assertThat(dibResponseDto.getFirstImageUrl()).isEqualTo(dib.getFirstImage());
        }
    }

    @Test
    @DisplayName("쿠폰 목록 조회")
    public void getMyCoupons() {
        // given
        CouponGroup couponGroup = makeCouponGroup(makeEvent());
        Coupon coupon = issueCoupon(couponGroup);

        // when
        CouponResponseDto response = mypageService.getMyCoupons(testUser.getId());

        assertThat(response.getCount()).isEqualTo(1);

        // then
        for(DetailCouponResponseDto couponResponseDto : response.getCouponList()) {
            assertThat(couponResponseDto.getRegion()).isEqualTo(couponGroup.getRegion());
            assertThat(couponResponseDto.getCouponType()).isEqualTo(couponGroup.getCouponType().getKeyword());
            assertThat(couponResponseDto.getDiscountPercentage()).isEqualTo(couponGroup.getDiscountPercentage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            assertThat(couponResponseDto.getStartDate()).isEqualTo(couponGroup.getStartDate().format(formatter));
            assertThat(couponResponseDto.getClosingDate()).isEqualTo(couponGroup.getClosingDate().format(formatter));

            assertThat(couponResponseDto.isUsed()).isEqualTo(coupon.getStatus() == CouponStatus.USED ? true : false);
            assertThat(couponResponseDto.getCouponId()).isEqualTo(coupon.getCouponId());

            Long remainingDays = Duration.between(
                    couponGroup.getStartDate().atStartOfDay(), couponGroup.getClosingDate().atStartOfDay())
                    .toDays();

            assertThat(couponResponseDto.getRemainingDays()).isEqualTo(remainingDays);
        }
    }

    @Test
    @DisplayName("프로필 정보 수정")
    public void updateMyProfile() {
        // given
        UpdateProfileRequestDto updateProfileReq = MockRequest.testUpdateProfileReq();

        // when
        mypageService.updateMyProfile(testUser.getId(), updateProfileReq);

        // then
        User updateUser = userRepository.findById(testUser.getId()).orElseThrow();

        assertThat(updateUser.getNickname()).isEqualTo(updateProfileReq.getNickname());
        assertThat(updateUser.getProfileUrl()).isEqualTo(updateProfileReq.getImageUrl());
    }
}
