package org.springblade.modules.admin.util;

import java.net.URLEncoder;
import java.util.Map;

public class OAuthUtils {

    public static String generateSignatureBaseString(String method, String url, String requestBody, Map<String, String> oauthParameters) throws Exception {
        StringBuilder signatureBaseString = new StringBuilder();
        signatureBaseString.append(method.toUpperCase());
        signatureBaseString.append("&");
        signatureBaseString.append(URLEncoder.encode(url, "UTF-8"));
        signatureBaseString.append("&");
        signatureBaseString.append(URLEncoder.encode(generateParameterString(requestBody, oauthParameters), "UTF-8"));

        return signatureBaseString.toString();
    }

    public static String generateParameterString(String requestBody, Map<String, String> oauthParameters) throws Exception {
        StringBuilder parameterString = new StringBuilder();
        parameterString.append(generateNormalizedParameterString(oauthParameters));
        if (requestBody != null && !requestBody.isEmpty()) {
            parameterString.append("&");
            parameterString.append(URLEncoder.encode(requestBody, "UTF-8"));
        }

        return parameterString.toString();
    }

    public static String generateNormalizedParameterString(Map<String, String> parameters) throws Exception {
        StringBuilder normalizedParameters = new StringBuilder();
        for (String key : parameters.keySet()) {
            if (normalizedParameters.length() > 0) {
                normalizedParameters.append("&");
            }
            normalizedParameters.append(URLEncoder.encode(key, "UTF-8"));
            normalizedParameters.append("=");
            normalizedParameters.append(URLEncoder.encode(parameters.get(key), "UTF-8"));
        }

        return normalizedParameters.toString();
    }

    public static String generateSigningKey(String consumerSecret, String accessTokenSecret) throws Exception {
        StringBuilder signingKey = new StringBuilder();
        signingKey.append(URLEncoder.encode(consumerSecret, "UTF-8"));
        signingKey.append("&");
        signingKey.append(URLEncoder.encode(accessTokenSecret, "UTF-8"));

        return signingKey.toString();
    }
}
