package com.sep.onlinedeliverysystem.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.services.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationServiceImpl implements LocationService {

    //API key for LocationIQ
    private final String LOCATIONIQ_API_KEY = "pk.958aa9e2459c09b2d67d8ba92c9d0afb";

    @Override
    public double calculateDistanceBetweenPostcodes(String postcode1, String postcode2) {
        String coords1 = getCoordinates(postcode1);
        String coords2 = getCoordinates(postcode2);
        if (coords1 == null || coords2 == null) {
            throw new IllegalArgumentException("Invalid postcodes provided");
        }

        //split the coords into lat & lon
        String[] parts1 = coords1.split(","); //makes an array with lat and lon
        String[] parts2 = coords2.split(",");
        double lat1 = Double.parseDouble(parts1[0]);
        double lon1 = Double.parseDouble(parts1[1]);
        double lat2 = Double.parseDouble(parts2[0]);
        double lon2 = Double.parseDouble(parts2[1]);
        return calculateDistance(lat1, lon1, lat2, lon2);
    }

    private String getCoordinates(String postcode) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://eu1.locationiq.com/v1/search.php?key=" + LOCATIONIQ_API_KEY + "&q=" + postcode + "&format=json";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            try {
                // Parse JSON response to extract latitude and longitude
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                String latitude = rootNode.get(0).get("lat").asText();
                String longitude = rootNode.get(0).get("lon").asText();
                return latitude + "," + longitude;
            } catch (Exception e) {
                // exception handling
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        double R = 6371e3; // Earth radius in meters
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        distance = Math.round(distance * 100.0) / 100.0;

        return distance;
    }
}
