package enterwind.ra.aduan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.pengaduan.DetailAduanActivity;
import enterwind.ra.aduan.models.Pengaduan;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Activity mContext;
    private List<Pengaduan> pengaduanList;

    public HistoryAdapter(Activity mContext, List<Pengaduan>pengaduanList){
        this.mContext = mContext;
        this.pengaduanList = pengaduanList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pengaduan pengaduan = pengaduanList.get(position);
        Picasso.with(mContext).load(pengaduan.getFoto()).fit().into(holder.foto);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(mContext, DetailAduanActivity.class);
                aa.putExtra("id", pengaduan.getId());
                aa.putExtra("foto", pengaduan.getFoto());
                mContext.startActivity(aa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pengaduanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardHistory) CardView cardView;
        @BindView(R.id.getImageAduan) ImageView foto;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
