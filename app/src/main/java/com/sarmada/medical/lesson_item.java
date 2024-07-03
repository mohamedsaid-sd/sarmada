package com.sarmada.medical;

public class lesson_item {
    String id ;
    String title;
    String vedio;
    String perf;

    public lesson_item(String id, String title, String vedio, String perf) {
        this.id = id;
        this.title = title;
        this.vedio = vedio;
        this.perf = perf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVedio() {
        return vedio;
    }

    public void setVedio(String vedio) {
        this.vedio = vedio;
    }

    public String getPerf() {
        return perf;
    }

    public void setPerf(String perf) {
        this.perf = perf;
    }
}
