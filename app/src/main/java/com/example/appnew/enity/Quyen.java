package com.example.appnew.enity;

public class Quyen {
    String id, tenQuyen;

    public Quyen(String id, String tenQuyen) {
        this.id = id;
        this.tenQuyen = tenQuyen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }
}
