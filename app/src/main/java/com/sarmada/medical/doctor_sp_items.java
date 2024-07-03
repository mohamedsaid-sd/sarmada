package com.sarmada.medical;

public class doctor_sp_items {
    String id ;
    String sp ;
    String cat_id;

    public doctor_sp_items(String id, String sp, String cat_id) {
        this.id = id;
        this.sp = sp;
        this.cat_id = cat_id;
    }

    public doctor_sp_items(String id, String sp) {
        this.id = id;
        this.sp = sp;
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

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }
}
