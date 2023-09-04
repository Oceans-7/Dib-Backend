package com.oceans7.dib.domain.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MypageResponseDto {

    @Schema(description = "프로필 사진 URL", example = "https://dib-file-bucket.s3.ap-northeast-2.amazonaws.com/images/e2a5c3f0-514a-4c49-836e-a9b7d45c1569.png")
    private String profileUrl;

    @Schema(description = "닉네임", example = "김다이브")
    private String nickname;

    @Schema(description = "찜 개수", example = "1")
    private Long dibCount;

    @Schema(description = "쿠폰 개수", example = "3")
    private Long couponCount;

    public static MypageResponseDto of(String profileUrl, String nickname, Long dibCount, Long couponCount) {
        MypageResponseDto mypageResponse = new MypageResponseDto();
        mypageResponse.profileUrl = profileUrl;
        mypageResponse.nickname = nickname;
        mypageResponse.dibCount = dibCount;
        mypageResponse.couponCount = couponCount;

        return mypageResponse;
    }
}
