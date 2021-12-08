package com.example.rybd;

public class RegisterHelper {

    String email,password,nama;

    public RegisterHelper() {
    }

    public RegisterHelper(String email, String password, String nama) {
        this.email = email;
        this.password = password;
        this.nama = nama;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNama() {
        return nama;
    }
}
