package com.example.TransportCompany.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
 class OpenRouteService {
    @Value("${open_route_key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;


    public Optional<Map<String, Object>> getCoordinates(String city) {
        String url = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + city;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var responseEntity = restTemplate.exchange(requestEntity, Map.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            var response = responseEntity.getBody();
            return Optional.ofNullable(response)
                    .map(map -> (ArrayList<Map<String, Object>>) map.get("features"))
                    .filter(features -> !features.isEmpty())
                    .map(features -> (Map<String, Object>) features.get(0).get("geometry"));
        }

        return Optional.empty();
    }


    public long calculateDistance(String startCity, String destinationCity) {
        var startCityCoords = getCoordinates(startCity);
        var destinationCityCoords = getCoordinates(destinationCity);
        long distance = 0;

        if (startCityCoords.isPresent() && destinationCityCoords.isPresent()) {
            Map<String, Object> startCoords = startCityCoords.get();
            Map<String, Object> destinationCoords = destinationCityCoords.get();
            double longitudeStartCity = (double) ((ArrayList<?>) startCoords.getOrDefault("coordinates", new ArrayList<>())).get(0);
            double latitudeStartCity = (double) ((ArrayList<?>) startCoords.getOrDefault("coordinates", new ArrayList<>())).get(1);
            double longitudeDestinationCity = (double) ((ArrayList<?>) destinationCoords.getOrDefault("coordinates", new ArrayList<>())).get(0);
            double latitudeDestinationCity = (double) ((ArrayList<?>) destinationCoords.getOrDefault("coordinates", new ArrayList<>())).get(1);

            String urlString = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + API_KEY
                    + "&start=" + longitudeStartCity + ","
                    + latitudeStartCity + "&end=" + longitudeDestinationCity + "," + latitudeDestinationCity;


            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            var httpEntity = new HttpEntity("parameters", headers);
            // headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            // RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(urlString));
            //ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);
            var responseEntity= restTemplate.exchange(
                    urlString,
                    HttpMethod.GET,
                    httpEntity,
                    Map.class
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = responseEntity.getBody();
                distance = Optional.ofNullable(responseBody)
                        .map(map -> (ArrayList<Map<String, Object>>) map.get("features"))
                        .filter(features -> !features.isEmpty())
                        .map(features -> (Map<String, Object>) features.get(0).get("properties"))
                        .map(properties -> (Map<String, Object>) properties.get("summary"))
                        .map(summary -> (double) summary.get("distance"))
                        .map(distanceInMeters -> Math.round(distanceInMeters / 1000))
                        .orElse(distance);
            }
        }
        return distance;
    }
}
