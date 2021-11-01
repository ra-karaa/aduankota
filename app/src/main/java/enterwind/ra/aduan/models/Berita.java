package enterwind.ra.aduan.models;

public class Berita {

    public String id, judul, konten, foto, tanggal;

    public Berita(){

    }

    public Berita(String id, String judul, String konten, String foto, String tanggal){
        this.id = id;
        this.judul = judul;
        this.konten = konten;
        this.foto = foto;
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
