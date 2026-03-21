package com.forecast.forecastapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPoint {

    private String startTime;
    private String publishTime;
    private String fuelType;
    private String settlementDate;
    private int settlementPeriod;
    private int generation;

    public DataPoint() {}

    public DataPoint(String startTime, String publishTime, int generation) {
        this.startTime = startTime;
        this.publishTime = publishTime;
        this.generation = generation;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public int getGeneration() {
        return generation;
    }
}