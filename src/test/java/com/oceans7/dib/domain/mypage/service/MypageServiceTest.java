package com.oceans7.dib.domain.mypage.service;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
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
import com.oceans7.dib.global.MockResponse;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Autowired
    private ImageAssetUrlProcessor imageAssetUrlProcessor;

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
    @DisplayName("[exception] 사용자를 찾지 못함 테스트")
    public void getMyProfileNotFoundUserException() {
        // given
        Long notFoundUserId = testUser.getId() + 1;

        // when, then
        assertThrows(ApplicationException.class, () -> mypageService.getMyProfile(notFoundUserId));

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
            assertThat(dibResponseDto.isDib()).isEqualTo(true);
        }
    }

    @Test
    @DisplayName("찜 목록 조회 : 찜이 하나도 없는 경우")
    public void getMyDibsIfNoDib() {
        // when
        DibResponseDto response = mypageService.getMyDibs(testUser.getId());

        // then
        assertThat(response.getCount()).isEqualTo(0);
        assertThat(response.getDibList()).isNull();
    }

    @Test
    @DisplayName("쿠폰 목록 조회")
    public void getMyCoupons() {
        // given
        CouponGroup couponGroup = makeCouponGroup(makeEvent());
        Coupon coupon = issueCoupon(couponGroup);

        // when
        CouponResponseDto response = mypageService.getMyCoupons(testUser.getId());

        // then
        CouponResponseDto mockResponse = MockResponse.testCouponRes(coupon);
        assertThat(response.getCount()).isEqualTo(mockResponse.getCount());
        for(int i = 0; i < response.getCount(); i++) {
            assertThat(response.getCouponList().get(i).getCouponId()).isEqualTo(mockResponse.getCouponList().get(i).getCouponId());
            assertThat(response.getCouponList().get(i).getCouponType()).isEqualTo(mockResponse.getCouponList().get(i).getCouponType());
            assertThat(response.getCouponList().get(i).getRegion()).isEqualTo(mockResponse.getCouponList().get(i).getRegion());
            assertThat(response.getCouponList().get(i).getCouponImageUrl()).isEqualTo(mockResponse.getCouponList().get(i).getCouponImageUrl());
            assertThat(response.getCouponList().get(i).getStartDate()).isEqualTo(mockResponse.getCouponList().get(i).getStartDate());
            assertThat(response.getCouponList().get(i).getClosingDate()).isEqualTo(mockResponse.getCouponList().get(i).getClosingDate());
            assertThat(response.getCouponList().get(i).getRemainingDays()).isEqualTo(mockResponse.getCouponList().get(i).getRemainingDays());
            assertThat(response.getCouponList().get(i).getDiscountPercentage()).isEqualTo(mockResponse.getCouponList().get(i).getDiscountPercentage());
        }
    }

    @Test
    @DisplayName("쿠폰 목록 조회 : 쿠폰이 하나도 없는 경우")
    public void getMyCouponsIfNoCoupon() {
        // when
        CouponResponseDto response = mypageService.getMyCoupons(testUser.getId());

        // then
        assertThat(response.getCount()).isEqualTo(0);
        assertThat(response.getCouponList()).isNull();
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
        assertThat(updateUser.getProfileUrl()).isEqualTo(imageAssetUrlProcessor.extractUrlPath(updateProfileReq.getImageUrl()));
    }
}
