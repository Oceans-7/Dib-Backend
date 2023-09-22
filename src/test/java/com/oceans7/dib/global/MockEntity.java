package com.oceans7.dib.global;

import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.event.entity.*;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static com.oceans7.dib.global.MockRequest.CONTENT_ID;
import static com.oceans7.dib.global.MockRequest.CONTENT_TYPE;

public class MockEntity {
    public static User testUser() {
        return User.of("profile_img", "oceans", SocialType.KAKAO, "dib123", Role.USER);
    }

    public static CouponGroup testCouponGroup() {
        return CouponGroup.of("제주 서귀포 숙박 할인권", "제주 서귀포", CouponType.ACCOMMODATION, "1234", 10, LocalDate.now(), LocalDate.now().plusMonths(1), "https://picsum.photos/150/190", "https://picsum.photos/150/190");
    }

    public static CouponGroup testCouponGroup2() {
        return CouponGroup.of("제주 서귀포 다이빙 체험 할인권", "제주 서귀포", CouponType.SCUBA_DIVING, "1234", 10, LocalDate.now(), LocalDate.now().plusMonths(1), "https://picsum.photos/150/190", "https://picsum.photos/150/190");
    }

    public static Coupon testCoupon() {
        return Coupon.of(LocalDate.now(), CouponStatus.UNUSED);
    }

    public static Dib testDib(User user) {
        return Dib.of(CONTENT_ID, CONTENT_TYPE.getCode(), "뷰티플레이", "서울특별시 중구 명동1가 1-3 YWCA연합회", "070-4070-9675", "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg", user);
    }
    public static Event testEvent() {
        return Event.of("FF0770EF", "FFEBF4FE", "https://picsum.photos/150/190");
    }

    public static CustomContent testCustomContent() {
        return CustomContent.of(
                MockResponse.getCustomContentTestJsonFile(),
                "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg",
                "제주 서귀포",
                "다이빙 명소 및 관광지 파헤치기"
        );
    }

    public static MarineNotice testMarineNotice() {
        return MarineNotice.of("보름달물해파리 경남 주의단계", "특보", "안녕하세요, DIB 입니다.\n\n6월 22일부터 28일 간 총 16건의 해파리 웹 신고가 들어왔으며, 그 중 8건이 보름달물해파리였습니다. 해파리 발견 지역은 강원 2건, 경남 7건, 경북 1건, 전남 5건, 전북 1건, 제주 2건이었습니다.");
    }
}
