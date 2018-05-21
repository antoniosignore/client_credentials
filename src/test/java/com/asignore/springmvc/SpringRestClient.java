package com.asignore.springmvc;

public class SpringRestClient {

    public static final String REST_SERVICE_URI = "http://localhost:8080/test";
    public static final String AUTH_SERVER_URI = "http://localhost:8080/test/oauth/token";
    public static final String CLIENT_CREDENTIAL_GRANT = "?grant_type=client_credentials";
    public static final String QPM_ACCESS_TOKEN = "?access_token=";


//    public static void main(String args[]) {
//
//        AuthTokenInfo tokenInfo = sendTokenRequest();
//
//        String access_token = tokenInfo.getAccess_token();
//
//        System.out.println("access_token = " + access_token);
//
//        }


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