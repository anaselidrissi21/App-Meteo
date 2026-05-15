package com.example.appmeteo;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    private static final String GEOCODING_API_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String FORECAST_API_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {
        if (city == null || city.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City is required");
        }

        Map<String, Object> location = findLocation(city.trim());
        Map<String, Object> currentWeather = findCurrentWeather(location);

        Integer weatherCode = getNumber(currentWeather, "weather_code").intValue();

        return Map.of(
                "city", location.get("name"),
                "country", location.get("country"),
                "latitude", location.get("latitude"),
                "longitude", location.get("longitude"),
                "temperature", currentWeather.get("temperature_2m"),
                "humidity", currentWeather.get("relative_humidity_2m"),
                "windSpeed", currentWeather.get("wind_speed_10m"),
                "weatherCode", weatherCode,
                "description", describeWeather(weatherCode),
                "time", currentWeather.get("time")
        );
    }

    private Map<String, Object> findLocation(String city) {
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
                .queryParam("name", city)
                .queryParam("count", 1)
                .queryParam("language", "en")
                .queryParam("format", "json")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> results = response == null ? null : (List<Map<String, Object>>) response.get("results");

        if (results == null || results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }

        return results.get(0);
    }

    private Map<String, Object> findCurrentWeather(Map<String, Object> location) {
        String url = UriComponentsBuilder.fromHttpUrl(FORECAST_API_URL)
                .queryParam("latitude", location.get("latitude"))
                .queryParam("longitude", location.get("longitude"))
                .queryParam("current", "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> currentWeather = response == null ? null : (Map<String, Object>) response.get("current");

        if (currentWeather == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Weather data unavailable");
        }

        return currentWeather;
    }

    private Number getNumber(Map<String, Object> data, String key) {
        Object value = data.get(key);

        if (!(value instanceof Number)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Invalid weather data");
        }

        return (Number) value;
    }

    private String describeWeather(int code) {
        if (code == 0) {
            return "Clear sky";
        }

        if (code <= 3) {
            return "Cloudy";
        }

        if (code <= 48) {
            return "Fog";
        }

        if (code <= 67) {
            return "Rain";
        }

        if (code <= 77) {
            return "Snow";
        }

        if (code <= 82) {
            return "Rain showers";
        }

        if (code <= 86) {
            return "Snow showers";
        }

        if (code <= 99) {
            return "Thunderstorm";
        }

        return "Unknown";
    }
}
