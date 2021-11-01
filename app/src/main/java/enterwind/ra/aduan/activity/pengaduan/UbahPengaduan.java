package enterwind.ra.aduan.activity.pengaduan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.utils.EndPoints;

public class UbahPengaduan extends AppCompatActivity {

    @BindView(R.id.kategori) TextView kategori;
    @BindView(R.id.putNama) TextView nama;
    @BindView(R.id.putAlamat) TextView alamat;
    @BindView(R.id.putBentuk) TextView bentuk;
    @BindView(R.id.putInformasi) TextView informasi;
    @BindView(R.id.putKerugian) TextView kerugian;
    @BindView(R.id.putSaksi) TextView saksi;
    String id, kategori_id;
    SweetAlertDialog dialog;
    public static final String TAG = UbahPengaduan.class.getSimpleName();
    public Context mContext = UbahPengaduan.this;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_pengaduan_ubah);
        ButterKnife.bind(this);
        proses();

        initDialog();
    }

    private void initDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Tunggu Sebentar...");
        dialog.setCancelable(false);
    }

    private void proses() {
        id = getIntent().getStringExtra("id");
        kategori.setText(getIntent().getStringExtra("kategori"));
        kategori_id = getIntent().getStringExtra("id_kategori");
        nama.setText(getIntent().getStringExtra("nama"));
        alamat.setText(getIntent().getStringExtra("alamat"));
        bentuk.setText(getIntent().getStringExtra("bentuk"));
        informasi.setText(getIntent().getStringExtra("informasi"));
        kerugian.setText(getIntent().getStringExtra("kerugian"));
        saksi.setText(getIntent().getStringExtra("saksi"));
    }

    @OnClick(R.id.kategori) void onKategori(){
        startActivityForResult(new Intent(UbahPengaduan.this, KategoriActivity.class), 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                kategori.setText(data.getStringExtra("label"));
                kategori_id = data.getStringExtra("id");
            }
        }
    }

    @OnClick(R.id.btnUbah) void onUbah(){
        dialog.show();
        String url = EndPoints.URL_UBAH_ADUAN + id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.equals("sukses")){
                    dialog.dismiss();
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil!")
                            .setContentText("Berhasil Ubah Data.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(UbahPengaduan.this, PengaduanActivity.class));
                                    finish();
                                }
                            }).show();
                } else {
                    dialog.dismiss();
                    Log.d(TAG, "response" + response.toString());
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Maaf, data gagal diubah!")
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d(TAG, "response" + error.toString());
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Galat!")
                        .setContentText("Maaf, Mohon Hubungi Tim!")
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("kategori_id", kategori_id);
                params.put("nama", nama.getText().toString().trim());
                params.put("alamat", alamat.getText().toString().trim());
                params.put("bentuk", bentuk.getText().toString().trim());
                params.put("info", informasi.getText().toString().trim());
                params.put("kerugian", kerugian.getText().toString().trim());
                params.put("saksi", saksi.getText().toString().trim());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);
    }

}
