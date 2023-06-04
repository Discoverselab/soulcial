package org.springblade.modules.admin.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TwitterOAuthPublish {

    private static final String CONSUMER_KEY = "g0mikCnfhj22dBl36gtwkr5U1";
    private static final String CONSUMER_SECRET = "4AuHgi46xKZae9XGSOnOYQIMi0FAuIHDqQ8c2WcjWxGjZHjqOY";
    private static final String ACCESS_TOKEN = "1665007675040735232-e5dvPpxgwSbY44YwPxLzMYy8WQUoc7";
    private static final String ACCESS_TOKEN_SECRET = "PXvRQsdMuk2YE0KthlFAWVQbZu9VueDHGMl13InpoTi9S";

    private static final String BASE_URL = "https://api.twitter.com/1.1/statuses/update.json";

    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter your tweet: ");
//        String tweetText = scanner.nextLine();
//        scanner.close();

		String tweetText = "i want to sleep ! 3:57";

        Map<String, String> oauthParams = new HashMap<>();
        oauthParams.put("oauth_consumer_key", CONSUMER_KEY);
        oauthParams.put("oauth_nonce", String.valueOf(System.currentTimeMillis()));
        oauthParams.put("oauth_signature_method", "HMAC-SHA1");
        oauthParams.put("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        oauthParams.put("oauth_token", ACCESS_TOKEN);
        oauthParams.put("oauth_version", "1.0");

        String signatureBaseString = getSignatureBaseString(oauthParams, tweetText);
        String signingKey = getSigningKey();
        String oauthSignature = sign(signatureBaseString, signingKey);

        oauthParams.put("oauth_signature", oauthSignature);

        String authHeader = getAuthorizationHeader(oauthParams);

        URL url = new URL(BASE_URL);

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authHeader);
        connection.setDoOutput(true);
        connection.getOutputStream().write(("status=" + tweetText).getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();

        connection.disconnect();
    }

    private static String getSignatureBaseString(Map<String, String> oauthParams, String tweetText) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("POST&");
        sb.append(urlEncode(BASE_URL)).append("&");

        Map<String, String> params = new HashMap<>(oauthParams);
        params.put("status", tweetText);

        boolean first = true;
        for (String key : params.keySet()) {
            if (!first) {
                sb.append("%26");
            }
            first = false;
            sb.append(urlEncode(key)).append("%3D").append(urlEncode(params.get(key)));
        }

        return sb.toString();
    }

    private static String getSigningKey() throws Exception {
        String encodedConsumerSecret = urlEncode(CONSUMER_SECRET);
        String encodedAccessTokenSecret = urlEncode(ACCESS_TOKEN_SECRET);
        String signingKey = encodedConsumerSecret + "&" + encodedAccessTokenSecret;
        return signingKey;
    }

    private static String sign(String data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String getAuthorizationHeader(Map<String, String> oauthParams) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");

        boolean first = true;
        for (String key : oauthParams.keySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(urlEncode(key)).append("=\"").append(urlEncode(oauthParams.get(key))).append("\"");
        }

        return sb.toString();
    }

    private static String urlEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
