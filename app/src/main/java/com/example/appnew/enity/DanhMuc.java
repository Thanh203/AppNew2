package com.example.appnew.enity;

public class DanhMuc {
    private String IDDanhMuc;
    private String TenDanhMuc;
    private String Anh;
    private String Link;

    public DanhMuc(){

    }
    public DanhMuc(String IDDanhMuc, String tenDanhMuc, String anh, String link) {
        this.IDDanhMuc = IDDanhMuc;
        TenDanhMuc = tenDanhMuc;
        Anh = anh;
        Link = link;
    }

    public String getIDDanhMuc() {
        return IDDanhMuc;
    }

    public void setIDDanhMuc(String idDanhMuc) {
        IDDanhMuc = idDanhMuc;
    }


    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
