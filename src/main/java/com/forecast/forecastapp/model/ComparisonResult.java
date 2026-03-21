package com.forecast.forecastapp.model;

public class ComparisonResult {

    private String time;
    private int actual;
    private int forecast;
    private int error;

    public ComparisonResult(String time, int actual, int forecast) {
        this.time = time;
        this.actual = actual;
        this.forecast = forecast;
        this.error = actual - forecast;
    }

    public String getTime() { return time; }
    public int getActual() { return actual; }
    public int getForecast() { return forecast; }
    public int getError() { return error; }
}