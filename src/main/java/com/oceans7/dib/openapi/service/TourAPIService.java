package com.oceans7.dib.openapi.service;

import com.oceans7.dib.domain.place.ContentType;
import com.oceans7.dib.global.util.EncoderUtil;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.image.DetailImageListResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.info.DetailInfoListResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.intro.DetailIntroResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.intro.DetailIntroResponse.*;
import com.oceans7.dib.openapi.dto.response.tourapi.list.AreaCodeList;
import com.oceans7.dib.openapi.dto.response.tourapi.list.TourAPICommonListResponse;
import com.oceans7.dib.openapi.dto.response.tourapi.detail.common.DetailCommonListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TourAPIService extends AbstractOpenAPIService {

    @Value("${open-api.data-go-kr.tour-api.service-key}")
    private String serviceKey;

    @Value("${open-api.data-go-kr.tour-api.callback-url}")
    private String callbackUrl;

    @Value("${open-api.data-go-kr.tour-api.data-type}")
    private String dataType;

    @Value("${open-api.data-go-kr.tour-api.mobile-os}")
    private String mobileOS;

    @Value("${open-api.data-go-kr.tour-api.mobile-app}")
    private String mobileApp;

    private final String YES_OPTION = "Y";

    /**
     * 위치 기반 서비스 API 호출
     * @param mapX
     * @param mapY
     * @param contentTypeId
     * @param arrangeType
     * @param page
     * @param pageSize
     * @return
     */
    public TourAPICommonListResponse fetchDataFromLocationBasedApi(double mapX, double mapY,
                                                                   String contentTypeId, String arrangeType,
                                                                   int page, int pageSize) {
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
                "&contentTypeId=" + contentTypeId +
                "&pageNo=" + page +
                "&numOfRows=" + pageSize +
                "&arrange=" + arrangeType;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     * 키워드 기반 서비스 API 호출
     * @param keyword
     * @param areaCode
     * @param sigunguCode
     * @param contentTypeId
     * @param arrangeTypeCode
     * @return
     */
    public TourAPICommonListResponse fetchDataFromSearchKeywordApi(String keyword,
                                                                   String areaCode, String sigunguCode,
                                                                   String contentTypeId, String arrangeTypeCode,
                                                                   int page, int pageSize) {
        String api = "/searchKeyword1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&keyword=" + EncoderUtil.toURLEncodeUtf8(keyword) +
                "&areaCode=" + areaCode +
                "&sigunguCode=" + sigunguCode +
                "&contentTypeId=" + contentTypeId +
                "&pageNo=" + page +
                "&numOfRows=" + pageSize +
                "&arrange=" + arrangeTypeCode;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     *  지역 코드 조회 API 호출
     * @param areaCode
     * @return
     */
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
     * @param contentTypeId
     * @param arrangeTypeCode
     * @return
     */
    public TourAPICommonListResponse fetchDataFromAreaBasedApi(String areaCode, String sigunguCode,
                                                               String contentTypeId, String arrangeTypeCode,
                                                               int page, int pageSize) {
        String api = "/areaBasedList1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&areaCode=" + areaCode +
                "&sigunguCode=" + sigunguCode +
                "&contentTypeId=" + contentTypeId +
                "&pageNo=" + page +
                "&numOfRows=" + pageSize +
                "&arrange=" + arrangeTypeCode;

        String result = connectApi(urlStr);
        System.out.println(result);
        return parsingJsonObject(result, TourAPICommonListResponse.class);
    }

    /**
     * 공통 정보 조회 (상세 조회용1)
     * @param contentId
     * @param contentTypeId
     * @return
     */
    public DetailCommonListResponse fetchDataFromCommonApi(Long contentId, String contentTypeId) {
        String api = "/detailCommon1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentTypeId +
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
     * @param contentTypeId
     * @return
     */
    public DetailIntroResponse fetchDataFromIntroApi(Long contentId, String contentTypeId) {
        String api = "/detailIntro1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentTypeId;

        String result = connectApi(urlStr);
        System.out.println(result);

        switch(ContentType.getContentTypeByCode(Integer.parseInt(contentTypeId))) {
            case TOURIST_SPOT -> {
                return parsingJsonObject(result, SpotIntroResponse.class);
            }
            case CULTURAL_SITE -> {
                return parsingJsonObject(result, CultureIntroResponse.class);
            }
            case EVENT -> {
                return parsingJsonObject(result, EventIntroResponse.class);
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
     * @param contentTypeId
     * @return
     */
    public DetailInfoListResponse fetchDataFromInfoApi(Long contentId, String contentTypeId) {
        String api = "/detailInfo1";

        String urlStr = callbackUrl + api +
                "?serviceKey=" + serviceKey +
                "&MobileOS=" + mobileOS +
                "&MobileApp=" + mobileApp +
                "&_type=" + dataType +
                "&contentId=" + contentId +
                "&contentTypeId=" + contentTypeId;

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
