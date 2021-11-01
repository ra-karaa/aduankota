package enterwind.ra.aduan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.pengaduan.HomeActivity;
import enterwind.ra.aduan.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    String sessionAkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proses();
    }

    private void proses() {
        session = new SessionManager(getApplicationContext());
        session.chekLogin();

        HashMap<String, String> user = session.getUserDetail();
        sessionAkses = user.get(SessionManager.KEY_AKSES);

        if (session.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));

        }
    }

}
