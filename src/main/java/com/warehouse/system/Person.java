package com.warehouse.system;

import java.util.Date;

public class Person {
    public int person_id;
    public String name;
    public String sex;
    public Date birthdate;
    public String id_card;
    public String native_place;
    public String address;
    public String phone;
    public String other_info;

    public Person(int person_id, String name, String sex, Date birthdate, String id_card, String native_place, String address, String phone, String other_info) {
        this.person_id = person_id;
        this.name = name;
        this.sex = sex;
        this.birthdate = birthdate;
        this.id_card = id_card;
        this.native_place = native_place;
        this.address = address;
        this.phone = phone;
        this.other_info = other_info;
    }

    public Person() {
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getNative_place() {
        return native_place;
    }

    public void setNative_place(String native_place) {
        this.native_place = native_place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }
}