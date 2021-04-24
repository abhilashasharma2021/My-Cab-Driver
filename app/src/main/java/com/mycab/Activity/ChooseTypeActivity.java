package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mycab.R;
import com.mycab.databinding.ActivityChooseTypeBinding;

public class ChooseTypeActivity extends AppCompatActivity {
ActivityChooseTypeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTypeBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());

        binding.cardDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseTypeActivity.this,UploadDocumnetActivity.class));
            }
        });

    }
}