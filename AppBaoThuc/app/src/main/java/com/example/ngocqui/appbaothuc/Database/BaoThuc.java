package com.example.ngocqui.appbaothuc.Database;

import java.sql.Time;
import java.util.Calendar;

public class BaoThuc {
    private int Id;
    private int CachTat;
    private String ThoiGian;
    private int isBat;
    private int[] LapLai;

    public BaoThuc(int cachTat, String thoiGian) {
        CachTat = cachTat;
        ThoiGian = thoiGian;
    }

    public BaoThuc(int id, int cachTat, String thoiGian) {
        Id = id;
        CachTat = cachTat;
        ThoiGian = thoiGian;
    }

    public BaoThuc(int id, int cachTat, String thoiGian, int isBat) {
        Id = id;
        CachTat = cachTat;
        ThoiGian = thoiGian;
        this.isBat = isBat;
    }

    public int getIsBat() {
        return isBat;
    }

    public void setIsBat(int isBat) {
        this.isBat = isBat;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCachTat() {
        return CachTat;
    }

    public void setCachTat(int cachTat) {
        CachTat = cachTat;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }

    public int[] getLapLai() {
        return LapLai;
    }

    public void setLapLai(int[] lapLai) {
        LapLai = lapLai;
    }
}
