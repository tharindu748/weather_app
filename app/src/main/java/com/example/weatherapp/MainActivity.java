package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "ad20f5d222df84046f0527c28dc76ba8";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final long LOCATION_UPDATE_MIN_TIME = 5000;
    private static final float LOCATION_UPDATE_MIN_DISTANCE = 1000;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView weatherConditionTextView, temperatureTextView, humidityTextView, coordinatesTextView, addressTextView, currentTimeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableEdgeToEdgeDisplay();
        initializeViews();
        displayCurrentTime();
    }

    private void enableEdgeToEdgeDisplay() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        weatherConditionTextView = findViewById(R.id.weatherCondition);
        temperatureTextView = findViewById(R.id.temp);
        addressTextView = findViewById(R.id.addressText);
        humidityTextView = findViewById(R.id.humidityText);
        coordinatesTextView = findViewById(R.id.latitudeLongitude);
        currentTimeTextView = findViewById(R.id.systemTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdates();
        displayCurrentTime();
    }

    private void requestLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                handleLocationUpdate(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(MainActivity.this, "Location provider disabled", Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
    }

    private void handleLocationUpdate(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        coordinatesTextView.setText(String.format("%s, %s", latitude, longitude));
        fetchAddressFromLocation(location);

        RequestParams requestParams = new RequestParams();
        requestParams.put("appid", API_KEY);
        requestParams.put("lat", latitude);
        requestParams.put("lon", longitude);


        fetchWeatherData(requestParams);
    }

    private void fetchWeatherData(RequestParams requestParams) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(WEATHER_API_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Weather data retrieved successfully", Toast.LENGTH_SHORT).show();
                WeatherData weatherData = WeatherData.fromJson(response);
                updateUIWithWeatherData(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this, "Failed to retrieve weather data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                addressTextView.setText("Address: " + addressText);
            } else {
                addressTextView.setText("Address: Unable to determine");
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressTextView.setText("Address: Unable to determine");
        }
    }

    private void displayCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        currentTimeTextView.setText("Time: " + currentTime);
    }

    private void updateUIWithWeatherData(WeatherData weatherData) {
        temperatureTextView.setText(weatherData.getTemperature());
        humidityTextView.setText(weatherData.getHumidity());
        weatherConditionTextView.setText(weatherData.getWeatherDescription());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
