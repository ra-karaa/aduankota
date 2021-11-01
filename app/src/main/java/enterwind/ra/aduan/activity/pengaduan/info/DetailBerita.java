package enterwind.ra.aduan.activity.pengaduan.info;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.utils.EndPoints;

public class DetailBerita extends AppCompatActivity {

    public static final String TAG = DetailBerita.class.getSimpleName();
    @BindView(R.id.getImage) ImageView foto;
    @BindView(R.id.getJudulBerita) TextView judul;
    @BindView(R.id.getIsiBerita) TextView isi;
    @BindView(R.id.getTanggal) TextView tanggal;

    String id;

    @Override
    protected void onCreate(Bundle savedInsttanceState){
        super.onCreate(savedInsttanceState);
        setContentView(R.layout.activity_berita_detail);
        ButterKnife.bind(this);

        initIntent();
        proses();
    }

    @OnClick(R.id.btnBack) void  onBack(){
        finish();
    }

    private void initIntent() {
        id = getIntent().getExtras().getString("id");
    }

    private void proses() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Harap Tunggu");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.URL_BERITA + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: " + response);
                try {
                    judul.setText(response.getString("judul"));
                    isi.setText(response.getString("kontent"));
                    tanggal.setText(response.getString("tanggal"));
                    Picasso.with(DetailBerita.this).load(response.getString("foto")).fit().into(foto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG, "onErrorResponse: " + error);
                Toast.makeText(DetailBerita.this, "Sorry Error With Load", Toast.LENGTH_SHORT).show();
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
}
