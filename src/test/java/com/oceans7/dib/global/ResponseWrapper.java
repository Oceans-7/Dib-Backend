package com.oceans7.dib.global;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseWrapper<T> {
    @JsonProperty("response")
    private Response response;


    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public static class Response<T> {
        @JsonProperty("items")
        private T items;

        public Response(T items) {
            this.items = items;
        }
    }
}
