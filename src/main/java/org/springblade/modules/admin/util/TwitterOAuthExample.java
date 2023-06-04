package org.springblade.modules.admin.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;

public class TwitterOAuthExample {

    public static void main(String[] args) {
        try {
            // Step 1: Build the URL for the OAuth 2.0 Token Endpoint
            String oauthEndpoint = "https://api.twitter.com/oauth2/token";
            URL url = new URL(oauthEndpoint);

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));

            // Step 2: Set up the HTTP POST request
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Step 3: Encode the Client ID and Client Secret as a Base64-encoded string
            String clientId = "X05OQUhkbjNxTVVOUFJqMnI0c0g6MTpjaQ";
            String clientSecret = "hlzjkrw2D3KxF4_4PCmEMwVH2iJpiFpPSWjeRh5yrBX8zGCKM7";
            String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

            // Step 4: Set up the Authorization header with the encoded credentials
            conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);

			System.out.println("Basic " + encodedCredentials);

            // Step 5: Set up the request parameters
            String grantType = "password";
            String username = "peichao8030";
            String password = "PCp@ssw0rd";
            String requestBody = "grant_type=" + grantType + "&username=" + username + "&password=" + password;

            // Step 6: Send the request
            conn.getOutputStream().write(requestBody.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            // Step 7: Extract the access token from the response
            String accessToken = response.substring(response.indexOf(":") + 2, response.indexOf(",") - 1);
            System.out.println("Access Token: " + accessToken);

            // Step 8: Use the access token to make API requests
            // ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
