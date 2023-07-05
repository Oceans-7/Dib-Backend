package com.oceans7.dib.global.api.http;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("v2")
public interface KakaoApi {

    @GetExchange("local/search/address.{dataType}")
    String getSearchAddress(@PathVariable("dataType") String dataType,
                            @RequestParam("query") String query);
}
