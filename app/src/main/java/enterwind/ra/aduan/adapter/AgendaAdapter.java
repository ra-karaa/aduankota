package enterwind.ra.aduan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.models.Agenda;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

    public Context mContext;
    public List<Agenda> agendaList;

    public AgendaAdapter(Context mContext, List<Agenda> agendaList){
        this.mContext = mContext;
        this.agendaList = agendaList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_agenda, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Agenda agenda = agendaList.get(position);
        holder.tanggal.setText(agenda.getTanggal());
        holder.bulan.setText(agenda.getBulan());
        holder.tahun.setText(agenda.getTahun());
        holder.judul.setText(agenda.getJudul());
        holder.perihal.setText(agenda.getPerihal());
        holder.lokasi.setText(agenda.getLokasi());
    }

    @Override
    public int getItemCount() {
        return agendaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.getTanggal) TextView tanggal;
        @BindView(R.id.getBulan) TextView bulan;
        @BindView(R.id.getTahun) TextView tahun;
        @BindView(R.id.getJudul) TextView judul;
        @BindView(R.id.getPerihal) TextView perihal;
        @BindView(R.id.getLokasi)TextView lokasi;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
