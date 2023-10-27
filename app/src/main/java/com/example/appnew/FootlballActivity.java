package com.example.appnew;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.appnew.adapter.NewsAdapter;
import com.example.appnew.adapter.NewsViewModel;
import com.example.appnew.enity.BongDa;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FootlballActivity extends AppCompatActivity {
    RecyclerView NewsRecycler;
    NewsAdapter adapter;
    NewsViewModel model;
    public static SwipeRefreshLayout swipeRefreshLayoutNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footlball);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        swipeRefreshLayoutNews = findViewById(R.id.swipe_layout);
        swipeRefreshLayoutNews.setRefreshing(true);
        NewsRecycler = findViewById(R.id.NewsRecycler);
        NewsRecycler.setHasFixedSize(true);
        NewsRecycler.setLayoutManager(new LinearLayoutManager(this));

        model = ViewModelProviders.of(this).get(NewsViewModel.class);

        model.getHeroes().observe(this, new Observer<List<BongDa>>() {
            @Override
            public void onChanged(@Nullable List<BongDa> heroList) {
                adapter = new NewsAdapter(FootlballActivity.this, heroList,hero -> {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FootlballActivity.this);

                    LayoutInflater inflater =  getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.web_show, null);
                    dialogBuilder.setView(dialogView);

                    WebView mWebView=(WebView)dialogView.findViewById(R.id.WebConnect);
                    Button button=dialogView.findViewById(R.id.ok);

                    WebSettings webSettings = mWebView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    mWebView.loadData(hero.getEmbed(), "text/html", "UTF-8");

                    AlertDialog alertDialog = dialogBuilder.create();
                    button.setOnClickListener(view -> {alertDialog.dismiss();});
                    alertDialog.show();
                });
                // model.loadHeroes();
                NewsRecycler.setAdapter(adapter);
            }
        });
        addDAta();
        swipeRefreshLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutNews.setRefreshing(true);
                addDAta();
            }
        });
        swipeRefreshLayoutNews.post(new Runnable() {

            @Override
            public void run() {

                swipeRefreshLayoutNews.setRefreshing(true);

                addDAta();
            }
        });
        NewsRecycler.setAdapter(adapter);
    }
    ArrayList<BongDa> datas = new ArrayList<>();
    private void addDAta() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://free-football-soccer-videos.p.rapidapi.com/")
                .get()
                .addHeader("x-rapidapi-host", "free-football-soccer-videos.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4877c410b9mshe7fe9517ac14094p1cd13ejsn6a566299c797")
                .build();


        try {

            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    //System.out.println(responseData);
                    String title = "";
                    String thumbnail = "";
                    String date = "";
                    String embed = "";

                    try {
                        JSONArray json = new JSONArray(responseData);
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsons = json.getJSONObject(i);
                            title = jsons.getString("title");
                            thumbnail = jsons.getString("thumbnail");
                            embed = jsons.getString("embed");
                            date = jsons.getString("date");
                            datas.add(new BongDa(title, thumbnail,embed, date));

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                model.SetItem(datas);
                                swipeRefreshLayoutNews.setRefreshing(false);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();

                    }finally {
                        response.close();


                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}