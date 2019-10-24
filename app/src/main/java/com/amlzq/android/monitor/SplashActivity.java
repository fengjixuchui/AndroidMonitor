package com.amlzq.android.monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppUtil.exit();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        delayed();
    }

    // 隐私权政策

    private void delayed() {
        // 延迟3S
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 1500);
    }

    private void startMainActivity() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
