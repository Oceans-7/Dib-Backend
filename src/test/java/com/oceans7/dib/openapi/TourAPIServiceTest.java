package com.oceans7.dib.openapi;

import com.oceans7.dib.openapi.dto.response.LocationBasedList;
import com.oceans7.dib.openapi.service.TourAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TourAPIServiceTest {
    @Autowired
    TourAPIService tourAPIService;

    @Test
    @DisplayName("위치 기반 서비스 API 통신 테스트")
    public void callLocationBasedListAPITest() throws IOException {
        // given
        double mapX = 126.9779692;
        double mapY = 37.566535;
        String contentTypeId = "12";

        // when
        LocationBasedList list = tourAPIService.fetchDataFromLocationBasedApi(mapX, mapY, contentTypeId);

        // then
        assertThat(list.getLocationBasedItems().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("키워드 검색 서비스 API 통신 테스트")
    public void callSearchKeywordAPITest() throws IOException {
        // given
        String keyword = "강원";

        // when
        LocationBasedList list = tourAPIService.fetchDataFromSearchKeywordApi(keyword);

        // then
        assertThat(list.getLocationBasedItems().size()).isEqualTo(10);
    }
}
