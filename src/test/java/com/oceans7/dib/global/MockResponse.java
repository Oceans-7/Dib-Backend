package com.oceans7.dib.global;

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

import java.util.ArrayList;
import java.util.List;

public class MockResponse {
    private static final double X = 126.997555182293;
    private static final double Y = 37.5638077703601;

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

    private static TourAPICommonItemResponse setCommonTourItem(double dist, String zipcode) {
        return new TourAPICommonItemResponse(
                (long) 2946230, 12,
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
                (long) 2946230, 12,
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
        return new DetailInfoItemResponse((long) 2946230, 12, "화장실", "있음");
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
        item.add(new DetailImageItemResponse((long) 2946230, "http://tong.visitkorea.or.kr/cms/resource/50/2947650_image2_1.jpg"));
        item.add(new DetailImageItemResponse((long) 2946230, "http://tong.visitkorea.or.kr/cms/resource/52/2947652_image2_1.jpg"));
        item.add(new DetailImageItemResponse((long) 2946230, "http://tong.visitkorea.or.kr/cms/resource/51/2947651_image2_1.jpg"));
        item.add(new DetailImageItemResponse((long) 2946230, "http://tong.visitkorea.or.kr/cms/resource/53/2947653_image2_1.jpg"));
        return item;
    }

    public static ResponseWrapper testDetailImageRes() {
        return new ResponseWrapper(
                new Response(new DetailImageListResponse(setDetailImageItem()))
        );
    }

    private static List<FcstAPICommonItemResponse> setNcstItem() {
        List<FcstAPICommonItemResponse> item = new ArrayList<>();
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "0", "PTY", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "89","REH", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "0", "RN1", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "26.1", "T1H", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "1", "UUU", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "205", "VEC", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "2.1", "VVV", null, null, null));
        item.add(new FcstAPICommonItemResponse("20230726", "0100", "2.3", "WSD", null, null, null));
        return item;
    }

    public static ResponseWrapper testNcstRes() {
        return new ResponseWrapper(
                new Response(new FcstAPICommonListResponse(setNcstItem()))
        );
    }

    private static List<FcstAPICommonItemResponse> setFcstItem() {
        List<FcstAPICommonItemResponse> item = new ArrayList<>();
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "LGT", "20230726", "0100", "0"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "PTY", "20230726", "0100", "0"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "RN1", "20230726", "0100", "강수없음"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "SKY", "20230726", "0100", "4"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "T1H", "20230726", "0100", "25"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "REH", "20230726", "0100", "85"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "UUU", "20230726", "0100", "1.7"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "VVV", "20230726", "0100", "1.2"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "VEC", "20230726", "0100", "228"));
        item.add(new FcstAPICommonItemResponse("20230726", "0000", null, "WSD", "20230726", "0100", "2"));
        return item;
    }

    public static ResponseWrapper testFcstRes() {
        return new ResponseWrapper(
                new Response(new FcstAPICommonListResponse(setFcstItem()))
        );
    }
}
