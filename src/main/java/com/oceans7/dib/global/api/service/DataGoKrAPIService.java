package com.oceans7.dib.global.api.service;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.response.tourapi.detail.common.DetailCommonListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.global.api.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.global.api.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.global.api.response.tourapi.list.TourAPICommonListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataGoKrAPIService extends OpenAPIService {

    private final DataGoKrApi dataGoKrApi;

    public DataGoKrAPIService(DataGoKrApi dataGoKrApi) {
        this.dataGoKrApi = dataGoKrApi;
    }

    @Value("${open-api.data-go-kr.service-decode-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.mobile-app}")
    private String mobileApp;

    private final static String YES_OPTION = "Y";
    private final static int RADIUS = 20000;

    /**
     * 위치 기반 관광 정보 조회 API
     */
    public TourAPICommonListResponse getLocationBasedTourApi(double mapX, double mapY, int page, int pageSize,
                                                             String contentTypeId, String arrangeType) {
        String result = dataGoKrApi.getLocationBasedTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                mapX, mapY, RADIUS, page, pageSize,
                contentTypeId, arrangeType);

        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     * 키워드 검색 관광 정보 조회 API
     */
    public TourAPICommonListResponse getSearchKeywordTourApi(String keyword, int page, int pageSize) {

        String result = dataGoKrApi.getSearchKeywordTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                keyword, page, pageSize);

        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     *  지역 코드 조회 API
     */
    public AreaCodeList getAreaCodeApi(String areaCode) {
        String result = dataGoKrApi.getAreaCode(serviceKey, mobileOS, mobileApp, dataType,
                50, areaCode);
        return parsingJsonObject(result, AreaCodeList.class);
    }

    /**
     * 지역 기반 관광정보 조회 API
     */
    public TourAPICommonListResponse getAreaBasedTourApi(String areaCode, String sigunguCode, int page, int pageSize,
                                                               String contentTypeId, String arrangeType) {

        String result = dataGoKrApi.getAreaBasedTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                areaCode, sigunguCode, page, pageSize,
                contentTypeId, arrangeType);

        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     * 공통 정보 조회 API (상세 조회)
     */
    public DetailCommonListResponse getCommonApi(Long contentId, String contentTypeId) {
        String result = dataGoKrApi.getTourCommonInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, contentTypeId,
                YES_OPTION, YES_OPTION, YES_OPTION, YES_OPTION, YES_OPTION, YES_OPTION);

        return parsingJsonObject(result, DetailCommonListResponse.class);
    }

    /**
     * 소개 정보 조회 API (상세 조회)
     */
    public DetailIntroResponse getIntroApi(Long contentId, String contentTypeId) {

        String result = dataGoKrApi.getTourIntroInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, contentTypeId);

        switch(ContentType.getContentTypeByCode(Integer.parseInt(contentTypeId))) {
            case TOURIST_SPOT -> { return parsingJsonObject(result, DetailIntroResponse.SpotIntroResponse.class); }
            case CULTURAL_SITE -> { return parsingJsonObject(result, DetailIntroResponse.CultureIntroResponse.class); }
            case EVENT -> { return parsingJsonObject(result, DetailIntroResponse.EventIntroResponse.class); }
            case LEPORTS -> { return parsingJsonObject(result, DetailIntroResponse.LeportsIntroResponse.class); }
            case ACCOMMODATION -> { return parsingJsonObject(result, DetailIntroResponse.AccommodationIntroResponse.class); }
            case SHOPPING -> { return parsingJsonObject(result, DetailIntroResponse.ShoppingIntroResponse.class); }
            case RESTAURANT -> { return parsingJsonObject(result, DetailIntroResponse.RestaurantIntroResponse.class); }
            default -> { return null; }
        }
    }

    /**
     * 반복 정보 조회 API (상세 조회)
     */
    public DetailInfoListResponse getInfoApi(Long contentId, String contentTypeId) {
        String result = dataGoKrApi.getTourInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, contentTypeId);

        return parsingJsonObject(result, DetailInfoListResponse.class);
    }

    /**
     * 이미지 조회 (상세 조회용4)
     */
    public DetailImageListResponse getImageApi(Long contentId) {


        String result = dataGoKrApi.getTourImageInfo(serviceKey, mobileOS, mobileApp, dataType,
                contentId, YES_OPTION);

        return parsingJsonObject(result, DetailImageListResponse.class);
    }
}
