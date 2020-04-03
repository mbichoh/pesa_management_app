package com.nathanmbichoh.pesa_management_app.model;

public class User {

    private int id;
    private String name, email, phone, code;

    public User(int id, String name, String email, String phone, String code) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }
}
