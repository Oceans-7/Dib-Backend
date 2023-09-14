package com.oceans7.dib.global;

import com.oceans7.dib.domain.location.dto.response.LocationResponseDto;
import com.oceans7.dib.domain.place.dto.ArrangeType;
import com.oceans7.dib.domain.place.dto.FacilityType;
import com.oceans7.dib.domain.place.dto.response.*;
import com.oceans7.dib.domain.weather.dto.WeatherType;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonItemResponse;
import com.oceans7.dib.global.api.response.fcstapi.FcstAPICommonListResponse;
import com.oceans7.dib.global.api.response.kakao.Address;
import com.oceans7.dib.global.api.response.kakao.AddressItem;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
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
import com.oceans7.dib.global.util.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceans7.dib.global.MockRequest.*;

public class MockResponse {
    public final static double MIN_DISTANCE = 0.0;
    public final static double MAX_DISTANCE = 20.0;

    // --- KakaoLocalAPIService Test Mock Response
    private static Address setAddress() {
        return Address.builder()
                .addressName("서울 중구")
                .region1depthName("서울")
                .region2depthName("중구")
                .build();
    }

    private static Address setRoadAddress() {
        return Address.builder()
                .addressName("서울특별시 중구 창경궁로 17")
                .region1depthName("서울")
                .region2depthName("중구")
                .build();
    }

    private static AddressItem setAddressItem(Address address, Address roadAddress) {
        return AddressItem.builder()
                .address(address)
                .roadAddress(roadAddress)
                .addressName("서울 중구")
                .addressType("REGION")
                .x(X)
                .y(Y)
                .build();
    }

    private static LocalResponse setALocalResponse(List<AddressItem> localAPIItemList) {
        return LocalResponse.builder()
                .addressItems(localAPIItemList)
                .build();
    }

    public static LocalResponse testSearchAddressRes() {
        List<AddressItem> localAPIItemList = new ArrayList<>();
        localAPIItemList.add(setAddressItem(setAddress(), null));
        return setALocalResponse(localAPIItemList);
    }

    public static LocalResponse testGeoAddressRes() {
        List<AddressItem> localAPIItemList = new ArrayList<>();
        localAPIItemList.add(setAddressItem(null, setRoadAddress()));
        return setALocalResponse(localAPIItemList);
    }

    // --- DataGoKrAPIService Test Mock Response
    private static TourAPICommonItemResponse setCommonTourItem(double dist) {
        return TourAPICommonItemResponse.builder()
                .contentId(CONTENT_ID)
                .contentTypeId(CONTENT_TYPE.getCode())
                .title("뷰티플레이")
                .tel("")
                .thumbnail("http://tong.visitkorea.or.kr/cms/resource/49/2947649_image3_1.jpg")
                .addr1("서울특별시 중구 명동1가 1-3 YWCA연합회")
                .addr2("")
                .mapX(X)
                .mapY(Y)
                .distance(dist)
                .areaCode("1")
                .sigunguCode("24")
                .modifiedTime("20230214113655")
                .build();
    }

    private static TourAPICommonItemResponse setCommonTourItem2(double dist) {
        return TourAPICommonItemResponse.builder()
                .contentId(CONTENT_ID)
                .contentTypeId(CONTENT_TYPE.getCode())
                .title("서울 남현동 요지")
                .tel("")
                .thumbnail("http://tong.visitkorea.or.kr/cms/resource/49/2947649_image3_1.jpg")
                .addr1("서울특별시 관악구 남현3길 60")
                .addr2("")
                .mapX(X)
                .mapY(Y)
                .distance(dist)
                .areaCode("1")
                .sigunguCode("24")
                .modifiedTime("20230214113655")
                .build();
    }

    private static TourAPICommonListResponse setTourAPIResponse(List<TourAPICommonItemResponse> tourAPIItemList, int totalCount, int page, int pageSize) {
        return TourAPICommonListResponse.builder()
                .tourAPICommonItemResponseList(tourAPIItemList)
                .totalCount(totalCount)
                .page(page)
                .pageSize(pageSize)
                .build();
    }
    public static ResponseWrapper testLocationBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(1000.1711716167842));
        return new ResponseWrapper(
                new Response(setTourAPIResponse(item, 1, 1, 1))
        );
    }

    public static ResponseWrapper testAreaBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(0));
        return new ResponseWrapper(
                new Response(setTourAPIResponse(item, 1, 1, 1))
        );
    }

    public static ResponseWrapper testKeywordBasedRes() {
        List<TourAPICommonItemResponse> item = new ArrayList<>();
        item.add(setCommonTourItem(0));
        return new ResponseWrapper(
                new Response(setTourAPIResponse(item, 1, 1, 1))
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
        return new DetailCommonItemResponse(setCommonTourItem(0),"www.beautyplay.kr", "");
    }

    public static ResponseWrapper testDetailCommonRes() {
        return new ResponseWrapper(
                new Response(new DetailCommonListResponse(setDetailCommonItem(), 1))
        );
    }

    private static SpotItemResponse setDetailIntroItem() {
        return new SpotItemResponse(
                "070-4070-9675", "", "", "", "",
                "일요일", "10:00~19:00(뷰티 체험은 18:00까지)");
    }

    public static ResponseWrapper testDetailIntroRes() {
        return new ResponseWrapper(
                new Response(new SpotIntroResponse(setDetailIntroItem()))
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
        List<TourAPICommonItemResponse> tourAPIItemList = new ArrayList<>();
        tourAPIItemList.add(setCommonTourItem(1000.1711716167842));
        tourAPIItemList.add(setCommonTourItem2(10001.983304508862));
        return setTourAPIResponse(tourAPIItemList, 1, 1, 2);
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
        List<TourAPICommonItemResponse> tourAPIItemList = new ArrayList<>();
        tourAPIItemList.add(setCommonTourItem(1000.1711716167842));
        return setTourAPIResponse(tourAPIItemList, 1, 1, 1);
    }

    public static LocalResponse testSearchNoAddressRes() {
        return new LocalResponse(null);
    }

    public static TourAPICommonListResponse testSearchRes() {
        List<TourAPICommonItemResponse> tourAPIItemList = new ArrayList<>();
        tourAPIItemList.add(setCommonTourItem(1000.1711716167842));
        return setTourAPIResponse(tourAPIItemList, 1, 1, 1);
    }

    public static TourAPICommonListResponse testNoResultRes() {
        List<TourAPICommonItemResponse> tourAPIItemList = new ArrayList<>();
        return setTourAPIResponse(tourAPIItemList, 0, 0, 0);
    }

    public static DetailCommonListResponse testPlaceCommonRes() {
        return new DetailCommonListResponse(setDetailCommonItem(), 1);
    }

    public static SpotIntroResponse testPlaceIntroRes() {
        return new SpotIntroResponse(setDetailIntroItem());
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

        return PlaceResponseDto.of(simpleDto, testPlaceRes().getTotalCount(), testPlaceRes().getPage(), testPlaceRes().getPageSize(), arrangeType);
    }

    public static PlaceResponseDto testGetPlaceBasedAreaRes() {
        ArrangeType arrangeType = null;
        List<SimplePlaceInformationDto> simpleDto = testAreaPlaceRes().getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .collect(Collectors.toList());

        return PlaceResponseDto.of(simpleDto, testAreaPlaceRes().getTotalCount(), testAreaPlaceRes().getPage(), testAreaPlaceRes().getPageSize(), arrangeType);
    }

    public static SearchPlaceResponseDto testSearchPlaceBasedKeywordRes() {
        List<SimplePlaceInformationDto> simpleDto = testSearchRes().getTourAPICommonItemResponseList().stream()
                .map(SimplePlaceInformationDto :: of)
                .collect(Collectors.toList());

        return SearchPlaceResponseDto.of(KEYWORD_QUERY, simpleDto, false, testSearchRes().getTotalCount(), testSearchRes().getPage(), testSearchRes().getPageSize());
    }

    public static  SearchPlaceResponseDto testSearchPlaceBasedAreaRes() {
        List<SimpleAreaResponseDto> simpleDto = new ArrayList<>();
        simpleDto.add(SimpleAreaResponseDto.of("서울 중구", "서울", "중구", X, Y, 1000.1711716167842));
        return SearchPlaceResponseDto.of(AREA_QUERY, simpleDto, true);
    }

    public static DetailPlaceInformationResponseDto testGetDetailPlaceRes() {
        DetailCommonItemResponse commonApiResponseMock = setDetailCommonItem();
        SpotItemResponse introApiReponseApi = setDetailIntroItem();

        return DetailPlaceInformationResponseDto.of(CONTENT_ID, CONTENT_TYPE, commonApiResponseMock.getTitle(), commonApiResponseMock.getAddress(),
                commonApiResponseMock.getMapX(), commonApiResponseMock.getMapY(), commonApiResponseMock.extractOverview(), commonApiResponseMock.extractHomepageUrl(),
                introApiReponseApi.extractUseTime(), introApiReponseApi.extractTel(), introApiReponseApi.extractRestDate(), introApiReponseApi.extractReservationUrl(), introApiReponseApi.extractEventDate(),
                testFacilityInfo(), testImageUrlList());
    }

    public static List<DetailPlaceInformationResponseDto.FacilityInfo> testFacilityInfo() {
        SpotItemResponse introApiReponseApi = setDetailIntroItem();

        List<DetailPlaceInformationResponseDto.FacilityInfo> facilityInfo = new ArrayList<>();
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.BABY_CARRIAGE, ValidatorUtil.checkAvailability(introApiReponseApi.getCheckBabyCarriage())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.CREDIT_CARD, ValidatorUtil.checkAvailability(introApiReponseApi.getCheckCreditCard())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.PET, ValidatorUtil.checkAvailability(introApiReponseApi.getCheckPet())));
        facilityInfo.add(DetailPlaceInformationResponseDto.FacilityInfo.of(FacilityType.RESTROOM, true));
        return facilityInfo;
    }

    public static List<String> testImageUrlList() {
        List<DetailImageItemResponse> imageItems = setDetailImageItem();
        return imageItems.stream()
                .map(image -> image.getOriginImageUrl())
                .collect(Collectors.toList());
    }
}
