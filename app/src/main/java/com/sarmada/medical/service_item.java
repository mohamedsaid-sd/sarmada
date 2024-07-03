package com.sarmada.medical;

public class service_item {
    String id ;
    String name ;
    String price ;
    String present ;
    String pprice ;


    public service_item(String id, String name, String price, String present, String pprice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.present = present;
        this.pprice = pprice;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
