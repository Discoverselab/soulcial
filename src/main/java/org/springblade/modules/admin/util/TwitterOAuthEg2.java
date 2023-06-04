package org.springblade.modules.admin.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class TwitterOAuthEg2 {

    public static void main(String[] args) {
        try {
            // Step 1: Set up the OAuth parameters
            String consumerKey = "g0mikCnfhj22dBl36gtwkr5U1";
            String consumerSecret = "4AuHgi46xKZae9XGSOnOYQIMi0FAuIHDqQ8c2WcjWxGjZHjqOY";
            String accessToken = "1665007675040735232-e5dvPpxgwSbY44YwPxLzMYy8WQUoc7";
            String accessTokenSecret = "PXvRQsdMuk2YE0KthlFAWVQbZu9VueDHGMl13InpoTi9S";
            String oauthSignatureMethod = "HMAC-SHA1";
            String oauthVersion = "1.0";
            String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000L);
            String oauthNonce = Long.toString(System.nanoTime());
//            String status = URLEncoder.encode("Hello, Twitter!", "UTF-8");
            String status = URLEncoder.encode("i want to sleep!", "UTF-8");
//            String endpointUrl = "https://api.twitter.com/1.1/statuses/update.json";
            String endpointUrl = "https://api.twitter.com/2/tweets";

            Map<String, String> oauthParameters = new HashMap<>();
            oauthParameters.put("oauth_consumer_key", consumerKey);
            oauthParameters.put("oauth_token", accessToken);
            oauthParameters.put("oauth_signature_method", oauthSignatureMethod);
            oauthParameters.put("oauth_timestamp", oauthTimestamp);
            oauthParameters.put("oauth_nonce", oauthNonce);
            oauthParameters.put("oauth_version", oauthVersion);

            // Step 2: Generate the signature base string and signing key
            String signatureBaseString = OAuthUtils.generateSignatureBaseString("POST", endpointUrl, status, oauthParameters);
            String signingKey = OAuthUtils.generateSigningKey(consumerSecret, accessTokenSecret);

            // Step 3: Compute the signature using HMAC-SHA1 algorithm and Base64 encoding
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            mac.init(secret);
            byte[] bytes = mac.doFinal(signatureBaseString.getBytes());
            String signature = new String(Base64.encodeBase64(bytes));

            // Step 4: Include the signature in the OAuth parameters
            oauthParameters.put("oauth_signature", signature);

            // Step 5: Send the request and handle the response
            String response = HTTPUtils.post(endpointUrl, status, oauthParameters);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
