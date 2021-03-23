package com.fendi.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
//Deklarasi global variabel
    Button btn_getCityId, btn_getWeatherById, btn_getWeatherByName;
    EditText editText_dataInput;
    ListView lv_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Deklarasi variabel
        btn_getCityId = findViewById(R.id.btn_getCityId);
        btn_getWeatherById = findViewById(R.id.btn_getWeatherByCityID);
        btn_getWeatherByName = findViewById(R.id.btn_getWeatherByCityName);

        editText_dataInput = findViewById(R.id.editText_dataInput);
        lv_weather = findViewById(R.id.lv_weather);

        WeatherData weatherData = new WeatherData(MainActivity.this);

        btn_getCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherData.getCityID(editText_dataInput.getText().toString(),
                        new WeatherData.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String cityID) {
                                Toast.makeText(MainActivity.this, "return : "+ cityID, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btn_getWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             weatherData.getWeatherByID(editText_dataInput.getText().toString(),
                     new WeatherData.getWeatherByIDListener() {
                 @Override
                 public void onError(String message) {
                     Toast.makeText(MainActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                 }

                         @Override
                         public void onResponse(List<WeatherReport> weatherReports) {
                             ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                                     android.R.layout.simple_list_item_1, weatherReports);
                             lv_weather.setAdapter(arrayAdapter);
                         }
             });
            }
        });
        btn_getWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherData.getWeatherByCityName(editText_dataInput.getText().toString(),
                        new WeatherData.getWeatherByCityNameListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReport> weatherReports) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                                        android.R.layout.simple_list_item_1, weatherReports);
                                lv_weather.setAdapter(arrayAdapter);
                            }
                        });
            }
        });
    }
}