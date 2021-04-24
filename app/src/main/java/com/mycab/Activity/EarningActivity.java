package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mycab.R;
import com.mycab.databinding.ActivityEarningBinding;
import com.mycab.databinding.ActivityOTPVerifyBinding;

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