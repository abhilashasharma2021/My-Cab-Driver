package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mycab.databinding.ActivityEarningBinding;

public class EarningActivity extends AppCompatActivity {
ActivityEarningBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEarningBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}