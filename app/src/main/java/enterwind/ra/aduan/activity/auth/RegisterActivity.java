package enterwind.ra.aduan.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.utils.EndPoints;

public class RegisterActivity extends AppCompatActivity {

    public Context mContext = RegisterActivity.this;
    public static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.etNik) EditText nik;
    @BindView(R.id.etNama) EditText nama;
    @BindView(R.id.etPhone) EditText phone;
    @BindView(R.id.etAlamat) EditText alamat;
    @BindView(R.id.etUsername) EditText user;
    @BindView(R.id.etPassword) EditText sandi;

    String nike, namae, usere, sandie, pun;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textSudah) void onLogin()
    {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }


    @OnClick(R.id.ivBack) void onFinish(){
        finish();
    }

    @OnClick(R.id.btnRegister) void onDaftar()
    {
        loading();
        nike = nik.getText().toString().trim();
        namae = nama.getText().toString().trim();
        usere = user.getText().toString().trim();
        sandie = sandi.getText().toString().trim();
        pun = phone.getText().toString().trim();

        if(!nike.isEmpty() && !namae.isEmpty() && !usere.isEmpty() && !sandie.isEmpty() &&!alamat.getText().toString().isEmpty()
        && !phone.getText().toString().isEmpty()){
            cekNik();
        }
        else {
            dialog.dismiss();
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Harap lengkapi seluruh inputan!")
                    .show();
        }
    }

    private void cekNik() {
        if(nike.length() < 15){
            dialog.dismiss();
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Nik Tidak Valid!")
                    .show();
        } else {
            cekPhone();
        }
    }

    private void cekPhone() {
        if (pun.length() < 10){
            dialog.dismiss();
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Nomor Tidak Valid")
                    .show();
        } else {
            lanjutkan();
        }
    }

    private void lanjutkan() {
        dialog.dismissWithAnimation();
        Intent aa = new Intent(RegisterActivity.this, RegisterFotoActivity.class);
        aa.putExtra("nik", nike);
        aa.putExtra("nama", namae);
        aa.putExtra("username", usere);
        aa.putExtra("pass", sandie);
        aa.putExtra("alamat", alamat.getText().toString().trim());
        aa.putExtra("phone", phone.getText().toString().trim());
        startActivity(aa);
    }

    private void loading() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void daftar() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if(response.equals("success")){
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Username sudah Digunakan!")
                            .setConfirmText("ok")
                            .show();
                } else {
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Username sudah Digunakan!")
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                dialog.dismiss();
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Galat!")
                        .setContentText("Mohon Hubungi Tim Teknis!")
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nike);
                params.put("nama", namae);
                params.put("username", usere);
                params.put("password", sandie);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
