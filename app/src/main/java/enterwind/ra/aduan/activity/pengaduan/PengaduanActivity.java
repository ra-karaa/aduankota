package enterwind.ra.aduan.activity.pengaduan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.BaseActivity;
import enterwind.ra.aduan.adapter.PolsekAdapter;
import enterwind.ra.aduan.models.Polse;
import enterwind.ra.aduan.utils.EndPoints;

public class PengaduanActivity extends BaseActivity implements LocationListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.resikelPolsek) RecyclerView recyclerView;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    private static final int ACTIVITY_NUM = 2;
    private Context mContext = PengaduanActivity.this;
    public static final String TAG = PengaduanActivity.class.getSimpleName();

    private List<Polse> polseList;
    private PolsekAdapter adapter;
    Criteria criteria;
    Location location;
    LocationManager locationManager;
    Double latitude, longitude;
    String provider;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_pengaduan);
        ButterKnife.bind(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);

        polseList = new ArrayList<>();
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);
        tes();
    }

    private void tes() {
        adapter = new PolsekAdapter(this, polseList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        cek();
    }

    private void cek() {
        if (ActivityCompat.checkSelfPermission(PengaduanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (PengaduanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PengaduanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
            if (location != null) {
                onLocationChanged(location);
            } else  if (location1 != null) {
                onLocationChanged(location);
            } else  if (location2 != null) {
                onLocationChanged(location);
            }else{
                shimmer.stopShimmer();
                Toast.makeText(this,"Mohon Aktifkan GPS Anda !!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.e(TAG, "User location latitude:" + latitude + ", longitude:" + longitude);

        prosess(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void prosess(Double latitude, Double longitude) {
        shimmer.startShimmer();
        String URL = EndPoints.URL_CEK + latitude + "/" + longitude;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.length() == 0){
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    kosong.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(false);
                }
                else {
                    polseList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Polse polse = new Polse();
                            polse.setId(object.getString("id"));
                            polse.setNama(object.getString("nama"));
                            polse.setAlamat(object.getString( "alamat"));
                            polse.setPhoto(object.getString( "photo"));
                            polse.setLatitude(object.getString("latitude"));
                            polse.setLongitude(object.getString("longitude"));
                            polse.setPhone(object.getString("phone"));
                            double jarak = Double.parseDouble(object.getString("distance"));
                            polse.setJarak("" + round(jarak, 2));
                            polseList.add(polse);
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
        requestQueue.add(jsonArrayRequest);
    }

    private static double round(double jarak, int i) {
        if (i < 0) throw new IllegalArgumentException();
        long factor = (long)Math.pow(10, i);
        jarak = jarak*factor;
        long tmp = Math.round(jarak);
        return (double) tmp/factor;
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
}
