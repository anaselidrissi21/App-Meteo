package com.example.appmeteo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;

    public WeatherService(@Value("${openweathermap.api-key:}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String getWeather(String city) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}


