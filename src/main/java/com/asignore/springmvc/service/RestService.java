package com.asignore.springmvc.service;

import com.asignore.springmvc.dto.StatDTO;
import com.asignore.springmvc.dto.ValueDTO;
import com.asignore.springmvc.model.AuthTokenInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;

@Component
public class RestService {

    public static final String AUTH_SERVER_URI = "http://localhost:8080/test/oauth/token";
    public static final String CLIENT_CREDENTIAL_GRANT = "?grant_type=client_credentials";

    /*
     * Prepare HTTP Headers.
     */
    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /*
     * Add HTTP Authorization header, using Basic-Authentication to send client-credentials.
     */
    private static HttpHeaders getHeadersWithClientCredentials() {
        String plainClientCredentials = "coding_test:bwZm5XC6HTlr3fcdzRnD";
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
        HttpHeaders headers = getHeaders();
        headers.add("Authorization", "Basic " + base64ClientCredentials);
        return headers;
    }


    /*
     * Add HTTP Authorization header, using Bearer-Authentication to send client-credentials.
     */
    private static HttpHeaders getHeadersWithBearerCredentials(String accessToken) {
        HttpHeaders headers = getHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }


    /*
     * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
     */
    @SuppressWarnings({"unchecked"})
    public static AuthTokenInfo sendTokenRequest() {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
        String url = AUTH_SERVER_URI + CLIENT_CREDENTIAL_GRANT;

        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();
        AuthTokenInfo tokenInfo = null;

        if (map != null) {
            tokenInfo = new AuthTokenInfo();
            tokenInfo.setAccess_token((String) map.get("access_token"));
            tokenInfo.setToken_type((String) map.get("token_type"));
            tokenInfo.setRefresh_token((String) map.get("refresh_token"));
            tokenInfo.setExpires_in((int) map.get("expires_in"));
            tokenInfo.setScope((String) map.get("scope"));
        }
        return tokenInfo;
    }


    @SuppressWarnings({"unchecked"})
    public static StatDTO risk(ValueDTO value, String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request = new HttpEntity<String>(
                mapper.writeValueAsString(value),
                getHeadersWithBearerCredentials(accessToken));
        String url = "http://localhost:8080/test/api/v1.0/risk";
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();
        if (map != null) {
            StatDTO dto = new StatDTO();
            dto.setValue(value.getValue());
            dto.setStat(Integer.parseInt(map.get("stat").toString()));
            return dto;

        }
        throw new IllegalStateException("Error calling the risk engine");
    }

}