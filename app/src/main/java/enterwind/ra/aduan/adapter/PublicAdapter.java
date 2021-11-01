package enterwind.ra.aduan.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.models.Pengaduan;

public class PublicAdapter extends RecyclerView.Adapter<PublicAdapter.MyViewHolder> {

    Activity mContext;
    List<Pengaduan> pengaduanList;

    public  PublicAdapter(Activity mContext, List<Pengaduan> pengaduanList){
        this.mContext = mContext;
        this.pengaduanList = pengaduanList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pengaduan pengaduan = pengaduanList.get(position);
        holder.nama.setText(pengaduan.getMember_nama());
        holder.rugi.setText(pengaduan.getBentuk());
        holder.lokasi.setText(pengaduan.getLokasi());
        holder.time.setText(pengaduan.getWaktu());
        Picasso.with(mContext).load(pengaduan.getFoto()).fit().into(holder.foto);
        String base64Image = pengaduan.member_foto.split(",")[1];
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        holder.memFoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        switch (pengaduan.getStatus()){
            case "3":
                holder.status.setText("PROSESED");
                break;
            case "4":
                holder.status.setText("ENDED");
                break;
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return pengaduanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.getMemberName) TextView nama;
        @BindView(R.id.getMemberFoto) CircleImageView memFoto;
        @BindView(R.id.getFoto) ImageView foto;
        @BindView(R.id.getKerugian) TextView rugi;
        @BindView(R.id.getAlamatPengaduan) TextView lokasi;
        @BindView(R.id.getTime) TextView time;
        @BindView(R.id.getStatus) TextView status;
        public  MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
