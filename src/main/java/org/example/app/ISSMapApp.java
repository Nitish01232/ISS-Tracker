package org.example.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.example.model.ISSPosition;
import org.example.service.ISSService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ISSMapApp extends Application {

    private WebEngine webEngine;
    private final ISSService issService = new ISSService();
    private ScheduledExecutorService executor;

    @Override
    public void start(Stage stage) {

        WebView webView = new WebView();
        webEngine = webView.getEngine();

        webEngine.loadContent(MAP_HTML);

        // Wait until HTML + JS is loaded
        webEngine.getLoadWorker().stateProperty().addListener(
                (obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        startIssUpdates();
                    }
                }
        );

        Scene scene = new Scene(webView, 800, 600);
        stage.setTitle("🛰 Real-Time ISS Tracker");
        stage.setScene(scene);
        stage.show();
    }

    // 🔥 THIS is where the "optimized idea" code goes
    private void startIssUpdates() {
        executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            try {
                ISSPosition pos = issService.getCurrentPosition();

                String js = String.format(
                        "updateISS(%f, %f);",
                        pos.latitude(),
                        pos.longitude()
                );

                Platform.runLater(() ->
                        webEngine.executeScript(js)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public static void main(String[] args) {
        launch();
    }




    // HTML stays INSIDE this class
    private static final String MAP_HTML = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8" />
            <title>ISS Tracker</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet"
                  href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <style>
                #map { height: 100vh; width: 100vw; margin: 0; }
                body { margin: 0; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script
                src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js">
            </script>
            <script>
                var map = L.map('map').setView([0, 0], 2);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png')
                    .addTo(map);

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
}
