package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mycab.R;
import com.mycab.databinding.ActivityNotificationBinding;
import com.mycab.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}