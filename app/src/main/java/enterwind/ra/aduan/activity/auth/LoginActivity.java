package enterwind.ra.aduan.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.pengaduan.HomeActivity;
import enterwind.ra.aduan.session.SessionManager;
import enterwind.ra.aduan.utils.EndPoints;

public class LoginActivity extends AppCompatActivity {

    private Context mContext = LoginActivity.this;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.txtUsername) EditText username;
    @BindView(R.id.txtPassword) EditText password;
    String user, pass;

    SweetAlertDialog dialog;
    SessionManager sessionManager;
    String fcmId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());
        fcmId = FirebaseInstanceId.getInstance().getToken();
    }

    @OnClick(R.id.btnLogin) void onLogin()
    {
        loading();
        user = username.getText().toString().trim();
        pass = password.getText().toString().trim();
        if (!user.isEmpty() && !pass.isEmpty()) {
            checkLogin(user, pass);
        } else {
            dialog.dismiss();
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Harap lengkapi seluruh inputan!")
                    .show();
        }
    }

    private void checkLogin(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_LOGIN + email + "/" + password + "/" + fcmId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.equals("gagal")) {
                    dialog.dismiss();
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Username dan Password Salah!")
                            .show();
                } else {
                    String[] list = response.split(",");
                    SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.DATASTREAMPREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SessionManager.DATASTREAMID, list[1]);
                    editor.apply();
                    switch (list[1]) {
                        case "2":
                        sessionManager.createLogin(list[0], list[1], list[2], list[3], list[4], list[5], list[6]);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        break;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error) ;
                dialog.dismiss();
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Galat!")
                        .setContentText("Mohon Hubungi Tim Teknis!")
                        .show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loading(){
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
    }

    @OnClick(R.id.register) void onAkun(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
