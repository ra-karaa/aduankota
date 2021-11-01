package enterwind.ra.aduan.activity.pengaduan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.session.SessionManager;
import enterwind.ra.aduan.utils.EndPoints;

public class DetailAduanActivity extends AppCompatActivity {

    public static final String TAG = DetailAduanActivity.class.getSimpleName();

    @BindView(R.id.getImageAduanSendiri) ImageView foto;
    @BindView(R.id.getNamaTerlapor) TextView nama;
    @BindView(R.id.getAlamatKejadian) TextView alamat;
    @BindView(R.id.getBentuk) TextView bentuk;
    @BindView(R.id.getKerugian) TextView rugi;
    @BindView(R.id.getSaksi) TextView saksi;
    @BindView(R.id.getInfo) TextView info;
    @BindView(R.id.getTanggal) TextView tanggal;
    @BindView(R.id.statusWait) TextView wait;
    @BindView(R.id.statusPublised) TextView publis;
    @BindView(R.id.statusDecline) TextView decline;
    @BindView(R.id.statusEnd) TextView ended;
    @BindView(R.id.alasan) LinearLayout alasan;
    @BindView(R.id.getAlasan) TextView alasatolak;
    @BindView(R.id.UbahIsi) LinearLayout ubah;

    String id, gambar, kategori_id, kategori;
    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_pengaduan_detail);
        ButterKnife.bind(this);

        initIntent();
        ambilData();
    }

    private void initIntent() {
        id = getIntent().getExtras().getString("id");
        gambar = getIntent().getExtras().getString("foto");
    }


    @OnClick(R.id.btnBack) void onBack(){
        finish();
    }

    private void ambilData() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Harap Tunggu");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.URL_DETAIL_SENDIRI + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response);
                progressDialog.dismiss();
                try {
                    Picasso.with(DetailAduanActivity.this).load(gambar).fit().into(foto);
                    nama.setText(response.getString("nama"));
                    alamat.setText(response.getString("lokasi"));
                    bentuk.setText(response.getString("bentuk"));
                    tanggal.setText(response.getString("waktu"));
                    saksi.setText(response.getString("saksi"));
                    info.setText(response.getString("info"));
                    rugi.setText(response.getString("rugi"));
                    kategori_id = response.getString("id_kategori");
                    kategori = response.getString("kategori");
                    switch (response.getString("status")){
                        case "1":
                            wait.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            decline.setVisibility(View.VISIBLE);
                            alasan.setVisibility(View.VISIBLE);
                            ubah.setVisibility(View.VISIBLE);
                            alasatolak.setText(response.getString("alasan"));
                            break;
                        case "3":
                            publis.setVisibility(View.VISIBLE);
                            break;
                        case "4":
                            ended.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    @OnClick(R.id.UbahIsi) void onUbah(){
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        Intent aa = new Intent(DetailAduanActivity.this, UbahPengaduan.class);
        aa.putExtra("id", id);
        aa.putExtra("id_kategori", kategori_id);
        aa.putExtra("kategori", kategori);
        aa.putExtra("nama", nama.getText().toString().trim());
        aa.putExtra("alamat", alamat.getText().toString().trim());
        aa.putExtra("bentuk", bentuk.getText().toString().trim());
        aa.putExtra("informasi", info.getText().toString().trim());
        aa.putExtra("kerugian", rugi.getText().toString().trim());
        aa.putExtra("saksi", saksi.getText().toString().trim());
        startActivity(aa);

    }

}
