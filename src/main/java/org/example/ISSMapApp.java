package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.concurrent.Worker;

public class ISSMapApp extends Application {

    private static final String MAP_HTML = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8" />
            <title>ISS Tracker</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <style>
                #map { height: 100vh; width: 100vw; margin: 0; padding: 0; }
                body { margin: 0; padding: 0; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <script>
                var map = L.map('map').setView([0, 0], 2);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 18,
                }).addTo(map);

                var marker = L.marker([0, 0]).addTo(map);

                function updateISS(lat, lon) {
                    marker.setLatLng([lat, lon]);
                    map.panTo([lat, lon]);
                }

                window.updateISS = updateISS;
            </script>
        </body>
        </html>
        """;

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.loadContent(MAP_HTML);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                new Thread(() -> {
                    while (true) {
                        try {
                            String[] coords = fetchISSCoordinates();
                            double lat = Double.parseDouble(coords[0]);
                            double lon = Double.parseDouble(coords[1]);
                            String js = String.format("updateISS(%f, %f);", lat, lon);

                            javafx.application.Platform.runLater(() -> {
                                try {
                                    webEngine.executeScript(js);
                                } catch (Exception e) {
                                    System.out.println("JS Error: " + e.getMessage());
                                }
                            });

                            Thread.sleep(5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        Scene scene = new Scene(webView, 800, 600);
        stage.setTitle("🛰 Real-Time ISS Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private String[] fetchISSCoordinates() throws Exception {
        String url = "http://api.open-notify.org/iss-now.json";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            result.append(line);
        }

        in.close();

        String data = result.toString();
        String lat = data.split("\"latitude\":")[1].split(",")[0].replace("\"", "").trim();
        String lon = data.split("\"longitude\":")[1].split("}")[0].replace("\"", "").trim();
        return new String[]{lat, lon};
    }

    public static void main(String[] args) {
        launch();
    }
}