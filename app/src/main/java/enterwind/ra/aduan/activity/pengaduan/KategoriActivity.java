package enterwind.ra.aduan.activity.pengaduan;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
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
import butterknife.OnClick;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.adapter.KategoriAdapter;
import enterwind.ra.aduan.adapter.PolsekAdapter;
import enterwind.ra.aduan.models.Kategori;
import enterwind.ra.aduan.models.Polse;
import enterwind.ra.aduan.utils.EndPoints;

public class KategoriActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = KategoriActivity.class.getSimpleName();

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    private List<Kategori> kategoriList;
    private KategoriAdapter adapter;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_kategori);
        ButterKnife.bind(this);
        kategoriList = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
        init();
    }

    private void init() {
        adapter = new KategoriAdapter(this, kategoriList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        proses();
    }

    private void proses() {
        shimmer.startShimmer();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EndPoints.URL_KATEGORI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.length() == 0){
                    kosong.setVisibility(View.VISIBLE);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    swipeRefresh.setRefreshing(false);
                }else {
                    kategoriList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Kategori kategori = new Kategori(
                                    obj.getString("id"),
                                    obj.getString("label")
                            );
                            kategoriList.add(kategori);
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

    @OnClick(R.id.btnBack) void onBack(){
        finish();
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
