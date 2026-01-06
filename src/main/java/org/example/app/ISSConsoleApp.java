package org.example.app;

import org.example.model.ISSPosition;
import org.example.service.ISSService;

public class ISSConsoleApp {

    public static void main(String[] args) throws InterruptedException {
        ISSService service = new ISSService();

        while (true) {
            try {
                ISSPosition pos = service.getCurrentPosition();

                System.out.println("🛰 ISS Location");
                System.out.printf("Latitude : %.4f%n", pos.latitude());
                System.out.printf("Longitude: %.4f%n", pos.longitude());
                System.out.println("--------------------------");

                Thread.sleep(10_000);

            } catch (Exception e) {
                System.err.println("Failed to fetch ISS data");
                e.printStackTrace();
                break;
            }
        }
    }
}
