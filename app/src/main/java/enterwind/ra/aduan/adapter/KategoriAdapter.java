package enterwind.ra.aduan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.models.Kategori;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.MyViewHolder> {

    private Activity mContext;
    private List<Kategori> kategoriList;

    public KategoriAdapter(Activity mContext, List<Kategori> kategoriList){
        this.mContext = mContext;
        this.kategoriList = kategoriList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_kategori, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     final Kategori kategori = kategoriList.get(position);
     holder.label.setText(kategori.getLabel());
     holder.cardView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent();
             intent.putExtra("id", kategori.getId());
             intent.putExtra("label", kategori.getLabel());
             mContext.setResult(Activity.RESULT_OK, intent);
             mContext.finish();
         }
     });
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardKategori) CardView cardView;
        @BindView(R.id.getLabel) TextView label;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
