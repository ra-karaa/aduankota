package enterwind.ra.aduan.activity.pengaduan.info.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import enterwind.ra.aduan.activity.pengaduan.info.InfoActivity;
import enterwind.ra.aduan.adapter.AgendaAdapter;
import enterwind.ra.aduan.adapter.BeritaAdapter;
import enterwind.ra.aduan.models.Agenda;
import enterwind.ra.aduan.models.Berita;
import enterwind.ra.aduan.utils.EndPoints;

public class BeritaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = InfoActivity.class.getSimpleName();
    private List<Berita>  beritaList;
    private BeritaAdapter adapter;

    @BindView(R.id.resikelBerita) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle tes){
        View view = inflater.inflate(R.layout.fragment_berita, container, false);
        ButterKnife.bind(this, view);

        beritaList = new ArrayList<>();
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
        init();
        return view;
    }

    private void init() {
        recyclerView.setAdapter(null);
        adapter = new BeritaAdapter(getActivity(), beritaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareData();
    }

    private void prepareData() {
        shimmer.startShimmer();
        String url = EndPoints.URL_BERITA;
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
                    beritaList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Berita agenda = new Berita(
                                    object.getString("id"),
                                    object.getString("judul"),
                                    object.getString("kontent"),
                                    object.getString("foto"),
                                    object.getString("tanggal")
                            );
                            beritaList.add(agenda);
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
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                init();
            }
        }, 1000);
    }
}
