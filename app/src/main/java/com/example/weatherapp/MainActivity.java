package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 101;
    ImageView currentWeatherImage;
    TextView tv_cityName,tv_currentDate,tv_weatherStatus,tv_currentTmp, tv_currentMinTmp,tv_currentMaxTmp,tv_windSpeed,tv_windDirection;
    TextView tv_humidity,tv_airPressure, tv_visibility,tv_predictability;
    LocationManager locationManager;
    String latitude, longitude;
    RecyclerView futureForecast;
    CustomAdapter customAdapter;
    WeatherDataService weatherDataService;
    Button refreshButton;
    String month_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_ui);


        currentWeatherImage = findViewById(R.id.currentWeatherImage);
        tv_cityName = findViewById(R.id.cityName);
        tv_currentDate = findViewById(R.id.dateOfTheDay);
        tv_weatherStatus = findViewById(R.id.weatherState);
        tv_currentTmp = findViewById(R.id.currentTemp);
        tv_currentMinTmp = findViewById(R.id.currentMinTemp);
        tv_currentMaxTmp = findViewById(R.id.currentMaxTemp);
        tv_windSpeed = findViewById(R.id.tv_windSpeed);
        tv_windDirection = findViewById(R.id.tv_windDirection);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_airPressure = findViewById(R.id.tv_airPressure);
        tv_visibility = findViewById(R.id.tv_visibility);
        tv_predictability = findViewById(R.id.tv_predictability);
        futureForecast = findViewById(R.id.futureReportsRecyclerView);
        refreshButton = findViewById(R.id.btn_refresh);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        month_name = month_date.format(cal.getTime());


        weatherDataService = new WeatherDataService(MainActivity.this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(GPS_PROVIDER)){
            GPSOn();
        }else{
            getLocation();
        }

        getWeatherByLocation();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherByLocation();
            }
        });

//        String url = "https://www.metaweather.com/static/img/weather/png/hr.png";
//
//        Glide.with(MainActivity.this).load(url).into(currentWeatherImage);


//        et_CityName = findViewById(R.id.et_DataInput);
//        btn_getCityId = findViewById(R.id.btn_getCityId);
//        btn_getWeatherById = findViewById(R.id.btn_getWeatherByCityId);
//        btn_getWeatherByCityName  = findViewById(R.id.btn_getWeatherByCityName);
//        lv_weatherReport = findViewById(R.id.lv_WeatherReport);
//        getLocation = findViewById(R.id.locationButton);
//        btn_getByLocation = findViewById(R.id.getByLocationButton);
//        panel = findViewById(R.id.mainPanel);
//
//        ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//
//        getLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
////        img = findViewById(R.id.weatherImage);
////
////        String url = "https://www.metaweather.com/static/img/weather/png/hr.png";
////
////        Glide.with(MainActivity.this).load(url).into(img);
//
//        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
//
//        btn_getByLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if(!locationManager.isProviderEnabled(GPS_PROVIDER)){
//                    GPSOn();
//                }else{
//                    getLocation();
//                }
//
//                weatherDataService.getForecastByLocation(latitude, longitude, new WeatherDataService.GetCityListByLocation() {
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(MainActivity.this,"Something Wrong!!!",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(List<CityModel> cityModelList) {
//
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,cityModelList);
//
//                        lv_weatherReport.setAdapter(arrayAdapter);
//
//                    }
//                });
//
//            }
//        });
//
//        btn_getCityId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                weatherDataService.getCityId(et_CityName.getText().toString(), new WeatherDataService.VolleyResponseListener() {
//                    @Override
//                    public void onError(String message) {
//
//                        Toast.makeText(MainActivity.this,"Something Wrong!!!",Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onResponse(String cityID) {
//
//                        Toast.makeText(MainActivity.this,"Returned ID of "+cityID,Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//
//
//
//            }
//        });
//
//        btn_getWeatherById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                weatherDataService.getCityForecastById(et_CityName.getText().toString(), new WeatherDataService.ForecastByIdResponseListener() {
//                    @Override
//                    public void onError(String message) {
//
//                        Toast.makeText(MainActivity.this,"Something wrong!!",Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModels);
//
//                        lv_weatherReport.setAdapter(arrayAdapter);
//
//
//                    }
//                });
//
//            }
//        });
//
//        btn_getWeatherByCityName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                weatherDataService.getCityForecastByName(et_CityName.getText().toString(), new WeatherDataService.GetCityForecastByNameCallBack()      {
//                    @Override
//                    public void onError(String message) {
//
//                        Toast.makeText(MainActivity.this,"Something wrong!!",Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModels);
//
//                        lv_weatherReport.setAdapter(arrayAdapter);
//
//
//                    }
//                });
//
//            }
//        });

    }

    private void getWeatherByLocation() {

        if(latitude!=null && longitude!=null){

            weatherDataService.getForecastByLocation(latitude, longitude, new WeatherDataService.GetCityListByLocation() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<CityModel> cityModelList) {

                    CityModel city = cityModelList.get(0);

                    String cityName = city.getTitle();

                    tv_cityName.setText(cityName);

                    weatherDataService.getCityForecastByName(cityName, new WeatherDataService.GetCityForecastByNameCallBack() {
                        @Override
                        public void onError(String message) {

                            Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponse(List<WeatherReportModel> weatherReportModelList) {

                            WeatherReportModel weatherReportForToday = weatherReportModelList.get(0);

                            String cDate = month_name+getString(R.string.comma)+weatherReportForToday.getApplicable_date().substring(8);
                            tv_currentDate.setText(cDate);

                            tv_weatherStatus.setText(weatherReportForToday.getWeather_state_name());

                            String cTmp = (int) weatherReportForToday.getThe_temp()+getString(R.string.degreeSign);
                            tv_currentTmp.setText(cTmp);

                            String cMinTmp = getString(R.string.min)+(int) weatherReportForToday.getMin_temp()+getString(R.string.degreeSign);
                            tv_currentMinTmp.setText(cMinTmp);

                            String cMaxTmp = getString(R.string.max)+(int)weatherReportForToday.getMax_temp()+getString(R.string.degreeSign);
                            tv_currentMaxTmp.setText(cMaxTmp);

                            String humid = (int)weatherReportForToday.getHumidity()+getString(R.string.percentSign);
                            tv_humidity.setText(humid);

                            String wSpeed = (int)weatherReportForToday.getWind_speed()+getString(R.string.mph);
                            tv_windSpeed.setText(wSpeed);

                            tv_windDirection.setText(weatherReportForToday.getWind_direction_compass());

                            float airPressureInBar = weatherReportForToday.getAir_pressure();
                            String apressure = (int) (airPressureInBar/33.8639)+getString(R.string.inHg);
                            tv_airPressure.setText(apressure);

                            String visible = (int)weatherReportForToday.getVisibility()+getString(R.string.mileSign);
                            tv_visibility.setText(visible);

                            String predict = weatherReportForToday.getPredictability()+getString(R.string.percentSign);
                            tv_predictability.setText(predict);

                            String abr = weatherReportForToday.getWeather_state_abbr();
                            String url = "https://www.metaweather.com/static/img/weather/png/"+abr+".png";
                            Glide.with(MainActivity.this).load(url).into(currentWeatherImage);

                            customAdapter =new CustomAdapter(MainActivity.this,new ArrayList<>(weatherReportModelList.subList(1,weatherReportModelList.size())));
                            futureForecast.setAdapter(customAdapter);
                            futureForecast.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));

                        }
                    });

                }
            });

        }else {

            Toast.makeText(MainActivity.this,"Could not get your location",Toast.LENGTH_SHORT).show();

        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
//                panel.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void GPSOn() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("true", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final  AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}