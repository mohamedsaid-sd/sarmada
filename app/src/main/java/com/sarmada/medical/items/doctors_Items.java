package com.sarmada.medical.items;

public class doctors_Items {
    String img ;
    String id ;
    String name  ;
    String phone ;
    String sp_name  ;
    String location ;
    String price ;
    String basic ;
    String vip ;
    String work_time ;
    String maxbasic;
    String maxvip;
    String link ;
    String cv ;
    String cat_id;

    public doctors_Items(String img, String id, String name, String phone, String sp_name, String location, String work_time, String maxbasic, String maxvip, String link, String cv , String cat_id) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sp_name = sp_name;
        this.location = location;
        this.work_time = work_time;
        this.maxbasic = maxbasic;
        this.maxvip = maxvip;
        this.link = link;
        this.cv = cv;
        this.cat_id = cat_id ;
    }

    public doctors_Items(String img, String id, String name, String phone, String sp_name, String location, String work_time, String maxbasic, String maxvip, String link, String cv) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sp_name = sp_name;
        this.location = location;
        this.work_time = work_time;
        this.maxbasic = maxbasic;
        this.maxvip = maxvip;
        this.link = link;
        this.cv = cv;
    }


    public doctors_Items(String img, String id, String name, String phone, String sp_name, String location, String work_time, String maxbasic, String maxvip) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sp_name = sp_name;
        this.location = location;
        this.work_time = work_time;
        this.maxbasic = maxbasic;
        this.maxvip = maxvip;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMaxvip() {
        return maxvip;
    }

    public void setMaxvip(String maxvip) {
        this.maxvip = maxvip;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMaxbasic() {
        return maxbasic;
    }

    public void setMaxbasic(String maxbasic) {
        this.maxbasic = maxbasic;
    }
}
