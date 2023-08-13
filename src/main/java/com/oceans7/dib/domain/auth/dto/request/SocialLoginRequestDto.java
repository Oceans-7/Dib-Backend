package com.oceans7.dib.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialLoginRequestDto {

    @NotNull
    @Schema(description = "idToken")
    private String idToken;

    @NotNull
    @Schema(description = "nonce")
    private String nonce;
}
