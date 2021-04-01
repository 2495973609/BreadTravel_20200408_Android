package com.example.breadtravel_20200408.entity;

public class Address {
    private String name;
    private String district;

    public Address() {

    }

    public Address(String name, String district) {
        this.name = name;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
