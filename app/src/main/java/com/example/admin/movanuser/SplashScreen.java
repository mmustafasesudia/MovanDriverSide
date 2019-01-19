package com.example.admin.movanuser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (ConfigURL.getLoginState(SplashScreen.this).length() > 0) {
                    Intent intent = new Intent(SplashScreen.this, Drawer.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
