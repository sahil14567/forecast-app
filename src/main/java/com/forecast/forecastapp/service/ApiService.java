package com.forecast.forecastapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.forecast.forecastapp.model.ComparisonResult;
import com.forecast.forecastapp.model.DataPoint;

@Service
public class ApiService {

    // 🔵 ACTUAL DATA (REAL API)
    public List<DataPoint> getActualData() {

        String url = "https://data.elexon.co.uk/bmrs/api/v1/datasets/FUELHH/stream?settlementDateFrom=2025-01-01&settlementDateTo=2025-01-31&fuelType=WIND";

        RestTemplate restTemplate = new RestTemplate();
        DataPoint[] data = restTemplate.getForObject(url, DataPoint[].class);

        return Arrays.asList(data);
    }

    // 🟢 FORECAST (MOCK - GENERATED)
    public List<DataPoint> getForecastData() {

        List<DataPoint> list = new ArrayList<>();

        LocalDateTime start = LocalDateTime.parse("2025-01-01T00:00");

        for (int i = 0; i < 15 * 48; i++) {

            LocalDateTime time = start.plusMinutes(30 * i);
            LocalDateTime publishTime = time.minusHours(4);

            int base = 14000 + (int)(Math.sin(i / 10.0) * 2000);
            int noise = (int)(Math.random() * 1000);

            int generation = base + noise;

            list.add(new DataPoint(
                    time.toString() + "Z",
                    publishTime.toString() + "Z",
                    generation
            ));
        }

        return list;
    }

    // 🔥 MAIN LOGIC
    public List<ComparisonResult> compareData(String start, String end, int horizon) {

        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        List<DataPoint> actualList = getActualData();
        List<DataPoint> forecastList = getForecastData();

        List<ComparisonResult> result = new ArrayList<>();

        for (DataPoint actual : actualList) {

            if (actual.getStartTime() == null) continue;

            LocalDateTime actualTime = LocalDateTime.parse(
                    actual.getStartTime().replace("Z", "")
            );

            if (actualTime.isBefore(startTime) || actualTime.isAfter(endTime)) {
                continue;
            }

            DataPoint bestForecast = null;

            for (DataPoint forecast : forecastList) {

                if (forecast.getStartTime() == null || forecast.getPublishTime() == null)
                    continue;

                LocalDateTime forecastTime = LocalDateTime.parse(
                        forecast.getStartTime().replace("Z", "")
                );

                if (!actualTime.equals(forecastTime)) {
                    continue;
                }

                LocalDateTime publishTime = LocalDateTime.parse(
                        forecast.getPublishTime().replace("Z", "")
                );

                // horizon logic
                if (!publishTime.isAfter(actualTime.minusHours(horizon))) {

                    if (bestForecast == null ||
                        publishTime.isAfter(LocalDateTime.parse(bestForecast.getPublishTime().replace("Z", "")))) {

                        bestForecast = forecast;
                    }
                }
            }

            if (bestForecast != null) {
                result.add(new ComparisonResult(
                        actual.getStartTime(),
                        actual.getGeneration(),
                        bestForecast.getGeneration()
                ));
            }
        }

        return result;
    }
}