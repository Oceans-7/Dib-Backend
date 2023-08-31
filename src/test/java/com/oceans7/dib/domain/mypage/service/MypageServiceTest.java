package com.oceans7.dib.domain.mypage.service;

import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.mypage.dto.response.DibResponseDto;
import com.oceans7.dib.domain.mypage.dto.response.MypageResponseDto;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MypageServiceTest {
    @Autowired
    MypageService mypageService;

    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    DibRepository dibRepository;

    @Autowired
    UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void before() {
        testUser = userRepository.save(MockRequest.testUser());
    }

    public void issueCoupon() {
        CouponGroup testCouponGroup = couponGroupRepository.save(MockRequest.testCouponGroup());
        couponRepository.save(MockRequest.testCoupon(testUser, testCouponGroup));
    }

    public Dib makeDib() {
        return dibRepository.save(MockRequest.testDib(testUser));
    }

    @Test
    @DisplayName("프로필 조회")
    public void getMyProfile() {
        // given
        issueCoupon();
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
        List<DibResponseDto> responseList = mypageService.getMyDibs(testUser.getId());

        // then
        for(DibResponseDto response : responseList) {
            assertThat(response.getContentId()).isEqualTo(dib.getContentId());
            assertThat(response.getContentType()).isEqualTo(ContentType.getContentTypeByCode(dib.getContentTypeId()));
            assertThat(response.getTitle()).isEqualTo(dib.getTitle());
            assertThat(response.getTel()).isEqualTo(dib.getTel());
            assertThat(response.getFirstImageUrl()).isEqualTo(dib.getFirstImage());
        }
    }
}
