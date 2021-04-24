package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mycab.R;
import com.mycab.databinding.ActivityChooseTypeBinding;
import com.mycab.databinding.ActivityUploadDocumnetBinding;

public class UploadDocumnetActivity extends AppCompatActivity {
ActivityUploadDocumnetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDocumnetBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadDocumnetActivity.this,AddBankDetailActivity.class));
            }
        });
    }
}