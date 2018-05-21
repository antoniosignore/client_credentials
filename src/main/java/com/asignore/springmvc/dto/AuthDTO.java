package com.asignore.springmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    String bearer;

    @JsonProperty("expires_in")
    Long expiresIn;

    String scope;
}
