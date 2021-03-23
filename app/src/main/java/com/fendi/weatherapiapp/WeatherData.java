package com.fendi.weatherapiapp;

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

public class WeatherData {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";


    Context context;

    String cityId;

    public WeatherData(Context context) {
        this.context = context;
    }

    public interface  VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityID);
    }
    public void getCityID(String cityName,  VolleyResponseListener volleyResponseListener){
        // Instantiate the RequestQueue.
        String url = QUERY_FOR_CITY_ID + cityName;

//                Requesting JSON
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        cityId = "";

                        try {
//                                    Pilih objek index pertama
                            JSONObject cityInfo = response.getJSONObject(0);
//                                    Fetching kata kunci "woeid"
                            cityId = cityInfo.getString("woeid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(context, "City ID : "+ cityId,
//                                Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(cityId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

//        return cityId;
      };

    public interface  getWeatherByIDListener{
        void onError(String message);
        void onResponse(List<WeatherReport> weatherReports);
    }

    public void getWeatherByID(String cityID, getWeatherByIDListener getWeatherByIDListener){
        JSONArray consolidated_weather_list = null;
        List<WeatherReport> report = new ArrayList<>();

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
//        get the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray consolidated_weather_list = response.getJSONArray(
                                    "consolidated_weather");
//                            get the first item in the array


                            for(int i =0;i<consolidated_weather_list.length();i++){
                                JSONObject one_day_api = (JSONObject) consolidated_weather_list.get(i);
                                WeatherReport first_day = new WeatherReport();

                                first_day.setId(one_day_api.getInt("id"));
                                first_day.setWeather_state_name(one_day_api.getString(
                                        "weather_state_name"));
                                first_day.setWeather_state_abbr(one_day_api.getString(
                                        "weather_state_abbr"));
                                first_day.setWind_direction_compass(one_day_api.getString(
                                        "wind_direction_compass"));
                                first_day.setCreated(one_day_api.getString("created"));
                                first_day.setApplicable_date(one_day_api.getString("applicable_date"));
                                first_day.setMin_temp(one_day_api.getLong("min_temp"));
                                first_day.setMax_temp(one_day_api.getLong("max_temp"));
                                first_day.setThe_temp(one_day_api.getLong("the_temp"));
                                first_day.setWind_speed(one_day_api.getLong("wind_speed"));
                                first_day.setWind_direction(one_day_api.getLong("wind_direction"));
                                first_day.setAir_pressure(one_day_api.getLong("air_pressure"));
                                first_day.setHumidity(one_day_api.getInt("humidity"));
                                first_day.setVisibility(one_day_api.getLong("visibility"));
                                first_day.setPredictability(one_day_api.getInt("predictability"));


                                report.add(first_day);
                            }
                            getWeatherByIDListener.onResponse(report);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        get the "consolidated_weather" object

//        get each array and put in new weather report
        MySingleton.getInstance(context).addToRequestQueue(request);
    };

    public interface getWeatherByCityNameListener{
        void onError(String message);
        void onResponse(List<WeatherReport> weatherReports);
    };
    public void getWeatherByCityName(String cityName,
                                     getWeatherByCityNameListener getWeatherByCityNameListeners){
//        Cari id berdasar nama
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                getWeatherByID(cityID, new getWeatherByIDListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReport> weatherReports) {
                        getWeatherByCityNameListeners.onResponse(weatherReports);
                    }
                });
            }
        });

//        Cari cuaca berdasar id yang didapat
    }
}
