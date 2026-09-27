package com.healthdesk.service;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HealthApiService {

    // Free public API - returns a random piece of advice/tip as JSON
    private static final String API_URL = "https://api.adviceslip.com/advice";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(6))
            .build();

    public static class HealthTip {
        public final int    id;
        public final String tip;

        public HealthTip(int id, String tip) {
            this.id  = id;
            this.tip = tip;
        }
    }

    public HealthTip fetchTip() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofSeconds(8))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;

            // JSON parsing with org.json library
            JSONObject json    = new JSONObject(response.body());
            JSONObject slip    = json.getJSONObject("slip");
            int        id      = slip.getInt("id");
            String     advice  = slip.getString("advice");

            return new HealthTip(id, advice);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
