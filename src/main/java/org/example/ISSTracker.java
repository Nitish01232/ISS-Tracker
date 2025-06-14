package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ISSTracker {
    public static void main(String[] args) {
        while (true) {
            try {
                // Step 1: Connect to API
                String apiUrl = "http://api.open-notify.org/iss-now.json";
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Step 2: Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Step 3: Parse JSON
                JSONObject json = new JSONObject(response.toString());
                JSONObject position = json.getJSONObject("iss_position");
                String latitude = position.getString("latitude");
                String longitude = position.getString("longitude");

                // Step 4: Print output
                System.out.println("🌍 ISS Real-Time Location:");
                System.out.println("Latitude:  " + latitude);
                System.out.println("Longitude: " + longitude);
                System.out.println("---------------------------------");

                // Step 5: Wait 10 seconds before next update
                Thread.sleep(10000);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}