package com.oceans7.dib.global.api.response.kakaoAuth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oceans7.dib.global.api.response.kakao.LocalResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenKeyListResponse {

    @JsonProperty("keys")
    private List<JWK> keys;

    @JsonCreator
    public OpenKeyListResponse(@JsonProperty("keys") List<JWK> keys) {
        this.keys = keys;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JWK {
        @JsonProperty("kid")
        private String kid;

        @JsonProperty("kty")
        private String kty;

        @JsonProperty("alg")
        private String alg;

        @JsonProperty("use")
        private String use;

        @JsonProperty("n")
        private String n;

        @JsonProperty("e")
        private String e;
    }
}
