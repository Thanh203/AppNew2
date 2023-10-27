package com.example.appnew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThoiTietActivity extends AppCompatActivity {
    EditText edtSearch;
    Button btnSearch, btnChangeActivity;
    ImageView imgIcon;
    TextView txtThanhpho,txtQuocGia,txtNhietDo,txtTrangThai,txtDoam, txtGio, txtMay, txtNgayCapNhat;

    String City = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoi_tiet);
        edtSearch = findViewById(R.id.edittextSearch);
        btnSearch = findViewById(R.id.btnSearch);
        // btnChangeActivity = findViewById(R.id.btnChangeActivity);
        txtThanhpho = findViewById(R.id.textviewThanhpho);
        txtQuocGia = findViewById(R.id.textviewQuocGia);
        txtNhietDo = findViewById(R.id.textviewNhietDo);
        txtTrangThai = findViewById(R.id.textviewTrangThai);
        txtGio = findViewById(R.id.textviewGio);
        txtDoam = findViewById(R.id.textviewDoam);
        txtMay = findViewById(R.id.textviewMay);
        txtNgayCapNhat = findViewById(R.id.textviewNgayCapNhat);
        imgIcon = findViewById(R.id.imgIcon);
        GetCurrentWeatherData("Saigon");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                if (city.equals("")){
                    City = "Saigon";
                    GetCurrentWeatherData(City);
                }
                else {
                    City = city;
                    GetCurrentWeatherData(city);
                }

            }
        });


    }
    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue= Volley.newRequestQueue(ThoiTietActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&appid=037d21cb14640ecd9a4f4eca49e7ff3f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtThanhpho.setText(name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd \n           HH-mm-ss");
                            String Day  = simpleDateFormat.format(date);

                            txtNgayCapNhat.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String TrangThai = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso picasso = Picasso.get();
                            picasso.load("https://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
                            txtTrangThai.setText(TrangThai);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo= jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue() - 273);

                            txtNhietDo.setText(Nhietdo+"°C");
                            txtDoam.setText(doam+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtGio.setText(gio+"m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
                            txtMay.setText(may+"%");


                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtQuocGia.setText("Tên quốc gia : " + country);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

}