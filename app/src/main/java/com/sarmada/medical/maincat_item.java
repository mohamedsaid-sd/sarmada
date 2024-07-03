package com.sarmada.medical;

public class maincat_item {
    String id ;
    String img;
    String maincat;
    String hot_line;
    String descripe ;
    String status ;
    String cat_id ;

    public maincat_item(String id, String img, String maincat, String hot_line, String descripe, String status, String cat_id) {
        this.id = id;
        this.img = img;
        this.maincat = maincat;
        this.hot_line = hot_line;
        this.descripe = descripe;
        this.status = status;
        this.cat_id = cat_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMaincat() {
        return maincat;
    }

    public void setMaincat(String maincat) {
        this.maincat = maincat;
    }

    public String getHot_line() {
        return hot_line;
    }

    public void setHot_line(String hot_line) {
        this.hot_line = hot_line;
    }

    public String getDescripe() {
        return descripe;
    }

    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
