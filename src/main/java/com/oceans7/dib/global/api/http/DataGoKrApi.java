package com.oceans7.dib.global.api.http;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface DataGoKrApi {

    @GetExchange("/B551011/KorService1/locationBasedList1")
    String getLocationBasedTourInfo(@RequestParam("serviceKey") String serviceKey,
                                    @RequestParam("MobileOS") String mobileOS,
                                    @RequestParam("MobileApp") String mobileApp,
                                    @RequestParam("_type") String dataType,
                                    @RequestParam("mapX") double mapX,
                                    @RequestParam("mapY") double mapY,
                                    @RequestParam("radius") int radius,
                                    @RequestParam("pageNo") int page,
                                    @RequestParam("numOfRows") int pageSize,
                                    @RequestParam("contentTypeId") String contentTypeId,
                                    @RequestParam("arrange") String arrangeType
    );

    @GetExchange("/B551011/KorService1/searchKeyword1")
    String getSearchKeywordTourInfo(@RequestParam("serviceKey") String serviceKey,
                                    @RequestParam("MobileOS") String mobileOS,
                                    @RequestParam("MobileApp") String mobileApp,
                                    @RequestParam("_type") String dataType,
                                    @RequestParam("keyword") String keyword,
                                    @RequestParam("pageNo") int page,
                                    @RequestParam("numOfRows") int pageSize,
                                    @RequestParam("areaCode")  String areaCode,
                                    @RequestParam("sigunguCode") String sigunguCode,
                                    @RequestParam("contentTypeId") String contentTypeId,
                                    @RequestParam("arrange") String arrangeType);

    @GetExchange("/B551011/KorService1/areaCode1")
    String getAreaCode(@RequestParam("serviceKey") String serviceKey,
                       @RequestParam("MobileOS") String mobileOS,
                       @RequestParam("MobileApp") String mobileApp,
                       @RequestParam("_type") String dataType,
                       @RequestParam("numOfRows") int pageSize,
                       @RequestParam("areaCode")  String areaCode);

    @GetExchange("/B551011/KorService1/areaBasedList1")
    String getAreaBasedTourInfo(@RequestParam("serviceKey") String serviceKey,
                                    @RequestParam("MobileOS") String mobileOS,
                                    @RequestParam("MobileApp") String mobileApp,
                                    @RequestParam("_type") String dataType,
                                    @RequestParam("areaCode")  String areaCode,
                                    @RequestParam("sigunguCode") String sigunguCode,
                                    @RequestParam("pageNo") int page,
                                    @RequestParam("numOfRows") int pageSize,
                                    @RequestParam("contentTypeId") String contentTypeId,
                                    @RequestParam("arrange") String arrangeType);

    @GetExchange("/B551011/KorService1/detailCommon1")
    String getTourCommonInfo(@RequestParam("serviceKey") String serviceKey,
                             @RequestParam("MobileOS") String mobileOS,
                             @RequestParam("MobileApp") String mobileApp,
                             @RequestParam("_type") String dataType,
                             @RequestParam("contentId") Long contentId,
                             @RequestParam("contentTypeId") String contentTypeId,
                             @RequestParam("defaultYN") String option1,
                             @RequestParam("firstImageYN") String option2,
                             @RequestParam("areacodeYN") String option3,
                             @RequestParam("addrinfoYN") String option4,
                             @RequestParam("defaultYN") String option5,
                             @RequestParam("overviewYN") String option6);

    @GetExchange("/B551011/KorService1/detailIntro1")
    String getTourIntroInfo(@RequestParam("serviceKey") String serviceKey,
                            @RequestParam("MobileOS") String mobileOS,
                            @RequestParam("MobileApp") String mobileApp,
                            @RequestParam("_type") String dataType,
                            @RequestParam("contentId") Long contentId,
                            @RequestParam("contentTypeId") String contentTypeId);

    @GetExchange("/B551011/KorService1/detailInfo1")
    String getTourInfo(@RequestParam("serviceKey") String serviceKey,
                       @RequestParam("MobileOS") String mobileOS,
                       @RequestParam("MobileApp") String mobileApp,
                       @RequestParam("_type") String dataType,
                       @RequestParam("contentId") Long contentId,
                       @RequestParam("contentTypeId") String contentTypeId);

    @GetExchange("/B551011/KorService1/detailImage1")
    String getTourImageInfo(@RequestParam("serviceKey") String serviceKey,
                             @RequestParam("MobileOS") String mobileOS,
                             @RequestParam("MobileApp") String mobileApp,
                             @RequestParam("_type") String dataType,
                             @RequestParam("contentId") Long contentId,
                             @RequestParam("subImageYN") String option);

    @GetExchange("/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    String getNowForecastInfo(@RequestParam("serviceKey") String serviceKey,
                              @RequestParam("dataType") String dataType,
                              @RequestParam("nx") int nx,
                              @RequestParam("ny") int ny,
                              @RequestParam("base_date") String baseDate,
                              @RequestParam("base_time") String baseTime,
                              @RequestParam("pageNo") int page,
                              @RequestParam("numOfRows") int pageSize);
}
