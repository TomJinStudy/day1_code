package com.jin.dao.service.impl;

public class user {
    private  String name;
    private  String adres;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "user{" +
                "name='" + name + '\'' +
                ", adres='" + adres + '\'' +
                '}';
    }

    public String getAdres() {
        return adres;

    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
