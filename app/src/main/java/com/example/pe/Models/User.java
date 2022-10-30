package com.example.pe.Models;

public class User {
    private String id;
    private String name;
    public String email;
    private String password;
    private String sex;
    //private boolean sex;

    public User(){

    }

    public String isSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(String name, String sex, String email, String password) {
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


