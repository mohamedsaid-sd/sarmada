package com.sarmada.medical;

public class class_item {

    String id ;
    String name ;
    String zoom ;

    public class_item(String id, String name, String zoom) {
        this.id = id;
        this.name = name;
        this.zoom = zoom;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setZoom(String sclss) {
        this.zoom = sclss;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZoom() {
        return zoom;
    }
}
