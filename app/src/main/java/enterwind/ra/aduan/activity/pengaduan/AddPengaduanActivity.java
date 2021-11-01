package enterwind.ra.aduan.activity.pengaduan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enterwind.ra.aduan.R;

public class AddPengaduanActivity extends AppCompatActivity {

    @BindView(R.id.kategori) TextView kategori;
    @BindView(R.id.putNama) TextView nama;
    @BindView(R.id.putAlamat) TextView alamat;
    @BindView(R.id.putBentuk) TextView bentuk;
    @BindView(R.id.putInformasi) TextView informasi;
    @BindView(R.id.putKerugian) TextView kerugian;
    @BindView(R.id.putSaksi) TextView saksi;
    String id;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_pengaduan_add);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.kategori) void onKategori(){
        startActivityForResult(new Intent(AddPengaduanActivity.this, KategoriActivity.class), 1);
    }

    @OnClick(R.id.btnBack) void onBack(){
        finish();
    }

    @OnClick(R.id.btnNext) void onNext(){
        if(!alamat.getText().toString().isEmpty() && !bentuk.getText().toString().isEmpty()
                && !kerugian.getText().toString().isEmpty()){
            Intent aa = new Intent(AddPengaduanActivity.this, AddPhotoPengaduanActivity.class);
            aa.putExtra("kategori", id);
            aa.putExtra("nama", nama.getText().toString().trim());
            aa.putExtra("alamat", alamat.getText().toString().trim());
            aa.putExtra("bentuk", bentuk.getText().toString().trim());
            aa.putExtra("informasi", informasi.getText().toString().trim());
            aa.putExtra("rugi", kerugian.getText().toString().trim());
            aa.putExtra("saksi", saksi.getText().toString().trim());
            startActivity(aa);
        } else{
            Toast.makeText(AddPengaduanActivity.this, "Harap Lengkapi Inputan", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                kategori.setText(data.getStringExtra("label"));
                id = data.getStringExtra("id");
            }
        }
    }
}
