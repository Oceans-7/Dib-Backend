package com.oceans7.dib.domain.event.service;

import com.oceans7.dib.domain.event.dto.response.CouponResponseDto;
import com.oceans7.dib.domain.event.dto.response.DetailCouponResponseDto;
import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.UseStatus;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponGroupRepository couponGroupRepository;

    @Transactional
    public void issue(Long userId, Long couponGroupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> handleNotFoundException());
        CouponGroup couponGroup = couponGroupRepository.findById(couponGroupId).orElseThrow(() -> handleNotFoundException());

        Optional<Coupon> findCoupon = couponRepository.findByUserAndCouponGroup(user, couponGroup);

        if(findCoupon.isPresent()) {
            throw new ApplicationException(ErrorCode.ALREADY_ISSUED_EXCEPTION);
        }

        getIssuedCoupon(user, couponGroup);
    }

    private void getIssuedCoupon(User user, CouponGroup couponGroup) {
        couponRepository.save(Coupon.of(LocalDate.now(), couponGroup, user, UseStatus.UNUSED));
    }

    private ApplicationException handleNotFoundException() {
        return new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    @Transactional(readOnly = true)
    public CouponResponseDto getCoupon(Long userId) {
        List<Coupon> couponList = couponRepository.findPossibleCouponsOrderByClosingDateAsc(userId);

        List<DetailCouponResponseDto> detailCouponResponseDtoList = couponList.stream()
                .map(DetailCouponResponseDto :: of)
                .collect(Collectors.toList());

        return CouponResponseDto.of(detailCouponResponseDtoList);
    }
}
