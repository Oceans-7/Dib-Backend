package com.oceans7.dib.domain.mypage.service;

import com.oceans7.dib.domain.mypage.dto.response.*;
import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final DibRepository dibRepository;

    private final ImageAssetUrlProcessor imageAssetUrlProcessor;

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public MypageResponseDto getMyProfile(Long userId) {
        User user = findUser(userId);

        Long dibCount = dibRepository.countByUser(user);
        Long couponCount = couponRepository.countByPossibleCoupon(userId);

        return MypageResponseDto.of(
                imageAssetUrlProcessor.prependCloudFrontHost(user.getProfileUrl()),
                user.getNickname(),
                dibCount,
                couponCount
        );
    }

    @Transactional(readOnly = true)
    public DibResponseDto getMyDibs(Long userId) {
        User user = findUser(userId);

        List<Dib> dibList = dibRepository.findByUser(user);

        List<DetailDibResponseDto> detailDibResponseDtoList = dibList.stream()
                .map(DetailDibResponseDto :: from)
                .collect(Collectors.toList());

        return DibResponseDto.from(detailDibResponseDtoList);
    }

    @Transactional(readOnly = true)
    public CouponResponseDto getMyCoupons(Long userId) {
        List<Coupon> couponList = couponRepository.findPossibleCouponsOrderByClosingDateAsc(userId);

        List<DetailCouponResponseDto> detailCouponResponseDtoList = couponList.stream()
                .map(coupon -> DetailCouponResponseDto.of(
                        coupon.getCouponId(),
                        imageAssetUrlProcessor.prependCloudFrontHost(coupon.getCouponGroup().getPartnerImageUrl()),
                        coupon.getCouponGroup().getRegion(),
                        coupon.getCouponGroup().getCouponType().getKeyword(),
                        coupon.getCouponGroup().getDiscountPercentage(),
                        coupon.getCouponGroup().getStartDate(),
                        coupon.getCouponGroup().getClosingDate(),
                        Duration.between(coupon.getCouponGroup().getStartDate().atStartOfDay(), coupon.getCouponGroup().getClosingDate().atStartOfDay()).toDays()
                ))
                .collect(Collectors.toList());

        return CouponResponseDto.from(detailCouponResponseDtoList);
    }

    @Transactional
    public void updateMyProfile(Long userId, UpdateProfileRequestDto request) {
        User user = findUser(userId);

        user.updateProfile(request.getNickname(), imageAssetUrlProcessor.extractUrlPath(request.getImageUrl()));
    }
}
