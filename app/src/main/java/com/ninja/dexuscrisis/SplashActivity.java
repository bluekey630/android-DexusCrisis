package com.ninja.dexuscrisis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        Thread m_Thread = new Thread(new Timer());
        m_Thread.start();
    }


    private class Timer implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}
