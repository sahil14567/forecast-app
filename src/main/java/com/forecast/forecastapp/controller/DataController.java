package com.forecast.forecastapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forecast.forecastapp.model.ComparisonResult;
import com.forecast.forecastapp.model.DataPoint;
import com.forecast.forecastapp.service.ApiService;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/actual")
    public List<DataPoint> actual() {
        return apiService.getActualData();
    }

    @GetMapping("/forecast")
    public List<DataPoint> forecast() {
        return apiService.getForecastData();
    }

    @GetMapping("/compare")
    public List<ComparisonResult> compare(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam int horizon) {

        return apiService.compareData(start, end, horizon);
    }
}
