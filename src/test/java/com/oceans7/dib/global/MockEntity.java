package com.oceans7.dib.global;

import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.event.entity.*;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.organism.entity.*;
import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.report.entity.Report;
import com.oceans7.dib.domain.report.entity.ReportImage;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public static List<MarineOrganism> testMarineOrganism() {
        List<MarineOrganism> marineOrganism = new ArrayList<>();

        marineOrganism.add(Organism.of(
                MarineOrganism.class,
                "빨간 씬벵이",
                "Striated frogfish",
                "일반적인 몸길이는 10cm이며 눈이 작고, 입이 위를 향하며, 피부에는 작은 가시가 있습니다.",
                "부산, 제주도 등 우리나라 남부 얕은 해역의 암초와 모래질로 된 바닥에 주로 서식하며, 해면체 군집, 산호초 등 다양한 환경에서 발견됩니다.",
                "주로 암초 지대에 살면서 머리의 돌기를 흔들어 작은 물고기를 유인해 큰 입을 벌려 잡아 먹습니다.",
                "https://first.images/1",
                "https://illustration.images/1"
        ));
        marineOrganism.add(Organism.of(
                MarineOrganism.class,
                "붉은 멍게",
                "Sea Peach",
                "몸이 위아래로 길쭉하며 납작한 원통으로 이루어져 있으며, 사람의 주먹 정도의 크기를 가집니다.",
                "동해안 중북부 수심 30~50m 정도의 암반에서 군락으로 분포합니다.",
                "비단 멍게로도 불리며, 돌기 없이 붉은 복숭아의 겉면과 비슷해 영어로는 바다 복숭아라는 뜻을 가집니다. 멍게의 본래 명칭은 우렁쉥이지만, 경남 사투리인 멍게가 표준어로 인정받아 \n" +
                        "사용되고 있습니다. 또한 물을 빨아들이는 입수공과 배설물 및 물을 내보내는 출수공으로 이루어져 있는데, 출수공으로 물을 내뿜는 모습 때문에 영어권에서는 바다 물총이라고도 부릅니다.",
                "https://first.images/2",
                "https://illustration.images/2"
        ));
        marineOrganism.add(Organism.of(
                MarineOrganism.class,
                "용치놀래기",
                "Multicolorfin rainbowfish",
                "몸길이는 25cm 정도이며 옆으로 길고 평평한 모양입니다.",
                "우리나라 서해 남부, 남해, 제주도, 동해 남부에서 발견됩니다.",
                "지방에선 수멩이, 술맹이, 술뱅이, 용치, 이놀래기 등으로도 부릅니다. 모두 암컷으로 태어났다가 일부가 성전환하는 자성선숙 암수한몸입니다. 낮에는 먹이를 찾아 움직이며 밤에는 \n" +
                        "깊은 바다의 모래 바닥에 내려가 있습니다. 봄부터 가을까지는 연안의 암초가 많은 곳에서 살다가 겨울이 되어 추워지면 깊은 바다에서 겨울잠을 잡니다. 겨울잠을 잘 때 비늘에 나이테가 한 개 늘어납니다.",
                "https://first.images/3",
                "https://illustration.images/3"
        ));

        return marineOrganism;
    }

    public static List<MarineOrganismImage> testMarineOrganismImage() {
        List<MarineOrganismImage> marineOrganismImageList = new ArrayList<>();
        marineOrganismImageList.add(MarineOrganismImage.of("https://images/1"));
        marineOrganismImageList.add(MarineOrganismImage.of("https://images/2"));
        marineOrganismImageList.add(MarineOrganismImage.of("https://images/3"));
        marineOrganismImageList.add(MarineOrganismImage.of("https://images/4"));
        return marineOrganismImageList;
    }

    public static List<HarmfulOrganism> testHarmfulOrganism() {
        List<HarmfulOrganism> harmfulOrganism = new ArrayList<>();

        harmfulOrganism.add(Organism.of(
                HarmfulOrganism.class,
                "빨간 씬벵이",
                "Striated frogfish",
                "일반적인 몸길이는 10cm이며 눈이 작고, 입이 위를 향하며, 피부에는 작은 가시가 있습니다.",
                "부산, 제주도 등 우리나라 남부 얕은 해역의 암초와 모래질로 된 바닥에 주로 서식하며, 해면체 군집, 산호초 등 다양한 환경에서 발견됩니다.",
                "주로 암초 지대에 살면서 머리의 돌기를 흔들어 작은 물고기를 유인해 큰 입을 벌려 잡아 먹습니다.",
                "https://first.images/1",
                "https://illustration.images/1"
        ));

        harmfulOrganism.add(Organism.of(
                HarmfulOrganism.class,
                "붉은 멍게",
                "Sea Peach",
                "몸이 위아래로 길쭉하며 납작한 원통으로 이루어져 있으며, 사람의 주먹 정도의 크기를 가집니다.",
                "동해안 중북부 수심 30~50m 정도의 암반에서 군락으로 분포합니다.",
                "비단 멍게로도 불리며, 돌기 없이 붉은 복숭아의 겉면과 비슷해 영어로는 바다 복숭아라는 뜻을 가집니다. 멍게의 본래 명칭은 우렁쉥이지만, 경남 사투리인 멍게가 표준어로 인정받아 \n" +
                        "사용되고 있습니다. 또한 물을 빨아들이는 입수공과 배설물 및 물을 내보내는 출수공으로 이루어져 있는데, 출수공으로 물을 내뿜는 모습 때문에 영어권에서는 바다 물총이라고도 부릅니다.",
                "https://first.images/2",
                "https://illustration.images/2"
        ));
        harmfulOrganism.add(Organism.of(
                HarmfulOrganism.class,
                "용치놀래기",
                "Multicolorfin rainbowfish",
                "몸길이는 25cm 정도이며 옆으로 길고 평평한 모양입니다.",
                "우리나라 서해 남부, 남해, 제주도, 동해 남부에서 발견됩니다.",
                "지방에선 수멩이, 술맹이, 술뱅이, 용치, 이놀래기 등으로도 부릅니다. 모두 암컷으로 태어났다가 일부가 성전환하는 자성선숙 암수한몸입니다. 낮에는 먹이를 찾아 움직이며 밤에는 \n" +
                        "깊은 바다의 모래 바닥에 내려가 있습니다. 봄부터 가을까지는 연안의 암초가 많은 곳에서 살다가 겨울이 되어 추워지면 깊은 바다에서 겨울잠을 잡니다. 겨울잠을 잘 때 비늘에 나이테가 한 개 늘어납니다.",
                "https://first.images/3",
                "https://illustration.images/3"
        ));

        return harmfulOrganism;
    }

    public static List<HarmfulOrganismImage> testHarmfulOrganismImage() {
        List<HarmfulOrganismImage> harmfulOrganismImageList = new ArrayList<>();

        harmfulOrganismImageList.add(HarmfulOrganismImage.of("https://images/1"));
        harmfulOrganismImageList.add(HarmfulOrganismImage.of("https://images/2"));
        harmfulOrganismImageList.add(HarmfulOrganismImage.of("https://images/3"));
        harmfulOrganismImageList.add(HarmfulOrganismImage.of("https://images/4"));

        return harmfulOrganismImageList;
    }

    public static Report testReport(User user) {
        return Report.of(
                MockRequest.testReportReq().getOrganismName(),
                MockRequest.testReportReq().getFoundLocation(),
                user);
    }

    public static List<ReportImage> testReportImage() {
        List<ReportImage> reportImageList = new ArrayList<>();

        reportImageList.add(ReportImage.of(MockRequest.testReportReq().getImageUrlList().get(0)));

        return reportImageList;
    }
}
