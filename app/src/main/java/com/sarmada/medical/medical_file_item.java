package com.sarmada.medical;

public class medical_file_item{
    String id ;
    String cid ;
    String img ;
    String txt ;
    String date ;
    String flag ;

    public medical_file_item(String id, String img, String txt, String date , String flag) {
        this.id = id;
        this.img = img;
        this.txt = txt;
        this.date = date;
        this.flag = flag ;
    }

    public medical_file_item(String id, String img, String txt, String date) {
        this.id = id;
        this.img = img;
        this.txt = txt;
        this.date = date;
    }



    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
