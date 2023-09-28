package com.oceans7.dib.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.dto.response.detail.Content;
import com.oceans7.dib.domain.custom_content.dto.response.detail.DetailContentResponseDto;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.event.entity.CouponGroup;
import com.oceans7.dib.domain.event.entity.Event;
import com.oceans7.dib.domain.event.dto.response.CouponSectionResponseDto;
import com.oceans7.dib.domain.event.dto.response.EventResponseDto;
import com.oceans7.dib.domain.event.dto.response.PartnerResponseDto;
import com.oceans7.dib.domain.event.dto.response.PartnerSectionResponseDto;
import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.domain.notice.dto.response.NoticeResponseDto;
import com.oceans7.dib.domain.notice.entity.MarineNotice;
import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.entity.HarmfulOrganism;
import com.oceans7.dib.domain.organism.entity.MarineOrganism;
import com.oceans7.dib.domain.organism.entity.Organism;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.dto.FacilityType;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.*;
import com.oceans7.dib.global.api.response.kakao.LocalResponse.AddressItem.*;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoItemResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeItem;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonItemResponse;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroItemResponse.*;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.global.ResponseWrapper.Response;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ValidatorUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceans7.dib.global.MockRequest.*;

public class MockResponse {
    public final static double MIN_DISTANCE = 0.0;
    public final static double MAX_DISTANCE = 20.0;

    // --- KakaoLocalAPIService Test Mock Response
    private static Address setAddress() {
        return new Address("서울 중구", "서울", "중구");
    }

    private static Address setRoadAddress() {
        return new Address("서울특별시 중구 창경궁로 17", "서울", "중구");
    }

    private static AddressItem setAddressItem(Address address, Address roadAddress) {
        return new AddressItem(address, roadAddress, "서울 중구", "REGION", X, Y);
    }

    public static LocalResponse testSearchAddressRes() {
        List<AddressItem> item = new ArrayList<>();
        item.add(setAddressItem(setAddress(), null));
        return new LocalResponse(item);
    }

    public static LocalResponse testGeoAddressRes() {
        List<AddressItem> item = new ArrayList<>();
        item.add(setAddressItem(null, setRoadAddress()));
        return new LocalResponse(item);
    }

    // --- DataGoKrAPIService Test Mock Response
    private static TourAPICommonItemResponse setCommonTourItem(double dist, String zipcode) {
        return new TourAPICommonItemResponse(
                CONTENT_ID, CONTENT_TYPE.getCode(),
                "뷰티플레이", "",
                "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image3_1.jpg",
                "서울특별시 중구 명동1가 1-3 YWCA연합회", "",
                "Type1", X, Y,
                "20230129232104", "20230208103221",
                dist, zipcode, "",
                "24", "1",
                "A03", "A0302", "A03022600");
    }

    private static TourAPICommonItemResponse setCommonTourItem2(double dist, String zipcode) {
        return new TourAPICommonItemResponse(
                CONTENT_ID, CONTENT_TYPE.getCode(),
                "서울 남현동 요지", "",
                "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/49/2947649_image3_1.jpg",
                "서울특별시 관악구 남현3길 60", "(남현동)",
                "Type1", X, Y,
                "20230129232104", "20230208103221",
                dist, zipcode, "",
                "24", "1",
                "A02", "A0201", "A02010700");
    }

    public static ResponseWrapper testLocationBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(1000.1711716167842, null));
        return new ResponseWrapper(
                new Response(
                        TourAPICommonListResponse.builder()
                                .tourAPICommonItemResponseList(item)
                                .page(1)
                                .pageSize(1)
                                .totalCount(1)
                                .build())
        );
    }

    public static ResponseWrapper testAreaBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(0, "04538"));
        return new ResponseWrapper(
                new Response(
                        TourAPICommonListResponse.builder()
                            .tourAPICommonItemResponseList(item)
                            .page(1)
                            .pageSize(1)
                            .totalCount(1)
                            .build())
        );
    }

    public static ResponseWrapper testKeywordBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(0, null));
        return new ResponseWrapper(
                new Response(
                        TourAPICommonListResponse.builder()
                                .tourAPICommonItemResponseList(item)
                                .page(1)
                                .pageSize(1)
                                .totalCount(1)
                                .build())
        );
    }

    private static AreaCodeItem setAreaCodeItem(String name, String code) {
        return new AreaCodeItem(name, code);
    }

    public static ResponseWrapper testAreaCodeRes() {
        List<AreaCodeItem> item = new ArrayList<>();
        item.add(setAreaCodeItem("서울","1"));
        item.add(setAreaCodeItem("인천","2"));
        item.add(setAreaCodeItem("대전", "3"));
        return new ResponseWrapper(
                new Response(new AreaCodeList(item))
        );
    }

    public static ResponseWrapper testSigunguCodeRes() {
        List<AreaCodeItem> item = new ArrayList<>();
        item.add(setAreaCodeItem("강남구","1"));
        item.add(setAreaCodeItem("강동구","2"));
        item.add(setAreaCodeItem("강북구", "3"));
        return new ResponseWrapper(
                new Response(new AreaCodeList(item))
        );
    }

    private static DetailCommonItemResponse setDetailCommonItem() {
        return new DetailCommonItemResponse(setCommonTourItem(0, "04538"),"www.beautyplay.kr", "");
    }

    public static ResponseWrapper testDetailCommonRes() {
        List<DetailCommonItemResponse> item = new ArrayList<>();
        item.add(setDetailCommonItem());

        return new ResponseWrapper(
                new Response(new DetailCommonListResponse(item))
        );
    }

    private static SpotItemResponse setDetailIntroItem() {
        return new SpotItemResponse(
                CONTENT_ID, CONTENT_TYPE.getCode(),
                "070-4070-9675",
                "", "", "",
                "일요일", "10:00~19:00(뷰티 체험은 18:00까지)");
    }

    public static ResponseWrapper testDetailIntroRes() {
        List<SpotItemResponse> item = new ArrayList<>();
        item.add(setDetailIntroItem());

        return new ResponseWrapper(
                new Response(new SpotIntroResponse(item))
        );
    }

    private static DetailInfoItemResponse setDetailInfoItem() {
        return new DetailInfoItemResponse(CONTENT_ID, CONTENT_TYPE.getCode(), "화장실", "있음");
    }

    public static ResponseWrapper testDetailInfoRes() {
        List<DetailInfoItemResponse> item = new ArrayList<>();
        item.add(setDetailInfoItem());

        return new ResponseWrapper(
                new Response(new DetailInfoListResponse(item))
        );
    }

    private static List<DetailImageItemResponse> setDetailImageItem() {
        List<DetailImageItemResponse> item = new ArrayList<>();
        item.add(new DetailImageItemResponse(CONTENT_ID, "http://tong.visitkorea.or.kr/cms/resource/50/2947650_image2_1.jpg"));
        item.add(new DetailImageItemResponse(CONTENT_ID, "http://tong.visitkorea.or.kr/cms/resource/52/2947652_image2_1.jpg"));
        item.add(new DetailImageItemResponse(CONTENT_ID, "http://tong.visitkorea.or.kr/cms/resource/51/2947651_image2_1.jpg"));
        item.add(new DetailImageItemResponse(CONTENT_ID, "http://tong.visitkorea.or.kr/cms/resource/53/2947653_image2_1.jpg"));
        return item;
    }

    public static ResponseWrapper testDetailImageRes() {
        return new ResponseWrapper(
                new Response(new DetailImageListResponse(setDetailImageItem()))
        );
    }

    // --- VlilageFcstAPIService Test Mock Response
    private static List<FcstAPICommonItemResponse> setNcstItem(String baseDate, String baseTime) {
        List<FcstAPICommonItemResponse> item = new ArrayList<>();
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "0", "PTY", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "89","REH", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "0", "RN1", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "26.1", "T1H", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "1", "UUU", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "205", "VEC", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "2.1", "VVV", null, null, null));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, "2.3", "WSD", null, null, null));
        return item;
    }

    public static ResponseWrapper testNcstRes() {
        return new ResponseWrapper(
                new Response(new FcstAPICommonListResponse(setNcstItem("20230726", "0100")))
        );
    }

    private static List<FcstAPICommonItemResponse> setFcstItem(String baseDate, String baseTime, String fcstDate, String fcstTime) {
        List<FcstAPICommonItemResponse> item = new ArrayList<>();
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "LGT", fcstDate, fcstTime, "0"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "PTY", fcstDate, fcstTime, "0"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "RN1", fcstDate, fcstTime, "강수없음"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "SKY", fcstDate, fcstTime, "4"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "T1H", fcstDate, fcstTime, "25"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "REH", fcstDate, fcstTime, "85"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "UUU", fcstDate, fcstTime, "1.7"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "VVV", fcstDate, fcstTime, "1.2"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "VEC", fcstDate, fcstTime, "228"));
        item.add(new FcstAPICommonItemResponse(baseDate, baseTime, null, "WSD", fcstDate, fcstTime, "2"));
        return item;
    }

    public static ResponseWrapper testFcstRes() {
        return new ResponseWrapper(
                new Response(new FcstAPICommonListResponse(setFcstItem("20230726", "0000", "20230726", "0100")))
        );
    }

    // --- PlaceService Test Mock Response
    public static TourAPICommonListResponse testPlaceRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(1000.1711716167842, null));
        item.add(setCommonTourItem2(10001.983304508862, null));
        return TourAPICommonListResponse.builder()
                                .tourAPICommonItemResponseList(item)
                                .page(1)
                                .pageSize(2)
                                .totalCount(1)
                                .build();
    }

    public static AreaCodeList testPlaceAreaCodeRes() {
        List<AreaCodeItem> item = new ArrayList<>();
        item.add(setAreaCodeItem("서울","1"));
        item.add(setAreaCodeItem("인천","2"));
        item.add(setAreaCodeItem("대전", "3"));
        return new AreaCodeList(item);
    }

    public static AreaCodeList testPlaceSigunguCodeRes() {
        List<AreaCodeItem> item = new ArrayList<>();
        item.add(setAreaCodeItem("강남구","1"));
        item.add(setAreaCodeItem("강동구","2"));
        item.add(setAreaCodeItem("중구", "24"));
        return new AreaCodeList(item);
    }

    public static TourAPICommonListResponse testAreaPlaceRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(1000.1711716167842, "04538"));
        return TourAPICommonListResponse.builder()
                .tourAPICommonItemResponseList(item)
                .page(1)
                .pageSize(1)
                .totalCount(1)
                .build();
    }

    public static LocalResponse testSearchNoAddressRes() {
        return new LocalResponse(null);
    }

    public static TourAPICommonListResponse testSearchRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(1000.1711716167842, null));
        return TourAPICommonListResponse.builder()
                .tourAPICommonItemResponseList(item)
                .page(1)
                .pageSize(1)
                .totalCount(1)
                .build();
    }

    public static TourAPICommonListResponse testNoResultRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        return TourAPICommonListResponse.builder()
                .tourAPICommonItemResponseList(item)
                .page(0)
                .pageSize(0)
                .totalCount(0)
                .build();
    }

    public static DetailCommonListResponse testPlaceCommonRes() {
        List<DetailCommonItemResponse> item = new ArrayList<>();
        item.add(setDetailCommonItem());

        return new DetailCommonListResponse(item);
    }

    public static SpotIntroResponse testPlaceIntroRes() {
        List<SpotItemResponse> item = new ArrayList<>();
        item.add(setDetailIntroItem());

        return new SpotIntroResponse(item);
    }

    public static DetailInfoListResponse testPlaceInfoRes() {
        List<DetailInfoItemResponse> item = new ArrayList<>();
        item.add(setDetailInfoItem());

        return new DetailInfoListResponse(item);
    }

    public static DetailImageListResponse testPlaceImageRes() {
        return new DetailImageListResponse(setDetailImageItem());
    }

    // --- LocationService Test Mock Response
    public static FcstAPICommonListResponse testLocationNcstRes(String baseDate, String baseTime) {
        return new FcstAPICommonListResponse(setNcstItem(baseDate, baseTime));
    }

    public static FcstAPICommonListResponse testLocationFcstRes(String baseDate, String baseTime, String fcstDate, String fcstTime) {
        return new FcstAPICommonListResponse(setFcstItem(baseDate, baseTime, fcstDate, fcstTime));
    }

    public static LocalResponse testGeoAddressXYExceptionRes() {
        return new LocalResponse(null);
    }

    // LocationController Test Mock Response
    public static LocationResponseDto testSearchPlaceRes() {
        String addressName = "서울특별시 중구 창경궁로 17";
        WeatherType weatherType = WeatherType.SUNNY;
        double temperatures = 33.2;

        return LocationResponseDto.of(addressName, weatherType, temperatures);
    }

    // PlaceController Test Mock Response
    public static PlaceResponseDto testGetPlaceRes() {
        ArrangeType arrangeType = null;
        List<SimplePlaceInformationDto> simpleDto = testPlaceRes().getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .collect(Collectors.toList());

        return PlaceResponseDto.of(simpleDto, testPlaceRes(), arrangeType);
    }

    public static PlaceResponseDto testGetPlaceBasedAreaRes() {
        ArrangeType arrangeType = null;
        List<SimplePlaceInformationDto> simpleDto = testAreaPlaceRes().getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .collect(Collectors.toList());

        return PlaceResponseDto.of(simpleDto, testAreaPlaceRes(), arrangeType);
    }

    public static SearchPlaceResponseDto testSearchPlaceBasedKeywordRes() {
        List<SimplePlaceInformationDto> simpleDto = testSearchRes().getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .collect(Collectors.toList());

        return SearchPlaceResponseDto.of(KEYWORD_QUERY, simpleDto, testSearchRes(), false);
    }

    public static SearchPlaceResponseDto testSearchPlaceBasedAreaRes() {
        List<SimpleAreaResponseDto> simpleDto = new ArrayList<>();
        simpleDto.add(SimpleAreaResponseDto.of("서울 중구", 1000.1711716167842, "서울", "중구", X, Y));
        return SearchPlaceResponseDto.of(AREA_QUERY, simpleDto, true);
    }

    public static DetailPlaceInformationResponseDto testGetDetailPlaceRes() {
        DetailCommonItemResponse commonItem = setDetailCommonItem();
        SpotItemResponse spotItem = setDetailIntroItem();
        DetailInfoItemResponse infoItem = setDetailInfoItem();
        List<DetailImageItemResponse> imageItems = setDetailImageItem();

        List<String> imageUrls = imageItems.stream()
                .map(image -> image.getOriginImageUrl())
                .collect(Collectors.toList());

        DetailPlaceInformationResponseDto mock = DetailPlaceInformationResponseDto.of(commonItem, imageUrls);

        List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(spotItem.getCheckBabyCarriage())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(spotItem.getCheckCreditCard())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(spotItem.getCheckPet())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.RESTROOM, true));

        mock.updateItem(spotItem.getUseTime(), spotItem.getInfoCenter(), spotItem.getRestDate(), null, null, facilityInfo);
        return mock;
    }

    public static PartnerResponseDto testPartnerRes(CouponGroup couponGroup) {
        return PartnerResponseDto.from(couponGroup);
    }

    public static PartnerSectionResponseDto testPartnerSectionRes(CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        String partnerSectionKeyword = String.format("%s, %s", firstCouponGroup.getCouponType().getKeyword(), secondCouponGroup.getCouponType().getKeyword());
        String partnerSectionTitle = String.format("%s %s \n할인 참여 업체", firstCouponGroup.getRegion(), partnerSectionKeyword);

        return PartnerSectionResponseDto.of(
                partnerSectionTitle,
                partnerSectionKeyword,
                testPartnerRes(firstCouponGroup),
                testPartnerRes(secondCouponGroup)
        );
    }

    public static CouponSectionResponseDto testCouponSectionRes(CouponGroup couponGroup) {
        return CouponSectionResponseDto.from(couponGroup);
    }

    public static EventResponseDto testEventRes(Event event, CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        return EventResponseDto.of(
                event.getEventId(),
                event.getBannerUrl(),
                event.getMainColor(),
                event.getSubColor(),
                testCouponSectionRes(firstCouponGroup),
                testCouponSectionRes(secondCouponGroup),
                testPartnerSectionRes(firstCouponGroup, secondCouponGroup)
        );
    }

    public static List<NoticeResponseDto> testMarineNoticeRes(MarineNotice marineNotice) {
        List<NoticeResponseDto> noticeResponse = new ArrayList<>();
        noticeResponse.add(NoticeResponseDto.from(marineNotice));
        return noticeResponse;
    }

    public static Content testContentRes() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Content result = mapper.readValue(getCustomContentTestJsonFile(), Content.class);

            return result;
        } catch(Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public static String getCustomContentTestJsonFile() {
        try{
            ClassPathResource resource = new ClassPathResource("custom-content-test-data.json");
            InputStream inputStream = resource.getInputStream();

            return new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ContentResponseDto> testCustomContentRes(CustomContent customContent) {
        List<ContentResponseDto> customContentResponse = new ArrayList<>();
        customContentResponse.add(ContentResponseDto.of(
                customContent.getCustomContentId(),
                customContent.getCoverImageUrl(),
                customContent.getTitle(),
                customContent.getSubTitle()
        ));
        return customContentResponse;
    }

    public static DetailContentResponseDto testDetailCustomContentRes() {
        return DetailContentResponseDto.of(1L, testContentRes());
    }

    public static List<SimpleOrganismResponseDto> testSimpleOrganismRes(List<? extends Organism> organismList) {
        return organismList.stream().map(organism ->
            SimpleOrganismResponseDto.of(
                    organism.getOrganismId(),
                    organism.getIllustrationImageUrl(),
                    organism.getKoreanName(),
                    organism.getEnglishName(),
                    organism.getDescription())
        ).collect(Collectors.toList());
    }

    public static OrganismResponseDto testMarineOrganismRes(MarineOrganism marineOrganism, List<MarineOrganism> otherMarineOrganism) {
        return OrganismResponseDto.of(
                marineOrganism.getOrganismId(),
                marineOrganism.getFirstImageUrl(),
                marineOrganism.getKoreanName(),
                marineOrganism.getEnglishName(),
                marineOrganism.getDescription(),
                marineOrganism.getBasicAppearance(),
                marineOrganism.getDetailDescription(),
                testMarineOrganismImageUrlRes(),
                testSimpleOrganismRes(otherMarineOrganism)
        );
    }

    private static List<String> testMarineOrganismImageUrlRes() {
        return MockEntity.testMarineOrganismImage().stream().map(image -> image.getUrl()).collect(Collectors.toList());
    }

    public static OrganismResponseDto testHarmfulOrganismRes(HarmfulOrganism harmfulOrganism, List<HarmfulOrganism> otherHarmfulOrganism) {
        return OrganismResponseDto.of(
                harmfulOrganism.getOrganismId(),
                harmfulOrganism.getFirstImageUrl(),
                harmfulOrganism.getKoreanName(),
                harmfulOrganism.getEnglishName(),
                harmfulOrganism.getDescription(),
                harmfulOrganism.getBasicAppearance(),
                harmfulOrganism.getDetailDescription(),
                testMarineOrganismImageUrlRes(),
                testSimpleOrganismRes(otherHarmfulOrganism)
        );
    }

    public static PartnerResponseDto testPartnerRes(CouponGroup couponGroup) {
        return PartnerResponseDto.from(couponGroup);
    }

    public static PartnerSectionResponseDto testPartnerSectionRes(CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        String partnerSectionKeyword = String.format("%s, %s", firstCouponGroup.getCouponType().getKeyword(), secondCouponGroup.getCouponType().getKeyword());
        String partnerSectionTitle = String.format("%s %s \n할인 참여 업체", firstCouponGroup.getRegion(), partnerSectionKeyword);

        return PartnerSectionResponseDto.of(
                partnerSectionTitle,
                partnerSectionKeyword,
                testPartnerRes(firstCouponGroup),
                testPartnerRes(secondCouponGroup)
        );
    }

    public static CouponSectionResponseDto testCouponSectionRes(CouponGroup couponGroup) {
        return CouponSectionResponseDto.from(couponGroup);
    }

    public static EventResponseDto testEventRes(Event event, CouponGroup firstCouponGroup, CouponGroup secondCouponGroup) {
        return EventResponseDto.of(
                event.getEventId(),
                event.getBannerUrl(),
                event.getMainColor(),
                event.getSubColor(),
                testCouponSectionRes(firstCouponGroup),
                testCouponSectionRes(secondCouponGroup),
                testPartnerSectionRes(firstCouponGroup, secondCouponGroup)
        );
    }

    public static List<NoticeResponseDto> testMarineNoticeRes(MarineNotice marineNotice) {
        List<NoticeResponseDto> noticeResponse = new ArrayList<>();
        noticeResponse.add(NoticeResponseDto.from(marineNotice));
        return noticeResponse;
    }

    public static Content testContentRes() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Content result = mapper.readValue(getCustomContentTestJsonFile(), Content.class);

            return result;
        } catch(Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public static String getCustomContentTestJsonFile() {
        try{
            ClassPathResource resource = new ClassPathResource("custom-content-test-data.json");
            InputStream inputStream = resource.getInputStream();

            return new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ContentResponseDto> testCustomContentRes(CustomContent customContent) {
        List<ContentResponseDto> customContentResponse = new ArrayList<>();
        customContentResponse.add(ContentResponseDto.of(
                customContent.getCustomContentId(),
                customContent.getCoverImageUrl(),
                customContent.getTitle(),
                customContent.getSubTitle()
        ));
        return customContentResponse;
    }

    public static DetailContentResponseDto testDetailCustomContentRes() {
        return DetailContentResponseDto.of(1L, testContentRes());
    }

    public static List<SimpleOrganismResponseDto> testSimpleOrganismRes(List<? extends Organism> organismList) {
        return organismList.stream().map(organism ->
            SimpleOrganismResponseDto.of(
                    organism.getOrganismId(),
                    organism.getIllustrationImageUrl(),
                    organism.getKoreanName(),
                    organism.getEnglishName(),
                    organism.getDescription())
        ).collect(Collectors.toList());
    }

    public static OrganismResponseDto testMarineOrganismRes(MarineOrganism marineOrganism, List<MarineOrganism> otherMarineOrganism) {
        return OrganismResponseDto.of(
                marineOrganism.getOrganismId(),
                marineOrganism.getFirstImageUrl(),
                marineOrganism.getKoreanName(),
                marineOrganism.getEnglishName(),
                marineOrganism.getDescription(),
                marineOrganism.getBasicAppearance(),
                marineOrganism.getDetailDescription(),
                testMarineOrganismImageUrlRes(),
                testSimpleOrganismRes(otherMarineOrganism)
        );
    }

    private static List<String> testMarineOrganismImageUrlRes() {
        return MockEntity.testMarineOrganismImage().stream().map(image -> image.getUrl()).collect(Collectors.toList());
    }

    public static OrganismResponseDto testHarmfulOrganismRes(HarmfulOrganism harmfulOrganism, List<HarmfulOrganism> otherHarmfulOrganism) {
        return OrganismResponseDto.of(
                harmfulOrganism.getOrganismId(),
                harmfulOrganism.getFirstImageUrl(),
                harmfulOrganism.getKoreanName(),
                harmfulOrganism.getEnglishName(),
                harmfulOrganism.getDescription(),
                harmfulOrganism.getBasicAppearance(),
                harmfulOrganism.getDetailDescription(),
                testMarineOrganismImageUrlRes(),
                testSimpleOrganismRes(otherHarmfulOrganism)
        );
    }
}
