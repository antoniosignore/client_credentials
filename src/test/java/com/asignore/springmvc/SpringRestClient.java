package com.asignore.springmvc;

import com.asignore.springmvc.model.AuthTokenInfo;
import com.asignore.springmvc.model.User;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SpringRestClient {

    public static final String REST_SERVICE_URI = "http://localhost:8080/test";
    public static final String AUTH_SERVER_URI = "http://localhost:8080/test/oauth/token";
    public static final String CLIENT_CREDENTIAL_GRANT = "?grant_type=client_credentials";
    public static final String QPM_ACCESS_TOKEN = "?access_token=";

    /*
     * Prepare HTTP Headers.
     */
    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
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
     * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
     */
    @SuppressWarnings({"unchecked"})
    private static AuthTokenInfo sendTokenRequest() {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
        String url = AUTH_SERVER_URI + CLIENT_CREDENTIAL_GRANT;

        System.out.println("url = " + url);

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
            System.out.println(tokenInfo);
        } else {
            System.out.println("No user exist----------");

        }
        return tokenInfo;
    }

    /*
     * Send a GET request to get list of all users.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void listAllUsers(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");

        System.out.println("\nTesting listAllUsers API-----------");
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> usersMap = (List<LinkedHashMap<String, Object>>) response.getBody();

        if (usersMap != null) {
            for (LinkedHashMap<String, Object> map : usersMap) {
                System.out.println("User : id=" + map.get("id") + ", Name=" + map.get("name") + ", Age=" + map.get("age") + ", Salary=" + map.get("salary"));
                ;
            }
        } else {
            System.out.println("No user exist----------");
        }
    }

    /*
     * Send a GET request to get a specific user.
     */
    private static void getUser(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting getUser API----------");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<User> response = restTemplate.exchange(REST_SERVICE_URI + "/user/1" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                HttpMethod.GET, request, User.class);
        User user = response.getBody();
        System.out.println(user);
    }

    /*
     * Send a POST request to create a new user.
     */
    private static void createUser(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting create User API----------");
        RestTemplate restTemplate = new RestTemplate();
        User user = new User(0, "Sarah", 51, 134);
        HttpEntity<Object> request = new HttpEntity<Object>(user, getHeaders());
        URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                request, User.class);
        System.out.println("Location : " + uri.toASCIIString());
    }

    /*
     * Send a PUT request to update an existing user.
     */
    private static void updateUser(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting update User API----------");
        RestTemplate restTemplate = new RestTemplate();
        User user = new User(1, "Tomy", 33, 70000);
        HttpEntity<Object> request = new HttpEntity<Object>(user, getHeaders());
        ResponseEntity<User> response = restTemplate.exchange(REST_SERVICE_URI + "/user/1" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                HttpMethod.PUT, request, User.class);
        System.out.println(response.getBody());
    }

    /*
     * Send a DELETE request to delete a specific user.
     */
    private static void deleteUser(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting delete User API----------");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "/user/3" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                HttpMethod.DELETE, request, User.class);
    }

    /*
     * Send a DELETE request to delete all users.
     */
    private static void deleteAllUsers(AuthTokenInfo tokenInfo) {
        Assert.notNull(tokenInfo, "Authenticate first please......");
        System.out.println("\nTesting all delete Users API----------");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),
                HttpMethod.DELETE, request, User.class);
    }

    public static void main(String args[]) {

        AuthTokenInfo tokenInfo = sendTokenRequest();

        String access_token = tokenInfo.getAccess_token();

        System.out.println("access_token = " + access_token);


//    	listAllUsers(tokenInfo);
//
//    	getUser(tokenInfo);
//
//    	createUser(tokenInfo);
//        listAllUsers(tokenInfo);
//
//        updateUser(tokenInfo);
//        listAllUsers(tokenInfo);
//
//        deleteUser(tokenInfo);
//        listAllUsers(tokenInfo);
//
//        deleteAllUsers(tokenInfo);
//        listAllUsers(tokenInfo);
    }


    /*
    Client credentials grant (section 4.4)
        The simplest of all of the OAuth 2.0 grants, this grant is suitable for machine-to-machine authentication where a specific user’s permission to access data is not required.

        The Flow
        The client sends a POST request with following body parameters to the authorization server:

        grant_type with the value client_credentials
        client_id with the the client’s ID
        client_secret with the client’s secret
        scope with a space-delimited list of requested scope permissions.
        The authorization server will respond with a JSON object containing the following properties:

        token_type with the value Bearer
        expires_in with an integer representing the TTL of the access token
        access_token the access token itself

     */
}