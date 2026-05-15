package com.example.appmeteo;

import java.util.LinkedHashMap;
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
    private static final String CURRENT_WEATHER_FIELDS = "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {
        if (city == null || city.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ville est obligatoire");
        }

        Map<String, Object> location = findLocation(city.trim());
        Map<String, Object> currentWeather = findCurrentWeather(location);

        Integer weatherCode = getNumber(currentWeather, "weather_code").intValue();
        Map<String, Object> weather = new LinkedHashMap<>();
        weather.put("city", location.get("name"));
        weather.put("country", location.get("country"));
        weather.put("latitude", location.get("latitude"));
        weather.put("longitude", location.get("longitude"));
        weather.put("temperature", currentWeather.get("temperature_2m"));
        weather.put("humidity", currentWeather.get("relative_humidity_2m"));
        weather.put("windSpeed", currentWeather.get("wind_speed_10m"));
        weather.put("weatherCode", weatherCode);
        weather.put("description", describeWeather(weatherCode));
        weather.put("time", currentWeather.get("time"));

        return weather;
    }

    private Map<String, Object> findLocation(String city) {
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
                .queryParam("name", city)
                .queryParam("count", 1)
                .queryParam("language", "fr")
                .queryParam("format", "json")
                .toUriString();

        Map<String, Object> response = getJsonObject(url);
        List<Map<String, Object>> results = getJsonList(response, "results");

        if (results == null || results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville introuvable");
        }

        return results.get(0);
    }

    private Map<String, Object> findCurrentWeather(Map<String, Object> location) {
        String url = UriComponentsBuilder.fromHttpUrl(FORECAST_API_URL)
                .queryParam("latitude", location.get("latitude"))
                .queryParam("longitude", location.get("longitude"))
                .queryParam("current", CURRENT_WEATHER_FIELDS)
                .toUriString();

        Map<String, Object> response = getJsonObject(url);
        Map<String, Object> currentWeather = getJsonObject(response, "current");

        if (currentWeather == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Données météo indisponibles");
        }

        return currentWeather;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonObject(String url) {
        return restTemplate.getForObject(url, Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getJsonObject(Map<String, Object> data, String key) {
        Object value = data == null ? null : data.get(key);
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getJsonList(Map<String, Object> data, String key) {
        Object value = data == null ? null : data.get(key);
        return value instanceof List ? (List<Map<String, Object>>) value : null;
    }

    private Number getNumber(Map<String, Object> data, String key) {
        Object value = data.get(key);

        if (!(value instanceof Number)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Données météo invalides");
        }

        return (Number) value;
    }

    private String describeWeather(int code) {
        switch (code) {
            case 0:
                return "Ciel dégagé";
            case 1:
            case 2:
            case 3:
                return "Nuageux";
            case 45:
            case 48:
                return "Brouillard";
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
                return "Pluie";
            case 71:
            case 73:
            case 75:
            case 77:
                return "Neige";
            case 80:
            case 81:
            case 82:
                return "Averses de pluie";
            case 85:
            case 86:
                return "Averses de neige";
            case 95:
            case 96:
            case 99:
                return "Orage";
            default:
                return "Inconnu";
        }
    }
}
