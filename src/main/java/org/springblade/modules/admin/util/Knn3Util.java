package org.springblade.modules.admin.util;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Knn3Util {
    public static void main(String[] args) throws Exception {
        String knn3ApiKey = "a97c41edaf7bbcde96dfc31ab15226a2e09c1d15913551f85d56847d3ee10ef8";

//        String bearerToken = getBearerToken(apiKey, apiSecretKey);
//        System.out.println("Bearer token: " + bearerToken);
    }

    private static String getBearerToken(String apiKey, String apiSecretKey) throws Exception {
        String url = "https://api.twitter.com/oauth2/token";
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection(proxy);

        String authString = apiKey + ":" + apiSecretKey;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
		System.out.println(authStringEnc);

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
//		conn.setRequestProperty("consumer_key","1665007675040735232-cEk9Sl5SBW7oWKTmnCdfUh08t8x9zy");
//		conn.setRequestProperty("consumer_secret","ZEx7HkjG9wIwHiQuUdktJBnJwcBDfwJrtQr0uYI9M75WG");
//		conn.setRequestProperty("bearer_token","AAAAAAAAAAAAAAAAAAAAABfNnwEAAAAAEFNhi8q0RiSRjnDeq5O678JF%2BZo%3DuEBA5qtLztNGBQHpF0NQ1LRtBa3SdFHiZFLrXCcwuyGd0DMXI2");
        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        String requestBody = "grant_type=client_credentials";
        conn.getOutputStream().write(requestBody.getBytes("UTF-8"));
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        JSONObject jsonObject = new JSONObject(sb.toString());
        return jsonObject.getString("access_token");
    }
}
