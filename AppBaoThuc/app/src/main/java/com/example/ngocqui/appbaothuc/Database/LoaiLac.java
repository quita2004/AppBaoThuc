package com.example.ngocqui.appbaothuc.Database;

public class LoaiLac {
    private int SoLanTat;
    private int doNhay;

    public LoaiLac(int soLanTat, int doNhay) {
        SoLanTat = soLanTat;
        this.doNhay = doNhay;
    }

    public int getSoLanTat() {
        return SoLanTat;
    }

    public void setSoLanTat(int soLanTat) {
        SoLanTat = soLanTat;
    }

    public int getDoNhay() {
        return doNhay;
    }

    public void setDoNhay(int doNhay) {
        this.doNhay = doNhay;
    }
}
