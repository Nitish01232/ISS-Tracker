package org.example.service;

import org.example.model.ISSPosition;
import org.example.network.ISSApiClient;
import org.json.JSONObject;

public class ISSService {

    private final ISSApiClient apiClient = new ISSApiClient();

    public ISSPosition getCurrentPosition() throws Exception {
        String json = apiClient.fetchRawJson();

        JSONObject root = new JSONObject(json);
        JSONObject pos = root.getJSONObject("iss_position");

        double lat = pos.getDouble("latitude");
        double lon = pos.getDouble("longitude");

        return new ISSPosition(lat, lon);
    }
}
