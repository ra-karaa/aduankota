package enterwind.ra.aduan.adapter;

import android.content.Context;
import android.content.Intent;
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
import enterwind.ra.aduan.activity.pengaduan.info.DetailBerita;
import enterwind.ra.aduan.models.Berita;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyViewHolder> {


    private Context mContext;
    private List<Berita> beritaList;

    public BeritaAdapter(Context mContext, List<Berita> beritaList){
        this.mContext = mContext;
        this.beritaList = beritaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_berita, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Berita berita = beritaList.get(position);
        holder.judul.setText(berita.getJudul());
        holder.tanggal.setText(berita.getTanggal());
        Picasso.with(mContext).load(berita.getFoto()).fit().into(holder.foto);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(mContext, DetailBerita.class);
                aa.putExtra("id", berita.getId());
                mContext.startActivity(aa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardBerita) CardView cardView;
        @BindView(R.id.getFotoBerita) ImageView foto;
        @BindView(R.id.getJudulBerita) TextView judul;
        @BindView(R.id.getTanggalBerita) TextView tanggal;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
