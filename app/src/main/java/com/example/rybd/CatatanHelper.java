package com.example.rybd;

import android.widget.EditText;

public class CatatanHelper {
    String judul, isian, tanggal,jam,remainder;
    Integer id;

    public CatatanHelper() {
    }

    public CatatanHelper(Integer id, String judul, String isian, String tanggal, String jam, String remainder) {
        this.id = id;
        this.judul = judul;
        this.isian = isian;
        this.tanggal = tanggal;
        this.jam = jam;
        this.remainder = remainder;
    }

    public String getJudul() {
        return judul;
    }

    public String getIsian() {
        return isian;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getRemainder() {
        return remainder;
    }

    public Integer getId() {
        return id;
    }
}
