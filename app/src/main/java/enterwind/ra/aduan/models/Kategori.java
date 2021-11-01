package enterwind.ra.aduan.models;

public class Kategori {

    public String id, label;

    public Kategori(){

    }

    public Kategori(String id, String label){
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
