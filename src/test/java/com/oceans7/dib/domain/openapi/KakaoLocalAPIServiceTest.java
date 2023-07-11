package com.oceans7.dib.domain.openapi;

import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.service.KakaoLocalAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class KakaoLocalAPIServiceTest {
    @Autowired
    KakaoLocalAPIService kakaoLocalAPIService;

    @Test
    @DisplayName("카카오 주소 검색 API 통신 테스트")
    public void callSearchAddressAPITest() {
        // given
        String query = "여주";

        // when
        LocalResponse result =
                kakaoLocalAPIService.getSearchAddressLocalApi(query);
        LocalResponse.AddressItem item = result.getAddressItems().get(0);
        LocalResponse.AddressItem.Address addressItem = item.getAddress();

        // then
        assertThat(item.getAddressName()).isEqualTo("경기 여주시");
        assertThat(item.getAddressType()).isEqualTo("REGION");
        assertThat(item.getX()).isEqualTo(127.637058787484);
        assertThat(item.getY()).isEqualTo(37.2984233734535);
        assertThat(addressItem.getRegion1depthName()).isEqualTo("경기");
        assertThat(addressItem.getRegion2depthName()).isEqualTo("여주시");
    }

    @Test
    @DisplayName("카카오 좌표 -> 주소 전환 API 통신 테스트")
    public void callGeoAddressAPITest() {
        // given
        double x = 127.637058787484;
        double y = 37.2984233734535;

        // when
        LocalResponse result =
                kakaoLocalAPIService.getGeoAddressLocalApi(x, y);
        LocalResponse.AddressItem item = result.getAddressItems().get(0);
        LocalResponse.AddressItem.RoadAddress addressItem = item.getRoadAddress();

        // then
        assertThat(addressItem.getAddressName()).isEqualTo("경기도 여주시 세종로 1");
    }
}
