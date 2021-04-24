package com.mycab.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.mycab.R;
import com.mycab.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,OTPVerifyActivity.class));
            }
        });

        String text = "By click start, you agree to our  <font color=#0092df>Terms and conditions</font>";
        binding.txtterm.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        binding.txSignIn.setOnClickListener(view -> {

            expands(binding.card, 600, 800);
            binding.btnSignIn.animate().alpha((float) 1).setDuration(1000).start();
            binding.cardEmail.setVisibility(View.GONE);
            binding.txSignIn.setTextColor(getResources().getColor(R.color.black));
            binding.txSignUp.setTextColor(getResources().getColor(R.color.grey));
            binding.btnSignUp.setVisibility(View.GONE);
            binding.llIntegration.setVisibility(View.GONE);
            binding.rlTerm.setVisibility(View.GONE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
            binding.txLogin.setVisibility(View.VISIBLE);
          /*  String text = "By click start, you agree to our  <font color=#0092df>Terms and conditions</font>";
            binding.txtterm.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);*/
           /* rel_Email.animate().alpha((float) 1).setDuration(1000).start();
            rel_fullname.animate().alpha((float) 1).setDuration(1000).start();
            rel_Email.setVisibility(View.VISIBLE);
            rel_fullname.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            btnLogI.setVisibility(View.GONE);*/
         //   rel_promocode.setVisibility(View.VISIBLE);
         //   mforgotPass.setVisibility(View.GONE);
          //  text.setText("Sign up");

            //alreadyRegister.setText("Already Registered?");
        });


        binding.txSignUp.setOnClickListener(view -> {

            collapse(binding.card, 600, 800);
            binding.btnSignUp.animate().alpha((float) 1).setDuration(1000).start();
            binding.cardEmail.setVisibility(View.VISIBLE);
            binding.txSignIn.setTextColor(getResources().getColor(R.color.grey));
            binding.txSignUp.setTextColor(getResources().getColor(R.color.black));
            binding.btnSignIn.setVisibility(View.GONE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.llIntegration.setVisibility(View.VISIBLE);
            binding.rlTerm.setVisibility(View.VISIBLE);
            binding.btnSignIn.setVisibility(View.GONE);
            binding.txLogin.setVisibility(View.GONE);
           /* rel_Email.animate().alpha((float) 1).setDuration(1000).start();
            rel_fullname.animate().alpha((float) 1).setDuration(1000).start();
            rel_Email.setVisibility(View.VISIBLE);
            rel_fullname.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            btnLogI.setVisibility(View.GONE);*/
            //   rel_promocode.setVisibility(View.VISIBLE);
            //   mforgotPass.setVisibility(View.GONE);
            //  text.setText("Sign up");

            //alreadyRegister.setText("Already Registered?");
        });




    }


    public static void expands(final View v, int duration, int targetHeight) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


}