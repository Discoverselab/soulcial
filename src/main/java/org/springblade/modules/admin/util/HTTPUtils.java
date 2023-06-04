package org.springblade.modules.admin.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

public class HTTPUtils {

    public static String post(String endpointUrl, String requestBody, Map<String, String> requestHeaders) throws Exception {
        URL url = new URL(endpointUrl);

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Set the request headers
        for (String key : requestHeaders.keySet()) {
            conn.setRequestProperty(key, requestHeaders.get(key));
        }

        // Send the request body
        if (requestBody != null && !requestBody.isEmpty()) {
            conn.getOutputStream().write(requestBody.getBytes());
        }

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
	}
}
