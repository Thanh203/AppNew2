package com.example.appnew.enity;

public class TinTuc {
    public String IDBaiBao, tenBaiBao, noiDung;
    public int soLuotXem, soLuotYeuThich, soLuotBinhLuan;
    public String anh, ngayDang, tacGia, linkBaiBao, IDDanhMuc, trangThai;

    public TinTuc() {

    }

    public TinTuc(String tenBaiBao, String linkBaiBao, String ngayDang, String anh) {
        this.tenBaiBao = tenBaiBao;
        this.anh = anh;
        this.ngayDang = ngayDang;
        this.linkBaiBao = linkBaiBao;
    }

    public TinTuc(String IDBaiBao, String tenBaiBao, String noiDung, int soLuotXem, int soLuotYeuThich, int soLuotBinhLuan, String anh, String ngayDang, String tacGia, String linkBaiBao, String IDDanhMuc, String trangThai) {
        this.IDBaiBao = IDBaiBao;
        this.tenBaiBao = tenBaiBao;
        this.noiDung = noiDung;
        this.soLuotXem = soLuotXem;
        this.soLuotYeuThich = soLuotYeuThich;
        this.soLuotBinhLuan = soLuotBinhLuan;
        this.anh = anh;
        this.ngayDang = ngayDang;
        this.tacGia = tacGia;
        this.linkBaiBao = linkBaiBao;
        this.IDDanhMuc = IDDanhMuc;
        this.trangThai = trangThai;
    }

    public String getIDBaiBao() {
        return IDBaiBao;
    }

    public void setIDBaiBao(String IDBaiBao) {
        this.IDBaiBao = IDBaiBao;
    }

    public String getTenBaiBao() {
        return tenBaiBao;
    }

    public void setTenBaiBao(String tenBaiBao) {
        this.tenBaiBao = tenBaiBao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getSoLuotXem() {
        return soLuotXem;
    }

    public void setSoLuotXem(int soLuotXem) {
        this.soLuotXem = soLuotXem;
    }

    public int getSoLuotYeuThich() {
        return soLuotYeuThich;
    }

    public void setSoLuotYeuThich(int soLuotYeuThich) {
        this.soLuotYeuThich = soLuotYeuThich;
    }

    public int getSoLuotBinhLuan() {
        return soLuotBinhLuan;
    }

    public void setSoLuotBinhLuan(int soLuotBinhLuan) {
        this.soLuotBinhLuan = soLuotBinhLuan;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getLinkBaiBao() {
        return linkBaiBao;
    }

    public void setLinkBaiBao(String linkBaiBao) {
        this.linkBaiBao = linkBaiBao;
    }

    public String getIDDanhMuc() {
        return IDDanhMuc;
    }

    public void setIDDanhMuc(String IDDanhMuc) {
        this.IDDanhMuc = IDDanhMuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}