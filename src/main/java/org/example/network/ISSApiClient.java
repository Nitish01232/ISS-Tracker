package org.example.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ISSApiClient {

    private static final String API_URL =
            "http://api.open-notify.org/iss-now.json";

    public String fetchRawJson() throws Exception {
        HttpURLConnection conn =
                (HttpURLConnection) new URL(API_URL).openConnection();

        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
