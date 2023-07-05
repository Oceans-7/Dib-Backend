package com.oceans7.dib.openapi;

import com.oceans7.dib.global.api.response.kakao.SearchAddressListResponse;
import com.oceans7.dib.global.api.response.kakao.SearchAddressListResponse.*;
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
        String query = "먀먀먀";

        // when
        SearchAddressListResponse result =
                kakaoLocalAPIService.getSearchAddressLocalApi(query);
        SearchAddressItemResponse item = result.getSearchAddressItemResponseList().get(0);
        SearchAddressItemResponse.Address addressItem = item.getAddress();

        // then
        assertThat(item.getAddress_name()).isEqualTo("경기 여주시");
        assertThat(item.getAddress_type()).isEqualTo("REGION");
        assertThat(item.getX()).isEqualTo(127.637058787484);
        assertThat(item.getY()).isEqualTo(37.2984233734535);
        assertThat(addressItem.getRegion_1depth_name()).isEqualTo("경기");
        assertThat(addressItem.getRegion_2depth_name()).isEqualTo("여주시");
    }
}
