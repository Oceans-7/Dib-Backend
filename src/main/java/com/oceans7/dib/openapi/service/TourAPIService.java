package com.oceans7.dib.openapi.service;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.util.EncoderUtil;
import com.oceans7.dib.openapi.dto.response.detail.image.DetailImageListResponse;
import com.oceans7.dib.openapi.dto.response.detail.info.*;
import com.oceans7.dib.openapi.dto.response.detail.intro.*;
import com.oceans7.dib.openapi.dto.response.simple.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.simple.TourAPICommonListResponse;
import com.oceans7.dib.openapi.dto.response.detail.common.DetailCommonListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TourAPIService extends AbstractOpenAPIService {

    @Value("${open-api.data-go-kr.service-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.callback-url}")
    private String callbackUrl;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.mobile-app}")
    private String mobileApp;

    private final String YES_OPTION = "Y";

    // 위치 기반 서비스 API 호출
    // TODO : 정렬 구분 & 지역/시군구 코드 추가
    // TODO : 페이지네이션 추가
    // TODO : 각 응답에 totalCount도 읽어들이도록 추가
    public TourAPICommonListResponse fetchDataFromLocationBasedApi(double mapX, double mapY, ContentType contentType) {
        String api = "/locationBasedList1";
        int radius = 20000;

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&mapX=" + mapX +
                "&mapY=" + mapY +
                "&radius=" + radius +
                "&contentTypeId=" + contentType.getCode();

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    // 키워드 기반 서비스 API 호출
    // TODO : 정렬 구분 & 지역/시군구 코드 추가
    public TourAPICommonListResponse fetchDataFromSearchKeywordApi(String keyword) {
        String api = "/searchKeyword1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&keyword=" + EncoderUtil.toURLEncodeUtf8(keyword);

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    // 지역 코드 조회 API 호출
    public AreaCodeList fetchDataFromAreaCodeApi(String areaCode) {
        String api = "/areaCode1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&numOfRows=" + 50 +
                "&areaCode=" + areaCode;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, AreaCodeList.class);
    }

    /**
     * 지역 기반 관광정보 조회
     * @param areaCode
     * @param sigunguCode
     * @return
     */
    public TourAPICommonListResponse fetchDataFromAreaBasedApi(String areaCode, String sigunguCode) {
        String api = "/areaBasedList1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&areaCode=" + areaCode +
                "&sigunguCode=" + sigunguCode;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     * 공통 정보 조회 (상세 조회용1)
     * @param contentId
     * @param contentType
     * @return
     */
    public DetailCommonListResponse fetchDataFromCommonApi(Long contentId, ContentType contentType) {
        String api = "/detailCommon1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentType.getCode() +
                "&defaultYN=" + YES_OPTION + "&firstImageYN=" + YES_OPTION +
                "&areacodeYN=" + YES_OPTION + "&addrinfoYN=" + YES_OPTION +
                "&mapinfoYN=" + YES_OPTION + "&overviewYN=" + YES_OPTION;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, DetailCommonListResponse.class);
    }

    /**
     * 소개 정보 조회 (상세 조회용2)
     * @param contentId
     * @param contentType
     * @return
     */
    public DetailIntroInterface fetchDataFromIntroApi(Long contentId, ContentType contentType) {
        String api = "/detailIntro1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentType.getCode();

        String result = connectApi(urlStr);
        System.out.println(result);

        switch(contentType) {
            case TOURIST_SPOT -> {
                return parsingJsonObject(result, SpotIntroResponse.class);
            }
            case EVENT -> {
                return parsingJsonObject(result, EventIntroResponse.class);
            }
            case TOUR_COURSE -> {
                return parsingJsonObject(result, TourCourseIntroResponse.class);
            }
            case LEPORTS -> {
                return parsingJsonObject(result, LeportsIntroResponse.class);
            }
            case ACCOMMODATION -> {
                return parsingJsonObject(result, AccommodationIntroResponse.class);
            }
            case SHOPPING -> {
                return parsingJsonObject(result, ShoppingIntroResponse.class);
            }
            case RESTAURANT -> {
                return parsingJsonObject(result, RestaurantIntroResponse.class);
            }
        }

        return null;
    }

    /**
     * 반복 정보 조회 (상세 조회용3 - 시설 정보)
     * 관광지 - 시설 정보
     * @param contentId
     * @param contentType
     * @return
     */
    public DetailInfoListResponse fetchDataFromInfoApi(Long contentId, ContentType contentType) {
        String api = "/detailInfo1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentType.getCode();

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, DetailInfoListResponse.class);
    }

    /**
     * 이미지 조회 (상세 조회용4)
     * @param contentId
     * @return
     */
    public DetailImageListResponse fetchImageDataFromApi(Long contentId) {
        String api = "/detailImage1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&subImageYN=" + YES_OPTION;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, DetailImageListResponse.class);
    }
}
