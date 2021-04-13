package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String LINK_TO_API = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_WEATHER_BY_CITY_ID = "https://www.metaweather.com/api/location/";
    public static final String QUERY_FOR_WEATHER_BY_CITY_LOCATION = "https://www.metaweather.com/api/location/search/?lattlong=";
    Context context;
    String cityId = "";

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityID);
    }

    public void getCityId(String cityName, VolleyResponseListener volleyResponseListener) {

        // Instantiate the RequestQueue.
        // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = LINK_TO_API + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(context,"City ID: "+ cityId ,Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Something Wrong! ", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Wrong!");

            }
        });

        //queue.add(request);

        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);

        // return cityId;
    }

    public interface ForecastByIdResponseListener {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastById(String cityId,ForecastByIdResponseListener forecastByIdResponseListener) {

        List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        String url = QUERY_FOR_WEATHER_BY_CITY_ID + cityId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray consolidated_weather = response.getJSONArray("consolidated_weather");

                    for(int i=0;i<consolidated_weather.length();i++) {
                        WeatherReportModel one_day_weather = new WeatherReportModel();
                        JSONObject first_day_from_API = (JSONObject) consolidated_weather.get(i);
                        one_day_weather.setId(first_day_from_API.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_from_API.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(first_day_from_API.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(first_day_from_API.getString("wind_direction_compass"));
                        one_day_weather.setCreated(first_day_from_API.getString("created"));
                        one_day_weather.setApplicable_date(first_day_from_API.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_from_API.getLong("min_temp"));
                        one_day_weather.setMax_temp(first_day_from_API.getLong("max_temp"));
                        one_day_weather.setThe_temp(first_day_from_API.getLong("the_temp"));
                        one_day_weather.setWind_speed(first_day_from_API.getLong("wind_speed"));
                        one_day_weather.setWind_direction(first_day_from_API.getLong("wind_direction"));
                        one_day_weather.setAir_pressure(first_day_from_API.getLong("air_pressure"));
                        one_day_weather.setHumidity(first_day_from_API.getInt("humidity"));
                        one_day_weather.setVisibility(first_day_from_API.getLong("visibility"));
                        one_day_weather.setPredictability(first_day_from_API.getInt("predictability"));

                        weatherReportModels.add(one_day_weather);
                    }

                    forecastByIdResponseListener.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

//
//
    }

    public interface GetCityForecastByNameCallBack{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModelList);
    }

    public void getCityForecastByName(String cityName, GetCityForecastByNameCallBack getCityForecastByNameCallBack){

        getCityId(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {

                getCityForecastById(cityID, new ForecastByIdResponseListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {

                        getCityForecastByNameCallBack.onResponse(weatherReportModels);

                    }
                });

            }
        });

    }

    public interface GetCityListByLocation{

        void onError(String message);
        void onResponse(List<CityModel> cityModelList);

    }
    public void getForecastByLocation(String latitude,String longitude, GetCityListByLocation getCityListByLocation){

        List<CityModel> cityModels = new ArrayList<>();
        String url = QUERY_FOR_WEATHER_BY_CITY_LOCATION+latitude+","+longitude;
//        System.out.println("----------URL: "+url+"-----------------");

        System.out.println("----------Requesting Array List-----------------");


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                    try {
                        for(int i=0;i<response.length();i++){
                            CityModel city = new CityModel();
                            JSONObject job = response.getJSONObject(i);
                            city.setDistance(job.getInt("distance"));
                            city.setTitle(job.getString("title"));
                            city.setWoeid(job.getInt("woeid"));
                            city.setLatt_long(job.getString("latt_long"));
                            cityModels.add(city);

//                            System.out.println(city.toString());
                        }

                        getCityListByLocation.onResponse(cityModels);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }

}
