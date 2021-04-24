package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mycab.R;
import com.mycab.databinding.ActivityOTPVerifyBinding;

public class OTPVerifyActivity extends AppCompatActivity {
ActivityOTPVerifyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPVerifyBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OTPVerifyActivity.this,ChooseTypeActivity.class));
            }
        });
    }
}