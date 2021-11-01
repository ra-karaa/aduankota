package enterwind.ra.aduan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.auth.LoginActivity;
import enterwind.ra.aduan.activity.pengaduan.HomeActivity;
import enterwind.ra.aduan.activity.pengaduan.TutorialActivity;

public class SplashActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uIoptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setVisibility(uIoptions);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

}
