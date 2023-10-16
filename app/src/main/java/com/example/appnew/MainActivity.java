package com.example.appnew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appnew.adapter.ItemCallback;

public class MainActivity extends AppCompatActivity implements ItemCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemClick(String id) {

    }
}