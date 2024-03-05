package com.sep.onlinedeliverysystem.controller;

import com.sep.onlinedeliverysystem.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/calculate-distance")
    @ResponseBody
    public double calculateDistance(@RequestBody Map<String, String> request) {
        String postcode1 = request.get("postcode1");
        String postcode2 = request.get("postcode2");
        if (postcode1 == null || postcode2 == null) {
            throw new IllegalArgumentException("Both postcodes must be provided");
        }
        return locationService.calculateDistanceBetweenPostcodes(postcode1, postcode2);
    }

}