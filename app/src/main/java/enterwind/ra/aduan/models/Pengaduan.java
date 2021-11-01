package enterwind.ra.aduan.models;

public class Pengaduan {

    public String id, member_foto, member_nama, lokasi, foto, bentuk, waktu, status;

    public Pengaduan() {

    }

    public Pengaduan(String id, String member_foto, String member_nama, String lokasi, String foto, String bentuk, String waktu, String status){
        this.id=id;
        this.member_foto = member_foto;
        this.member_nama = member_nama;
        this.lokasi = lokasi;
        this.foto = foto;
        this.bentuk = bentuk;
        this.waktu = waktu;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_foto() {
        return member_foto;
    }

    public void setMember_foto(String member_foto) {
        this.member_foto = member_foto;
    }

    public String getMember_nama() {
        return member_nama;
    }

    public void setMember_nama(String member_nama) {
        this.member_nama = member_nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBentuk() {
        return bentuk;
    }

    public void setBentuk(String bentuk) {
        this.bentuk = bentuk;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
