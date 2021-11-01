package enterwind.ra.aduan.activity.pengaduan;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.BaseActivity;
import enterwind.ra.aduan.adapter.PublicAdapter;
import enterwind.ra.aduan.models.Pengaduan;
import enterwind.ra.aduan.session.SessionManager;
import enterwind.ra.aduan.utils.EndPoints;

public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext = HomeActivity.this;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private List<Pengaduan> pengaduanList;
    private PublicAdapter adapter;

    @BindView(R.id.txtWelcome) TextView welcome;
    @BindView(R.id.carouselView) CarouselView carouselView;
    @BindView(R.id.resikelHome) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    ArrayList<String> judul = new ArrayList<String>();
    ArrayList<String> subjudul = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();


    SessionManager sessionManager;
    HashMap<String, String> HashMapForURL ;
    String nama;
    private static final int ACTIVITY_NUM = 0;

   @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
       ButterKnife.bind(this);
       setupBottomNavigationView(mContext, ACTIVITY_NUM);

       pengaduanList = new ArrayList<>();
       swipeRefresh.setOnRefreshListener(this);
       swipeRefresh.setRefreshing(true);
       initSession();
       welcomeMessage();
       initCarousel();
       init();
   }

    private void init() {
        adapter = new PublicAdapter(this, pengaduanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fetchJson();
    }

    private void fetchJson() {
        shimmer.startShimmer();
        String url = EndPoints.URL_ADUAN;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.length() == 0){
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    kosong.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(false);
                } else {
                    pengaduanList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Pengaduan aduan = new Pengaduan(
                                    object.getString("id"),
                                    object.getString("member_foto"),
                                    object.getString("member_nama"),
                                    object.getString("lokasi"),
                                    object.getString("foto"),
                                    object.getString("bentuk"),
                                    object.getString("waktu"),
                                    object.getString("status")
                            );
                            pengaduanList.add(aduan);
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
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void initSession() {
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> detail = sessionManager.getUserDetail();
        nama = detail.get(SessionManager.KEY_NAME);
    }

    private void welcomeMessage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String date = simpleDateFormat.format(calendar.getTime());
        int waktu = Integer.parseInt(date);

        if (waktu <= 11){
            welcome.setText("Selamat Pagi, " + nama + "!");
        } else if (waktu <= 15){
            welcome.setText("Selamat Siang, " + nama + "!");
        } else if (waktu <= 18){
            welcome.setText("Selamat Sore, " + nama + "!");
        } else if (waktu <= 23){
            welcome.setText("Selamat Malam, " + nama + "!");
        }
    }

    private void initCarousel() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                EndPoints.URL_SLIDER, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response);
                        if(response.length() > 0) {
                            for(int i = 0; i<response.length(); i++) {
                                JSONObject json = null;
                                try {
                                    json = response.getJSONObject(i);
                                    judul.add(json.getString("title"));
                                    subjudul.add(json.getString("subtitle"));
                                    image.add(json.getString("photo"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            carouselView.setViewListener(viewListener);
                            carouselView.setPageCount(judul.toArray().length);
                            carouselView.setSlideInterval(4000);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(mContext, "Terjadi kesalahan pada server.", Toast.LENGTH_SHORT).show();
                    }
                });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_slider, null);

            TextView tvJudul = (TextView) customView.findViewById(R.id.tvJudul);
            TextView tvDesc = (TextView) customView.findViewById(R.id.tvDesc);
            ImageView ivPhoto = (ImageView) customView.findViewById(R.id.ivPhoto);

            Picasso.with(mContext).load(image.get(position)).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(ivPhoto);
            tvJudul.setText(judul.get(position));
            tvDesc.setText(subjudul.get(position));

            return customView;
        }
    };

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
}
