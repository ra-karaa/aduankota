package enterwind.ra.aduan.activity.pengaduan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.BaseActivity;
import enterwind.ra.aduan.activity.auth.UpdateFotoActivity;
import enterwind.ra.aduan.adapter.HistoryAdapter;
import enterwind.ra.aduan.models.Pengaduan;
import enterwind.ra.aduan.session.SessionManager;
import enterwind.ra.aduan.utils.EndPoints;

public class ProfilActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ProfilActivity.this;
    public static final String TAG = ProfilActivity.class.getSimpleName();

    @BindView(R.id.getNama) TextView txtNama;
    @BindView(R.id.getNik) TextView txtNik;
    SessionManager sessionManager;
    @BindView(R.id.resikelHome) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;
    @BindView(R.id.standardBottomSheet) FrameLayout frameLayout;
    @BindView(R.id.getFotoMember) CircleImageView fotomember;

    String email;
    SweetAlertDialog dialog;

    private List<Pengaduan> pengaduanList;
    private HistoryAdapter adapter;
    boolean opened;
    @Override
    protected void onCreate(Bundle oke){
        super.onCreate(oke);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        pengaduanList = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
        ambilData();
        init();
        loading();

    }

    private void init() {
        adapter = new HistoryAdapter(this, pengaduanList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        proses_json();
    }

    private void proses_json() {
        shimmer.startShimmer();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EndPoints.URL_SENDIRI + email, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.length() == 0){
                    kosong.setVisibility(View.VISIBLE);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    swipeRefresh.setRefreshing(false);
                } else {
                    pengaduanList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Pengaduan pengaduan = new Pengaduan(
                                    object.getString("id"),
                                    object.getString("member_foto"),
                                    object.getString("member_nama"),
                                    object.getString("lokasi"),
                                    object.getString("foto"),
                                    object.getString("bentuk"),
                                    object.getString("waktu"),
                                    object.getString("status")
                            );
                            pengaduanList.add(pengaduan);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Kesalahan Teknis!")
                        .setContentText("Mohon hubungi tim teknis kami.")
                        .show();
                shimmer.stopShimmer();
                shimmer.setVisibility(View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
            }
        });
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 500000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 500000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void ambilData() {
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String , String> user = sessionManager.getUserDetail();
        txtNama.setText(user.get(SessionManager.KEY_NAME));
        txtNik.setText(user.get(SessionManager.KEY_NIK));
        email = user.get(SessionManager.KEY_USERNAME);
        ambilfoto(email);
    }

    private void ambilfoto(String email) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.URL_AMBIL_FOTO + email, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    Picasso.with(mContext).load(response.getString("foto")).fit().into(fotomember);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                Toast.makeText(mContext, "Error Load Photo", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @OnClick(R.id.logout) void onLogout()
    {
        new SweetAlertDialog(ProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Anda Yakin?")
                .setContentText("Benar Ingin Keluar dari Aplikasi!")
                .setConfirmText("Ya!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.logout();
                        finishAffinity();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

//    @OnClick(R.id.ubahfoto) void ubahFoto()
//    {
//        startActivity(new Intent(ProfilActivity.this, UpdateFotoActivity.class));
//    }


    @OnClick(R.id.hapus) void onHapus(){
        new SweetAlertDialog(ProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Anda Yakin?")
                .setContentText("Ingin Menghapus Akun!")
                .setConfirmText("Ya!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        hapusKan();
                    }
                })
                .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void hapusKan() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_HAPUS_AKUN + email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if(response.equals("sukses")){
                    dialog.dismiss();
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil!")
                            .setContentText("Berhasil Buat Pengaduan.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sessionManager = new SessionManager(getApplicationContext());
                                    sessionManager.logout();
                                    finishAffinity();
                                    dialog.dismissWithAnimation();
                                }
                            }).show();
                } else {
                    dialog.dismiss();
                    Log.d(TAG, "response" + response.toString());
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Maaf, Akun Gagal di Hapus!")
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
                dialog.dismiss();
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Galat!")
                        .setContentText("Mohon Hubungi Tim Teknis")
                        .show();

            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void loading() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Tunggu Sebentar...");
        dialog.setCancelable(false);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.fadein, R.anim.fade_out);
            }
        }, 1000);
    }

    @OnClick(R.id.imgToggle) void onPeler(){
        if(!opened) {
            frameLayout.setVisibility(View.INVISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,
                    0,
                    frameLayout.getHeight(),
                    0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            frameLayout.startAnimation(animate);
        } else {
            frameLayout.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,
                    0,
                    0,
                    frameLayout.getHeight());
            animate.setDuration(500);
            animate.setFillAfter(true);
            frameLayout.startAnimation(animate);
        }
        opened = !opened;
    }

//    @OnClick(R.id.ubah) void onUbahProfil(){
//        Toast.makeText(mContext, "Halam Ubah Profil", Toast.LENGTH_SHORT).show();
//    }

}
