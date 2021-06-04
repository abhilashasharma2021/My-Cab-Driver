package com.mycab.Driver.Activity.Fragment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mycab.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
      /*  binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });*/

    }
}