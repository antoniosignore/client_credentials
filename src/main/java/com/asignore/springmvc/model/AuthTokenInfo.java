package com.asignore.springmvc.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthTokenInfo {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;

}
