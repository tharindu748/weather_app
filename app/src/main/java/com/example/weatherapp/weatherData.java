package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

class WeatherData {

    private String temperature;
    private String weatherDescription;
    private String humidity;
    private int conditionId;

    public static WeatherData fromJson(JSONObject jsonObject) {
        try {
            WeatherData weatherData = new WeatherData();
            weatherData.conditionId = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            double tempInKelvin = jsonObject.getJSONObject("main").getDouble("temp");
            weatherData.temperature = String.valueOf((int) Math.round(tempInKelvin - 273.15));
            int humidityValue = jsonObject.getJSONObject("main").getInt("humidity");
            weatherData.humidity = String.valueOf(humidityValue);
            return weatherData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getTemperature() {
        return temperature + "°C";
    }



    public String getHumidity() {
        return humidity + "%";
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}


