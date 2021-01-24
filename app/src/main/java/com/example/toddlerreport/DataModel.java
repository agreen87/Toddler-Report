package com.example.toddlerreport;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class DataModel extends RealmObject {
    @PrimaryKey
    @Required
    private
    String id;

    @Required
    String name;

    @Required
    String phoneOne;

    @Required
    String phoneTwo;

    public DataModel() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public DataModel(String name, String phoneOne, String phoneTwo) {
        this.name = name;
        this.phoneOne = phoneOne;
        this.phoneTwo = phoneTwo;
    }
}
