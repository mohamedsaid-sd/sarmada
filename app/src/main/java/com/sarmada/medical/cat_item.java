package com.sarmada.medical;

public class cat_item {
    String id ;  // رقم الفئة
    String img ; // صورة الفئة
    String cat ; // اسم الفئة
    String status; // حالة الفئة

    public cat_item(String id, String img, String cat, String status) {
        this.id = id;
        this.img = img;
        this.cat = cat;
        this.status = status;
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

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
