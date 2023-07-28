package com.oceans7.dib.global.api.http;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("v2")
public interface KakaoApi {

    @GetExchange("local/search/address.json")
    String getSearchAddress(@RequestParam("query") String query);

    @GetExchange("local/geo/coord2address.json")
    String getGeoAddress(@RequestParam("x") double x, @RequestParam("y") double y);
}
