package com.oceans7.dib.domain.event.controller;

import com.oceans7.dib.domain.event.dto.response.EventResponseDto;
import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.repository.CouponGroupRepository;
import com.oceans7.dib.domain.event.repository.CouponRepository;
import com.oceans7.dib.domain.event.repository.EventRepository;
import com.oceans7.dib.domain.event.service.CouponService;
import com.oceans7.dib.domain.event.service.EventService;
import com.oceans7.dib.domain.event.dto.response.DetailEventResponseDto;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private CouponService couponService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private CouponGroupRepository couponGroupRepository;

    @MockBean
    private CouponRepository couponRepository;

    @MockBean
    private UserRepository userRepository;

    private final String TEST_USER_ID = "1";

    public User makeUser() {
        User user = MockEntity.testUser();

        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.save(user)).thenReturn(user);
        return userRepository.save(user);
    }

    private Event makeEvent() {
        Event event = MockEntity.testEvent();

        ReflectionTestUtils.setField(event, "eventId", 1L);

        when(eventRepository.save(event)).thenReturn(event);
        return eventRepository.save(event);
    }

    private CouponGroup makeFirstCouponGroup(Event event) {
        CouponGroup couponGroup = MockEntity.testCouponGroup(event);

        ReflectionTestUtils.setField(couponGroup, "couponGroupId", 1L);

        when(couponGroupRepository.save(couponGroup)).thenReturn(couponGroup);
        return couponGroupRepository.save(couponGroup);
    }

    private CouponGroup makeSecondCouponGroup(Event event) {
        CouponGroup couponGroup = MockEntity.testCouponGroup2(event);

        ReflectionTestUtils.setField(couponGroup, "couponGroupId", 2L);

        when(couponGroupRepository.save(couponGroup)).thenReturn(couponGroup);
        return couponGroupRepository.save(couponGroup);
    }

    private Coupon makeCoupon(CouponGroup couponGroup, User user) {
        Coupon coupon = MockEntity.testCoupon(couponGroup, user);

        ReflectionTestUtils.setField(coupon, "couponId", 1L);

        when(couponRepository.save(coupon)).thenReturn(coupon);
        return couponRepository.save(coupon);
    }

    @Test
    @DisplayName("이벤트 조회 테스트")
    @WithMockUser(username = TEST_USER_ID, roles = "USER")
    public void getAllEvent() throws Exception {
        // given
        Event event = makeEvent();

        List<EventResponseDto> mockResponse = MockResponse.testEventRes(event);
        when(eventService.getAllEvent()).thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/event"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].eventId").value(mockResponse.get(0).getEventId()))
                .andExpect(jsonPath("$.data[0].bannerImageUrl").value(mockResponse.get(0).getBannerImageUrl()));

    }

    @Test
    @DisplayName("이벤트 조회 테스트 : 진행중인 이벤트 없는 경우")
    @WithMockUser()
    public void getAllEventIfNotRegisteredEvent() throws Exception {
        // given
        when(eventService.getAllEvent()).thenReturn(new ArrayList<>());

        // when
        ResultActions result = mvc.perform(get("/event"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(new ArrayList<>()));
    }

    @Test
    @DisplayName("이벤트 상세 조회 테스트")
    @WithMockUser(username = TEST_USER_ID, roles = "USER")
    public void getEventDetail() throws Exception {
        // given
        Event event = makeEvent();
        CouponGroup firstCouponGroup = makeFirstCouponGroup(event);
        CouponGroup secondCouponGroup = makeSecondCouponGroup(event);

        DetailEventResponseDto mockResponse = MockResponse.testDetailEventRes(event, firstCouponGroup, secondCouponGroup);
        when(eventService.getEventDetail(event.getEventId()))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/event/" + event.getEventId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eventId").value(mockResponse.getEventId()))
                .andExpect(jsonPath("$.data.firstImageUrl").value(mockResponse.getFirstImageUrl()))
                .andExpect(jsonPath("$.data.mainColor").value(mockResponse.getMainColor()))
                .andExpect(jsonPath("$.data.subColor").value(mockResponse.getSubColor()))
                .andExpect(jsonPath("$.data.firstCouponSection.couponGroupId").value(mockResponse.getFirstCouponSection().getCouponGroupId()))
                .andExpect(jsonPath("$.data.firstCouponSection.title").value(mockResponse.getFirstCouponSection().getTitle()))
                .andExpect(jsonPath("$.data.firstCouponSection.keyword").value(mockResponse.getFirstCouponSection().getKeyword()))
                .andExpect(jsonPath("$.data.firstCouponSection.couponImageUrl").value(mockResponse.getFirstCouponSection().getCouponImageUrl()))
                .andExpect(jsonPath("$.data.secondCouponSection.couponGroupId").value(mockResponse.getSecondCouponSection().getCouponGroupId()))
                .andExpect(jsonPath("$.data.secondCouponSection.title").value(mockResponse.getSecondCouponSection().getTitle()))
                .andExpect(jsonPath("$.data.secondCouponSection.keyword").value(mockResponse.getSecondCouponSection().getKeyword()))
                .andExpect(jsonPath("$.data.secondCouponSection.couponImageUrl").value(mockResponse.getSecondCouponSection().getCouponImageUrl()))
                .andExpect(jsonPath("$.data.partnerSection.title").value(mockResponse.getPartnerSection().getTitle()))
                .andExpect(jsonPath("$.data.partnerSection.keyword").value(mockResponse.getPartnerSection().getKeyword()))
                .andExpect(jsonPath("$.data.partnerSection.firstPartner.description").value(mockResponse.getPartnerSection().getFirstPartner().getDescription()))
                .andExpect(jsonPath("$.data.partnerSection.firstPartner.partnerType").value(mockResponse.getPartnerSection().getFirstPartner().getPartnerType()))
                .andExpect(jsonPath("$.data.partnerSection.firstPartner.partnerImageUrl").value(mockResponse.getPartnerSection().getFirstPartner().getPartnerImageUrl()))
                .andExpect(jsonPath("$.data.partnerSection.firstPartner.couponOpenDate").value(mockResponse.getPartnerSection().getFirstPartner().getCouponOpenDate()))
                .andExpect(jsonPath("$.data.partnerSection.secondPartner.description").value(mockResponse.getPartnerSection().getSecondPartner().getDescription()))
                .andExpect(jsonPath("$.data.partnerSection.secondPartner.partnerType").value(mockResponse.getPartnerSection().getSecondPartner().getPartnerType()))
                .andExpect(jsonPath("$.data.partnerSection.secondPartner.partnerImageUrl").value(mockResponse.getPartnerSection().getSecondPartner().getPartnerImageUrl()))
                .andExpect(jsonPath("$.data.partnerSection.secondPartner.couponOpenDate").value(mockResponse.getPartnerSection().getSecondPartner().getCouponOpenDate()));

    }

    @Test
    @DisplayName("쿠폰 발급 테스트")
    @WithMockUser(username = TEST_USER_ID, roles = "USER")
    public void issueCoupon() throws Exception {
        // given
        CouponGroup firstCouponGroup = makeFirstCouponGroup(makeEvent());
        User user = makeUser();
        Coupon coupon = makeCoupon(firstCouponGroup, user);

        when(couponRepository.findByUserAndCouponGroup(user, firstCouponGroup))
                .thenReturn(Optional.of(coupon));

        // when
        ResultActions result = mvc.perform(post("/coupon/" + coupon.getCouponId())
                .with(csrf()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(""));
        Coupon findCoupon = couponRepository.findByUserAndCouponGroup(user, firstCouponGroup).orElseThrow();

        assertThat(findCoupon.getStatus()).isEqualTo(coupon.getStatus());
        assertThat(findCoupon.getIssuedDate()).isEqualTo(coupon.getIssuedDate());
        assertThat(findCoupon.getUser().getId()).isEqualTo(coupon.getUser().getId());
        assertThat(findCoupon.getCouponGroup().getCouponGroupId()).isEqualTo(coupon.getCouponGroup().getCouponGroupId());
    }
}
