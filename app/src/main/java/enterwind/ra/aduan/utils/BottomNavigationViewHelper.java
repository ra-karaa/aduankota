package enterwind.ra.aduan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.pengaduan.HomeActivity;
import enterwind.ra.aduan.activity.pengaduan.PengaduanActivity;
import enterwind.ra.aduan.activity.pengaduan.ProfilActivity;
import enterwind.ra.aduan.activity.pengaduan.TutorialActivity;
import enterwind.ra.aduan.activity.pengaduan.info.InfoActivity;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    /**
     * Setup Bottom Navigation View
     * @param bottomNavigationViewEx
     */
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }

    /**
     * Emable Navigation Setup
     * @param context
     * @param callingActivity
     * @param view
     */
    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        break;

                    case R.id.ic_info:
                        Intent intent2 = new Intent(context, InfoActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        break;

                    case R.id.ic_profil:
                        Intent intent3 = new Intent(context, ProfilActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        break;

                    case R.id.ic_adu:
                        Intent intent4 = new Intent(context, PengaduanActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        break;

                    case R.id.ic_tutorial:
                        Intent intent5 = new Intent(context, TutorialActivity.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                        break;


                }
                return false;
            }
        });
    }
}
