package com.mycab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycab.Activity.ChooseTypeActivity;
import com.mycab.Activity.LoginActivity;
import com.mycab.Activity.SignUpActivity;
import com.mycab.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private LinearLayout dotsLayout;
    private ViewPager viewPager;
    private TextView[] dots;
    private int[] layouts;
    Button btnStart;
    int maintain = 0;
    private PagerAdapter myViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout =  findViewById(R.id.layoutDots);
        btnStart =  findViewById(R.id.btnStart);
        layouts = new int[]{
                R.layout.screen1,
                R.layout.screen2,
                R.layout.screen3,
        };

        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
      viewPager.setAdapter(myViewPagerAdapter);
      viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Toast.makeText(MyActivity.this, i+"  Is Selected  "+data.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int i) {
                // here you will get the position of selected page
                maintain = i;


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Toast.makeText(WelcomeActivity.this, "" + maintain, Toast.LENGTH_SHORT).show();
                if (maintain == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    int current = getItem(+1);
                    Log.e("cscdasc", current + "");
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {

                        if (current > layouts.length) {
                            viewPager.setCurrentItem(current);

                        }

                    }

                }

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array1_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array1_dot_inactive);


        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            Log.e("hfjdhv", position + "");
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {

            } else {

            }
            if (position == 0) {

            } else if (position == 1) {

            } else if (position == 2) {
                GoToHome();
                btnStart.setVisibility(View.VISIBLE);
            } else if (position == 3) {

            } else {


            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }


    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {

        }

        @SuppressLint("ResourceAsColor")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            // Toast.makeText(WelcomeActivity.this, position+"", Toast.LENGTH_SHORT).show();


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    Toast.makeText(WelcomeActivity.this, "" + maintain, Toast.LENGTH_SHORT).show();
                    if (maintain == layouts.length - 1) {
                        // last page. make button text to GOT IT
                        int current = getItem(+1);
                        Log.e("dafdasfdsaf", current + "");
                        if (current < layouts.length) {
                            // move to next screen
                          viewPager.setCurrentItem(current);
                        } else {

                            //   startActivity(new Intent(ViewPagerActivity.this, HomeActivity.class));


                        }
                    }
                }
            });
            container.addView(view);

            return view;

        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);


        }
    }
        public void GoToHome() {

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                }
            });

           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(MainActivity.this, ChooseTypeActivity.class));


                }
            }, 1000);*/
        }
}
