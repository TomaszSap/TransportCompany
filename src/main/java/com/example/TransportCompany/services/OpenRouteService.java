package com.example.TransportCompany.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

@Service
public class OpenRouteService {
    private static final String API_KEY = "";
    private Optional<JSONArray> getCoordinates(String city) throws JSONException {
        JSONArray coordinates = null;
        try {
            URL url = new URL("https://api.openrouteservice.org/geocode/search?api_key="+API_KEY+"&text=" + city);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(url.openStream());
            String response = scanner.useDelimiter("\\Z").next();

            JSONObject jsonResponse = new JSONObject(response);
            JSONArray features = jsonResponse.getJSONArray("features");

            if (features.length() > 0) {
                coordinates = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

            }
            conn.disconnect();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.of(coordinates);
    }

    public  long calculateDistance(String startCity, String destinationCity) throws JSONException, IOException {
        var startCityCoords=  getCoordinates(startCity);
        var destinationCityCoords=  getCoordinates(destinationCity);
        long distance = 0;
        double longitudeStartCity;
        double latitudeStartCity;
        double longitudeDestinationCity;
        double latitudeDestinationCity;
        if (startCityCoords.isPresent()&& destinationCityCoords.isPresent())
        {
            longitudeStartCity=startCityCoords.get().getDouble(0);
            latitudeStartCity=startCityCoords.get().getDouble(1);
            longitudeDestinationCity=destinationCityCoords.get().getDouble(0);
            latitudeDestinationCity=destinationCityCoords.get().getDouble(1);
            String urlString = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + API_KEY
                    + "&start=" + longitudeStartCity+","+latitudeStartCity+ "&end=" + longitudeDestinationCity+","+latitudeDestinationCity;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(url.openStream());
            String responseBody = scanner.useDelimiter("\\A").next();


            String distanceString = responseBody.split("\"distance\":")[1].split(",")[0].trim();
            scanner.close();
            conn.disconnect();
            return Math.round(Double.parseDouble(distanceString) / 1000);
        }
        return distance;
    }
}
