package com.oceans7.dib.domain.mypage.service;

import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.mypage.dto.request.UpdateProfileRequestDto;
import com.oceans7.dib.domain.mypage.dto.response.DibResponseDto;
import com.oceans7.dib.domain.mypage.dto.response.MypageResponseDto;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.place.repository.DibRepository;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.service.DataGoKrAPIService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.AmazonS3ResourceStorage;
import com.oceans7.dib.global.util.MultipartUtil;
import com.oceans7.dib.global.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final DataGoKrAPIService tourAPIService;

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final DibRepository dibRepository;

    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    private static final String S3_URL = "https://dib-file-bucket.s3.ap-northeast-2.amazonaws.com/";

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public MypageResponseDto getMyProfile(Long userId) {
        User user = findUser(userId);

        Long dibCount = dibRepository.countByUser(user);
        Long couponCount = couponRepository.countByPossibleCoupon(userId);

        return MypageResponseDto.of(user.getProfileUrl(), user.getNickname(), dibCount, couponCount);
    }

    @Transactional(readOnly = true)
    public List<DibResponseDto> getMyDibs(Long userId) {
        User user = findUser(userId);

        List<Dib> dibList = dibRepository.findByUser(user);

        return dibList.stream().map(dib -> DibResponseDto.of(dib)).collect(Collectors.toList());
    }

    @Transactional
    public void updateMyProfile(Long userId, UpdateProfileRequestDto request) {
        User user = findUser(userId);

        checkDuplicatedNickname(user, request);

        String filePath = user.getProfileUrl();
        if(ValidatorUtil.isNotEmpty(request.getMultipartFile())) {
            filePath = MultipartUtil.getPath(request.getMultipartFile());
            amazonS3ResourceStorage.store(filePath, request.getMultipartFile());
        }

        user.updateProfile(request.getNickname(), S3_URL + filePath);
    }

    private void checkDuplicatedNickname(User user, UpdateProfileRequestDto request) {
        Optional<User> findUser = userRepository.findByNickname(request.getNickname());
        if(findUser.isPresent() && user != findUser.get()) {
            throw new ApplicationException(ErrorCode.ALREADY_USED_NICKNAME_EXCEPTION);
        }
    }
}
