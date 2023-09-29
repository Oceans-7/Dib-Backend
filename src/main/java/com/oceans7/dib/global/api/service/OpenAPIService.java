package com.oceans7.dib.global.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OpenAPIService {

    <T> T parsingJsonObject(String json, Class<T> valueType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T result = mapper.readValue(json, valueType);

            return result;
        } catch (ValueInstantiationException e) {
            log.error(e.getMessage());
            throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION);
        } catch(Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.NOT_FOUND_ITEM_EXCEPTION);
        }
    }
}
