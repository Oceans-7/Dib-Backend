package com.oceans7.dib.global.api.http;

import com.oceans7.dib.domain.weather.dto.ObsCode;
import com.oceans7.dib.global.api.http.KhoaDataType.KhoaDataType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface KhoaGoKrApi {

    @GetExchange("/api/oceangrid/{dataType}/search.do")
    String getValue(@PathVariable("dataType") String dataType,
                    @RequestParam("ServiceKey") String serviceKey,
                    @RequestParam("ObsCode") String obsCode,
                    @RequestParam("Date") String date,
                    @RequestParam("ResultType") String resultType
    );

    @GetExchange("/api/oceangrid/{dataType}/search.do")
    String getValue(@PathVariable("dataType") String dataType,
                    @RequestParam("ServiceKey") String serviceKey,
                    @RequestParam("Type") String Type,
                    @RequestParam("ResultType") String resultType
    );


}
