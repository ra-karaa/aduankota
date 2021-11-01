package enterwind.ra.aduan.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.pengaduan.AddPengaduanActivity;
import enterwind.ra.aduan.models.Polse;

public class PolsekAdapter extends RecyclerView.Adapter<PolsekAdapter.MyViewHolder> {

    private Activity mContext;
    private List<Polse> polseList;

    public PolsekAdapter(Activity mContext, List<Polse>polseList){
        this.mContext = mContext;
        this.polseList = polseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_polsek, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Polse polse = polseList.get(position);
        holder.nama.setText(polse.getNama());
        holder.alamat.setText(polse.getAlamat());
        holder.jarak.setText(polse.getJarak() + " KM");
        Picasso.with(mContext).load(polse.getPhoto()).fit().into(holder.foto);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polse.getNama().equals("POLSEK Samarinda Ilir")){
                    mContext.startActivity(new Intent(mContext, AddPengaduanActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Upsss Sorry !");
                    builder.setMessage("Silahkan Tekan Tombol CALL Untuk melakukan pengaduan melalui panggilan atau RUTE untuk melihat lokasi Polsek");
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNeutralButton("Rute", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + polse.getLatitude() + "," + polse.getLongitude());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                                mContext.startActivity(mapIntent);
                            } else {
                                Toast.makeText(mContext, "Maaf Google Maps Belum Terinstal. Install Terlebih dahulu.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, polse.getPhone(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + polse.getPhone()));
                            mContext.startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return polseList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardPolsek) CardView cardView;
        @BindView(R.id.getFotoPolsek) ImageView foto;
        @BindView(R.id.getNamaPolsek) TextView nama;
        @BindView(R.id.getAlamatPolsek) TextView alamat;
        @BindView(R.id.getJarakPolsek) TextView jarak;
        public MyViewHolder (View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
