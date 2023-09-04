package com.oceans7.dib.domain.location.controller;

import com.oceans7.dib.domain.location.dto.request.SearchLocationRequestDto;
import com.oceans7.dib.domain.location.service.LocationService;
import com.oceans7.dib.global.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.Mockito.when;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LocationService locationService;

    @Test
    @DisplayName("좌표 기준 주소 및 날씨 조회 테스트")
    @WithMockUser("user1")
    public void searchPlaceTest() throws Exception {
        // given
        SearchLocationRequestDto searchLocationReq = MockRequest.testSearchLocationReq();
        when(locationService.searchPlace(searchLocationReq))
                .thenReturn(MockResponse.testSearchPlaceRes());

        // when
        ResultActions result = mvc.perform(get("/location")
                .param("mapX", String.valueOf(MockRequest.X))
                .param("mapY", String.valueOf(MockRequest.Y)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.address").value(MockResponse.testSearchPlaceRes().getAddress()))
                .andExpect(jsonPath("$.data.weatherType").value(MockResponse.testSearchPlaceRes().getWeatherType().toString()))
                .andExpect(jsonPath("$.data.temperatures").value(MockResponse.testSearchPlaceRes().getTemperatures()));
    }

}
