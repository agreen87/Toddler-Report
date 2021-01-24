package com.example.toddlerreport;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyHelper {

    Realm realm;
    RealmResults<DataModel> dataModels;

    public MyHelper(Realm realm) {
        this.realm = realm;
    }

    public void selectFromRealm() {
        dataModels = realm.where(DataModel.class).findAll();
    }

    public ArrayList<DataModel> justRefresh() {
        ArrayList<DataModel> listItems = new ArrayList<>();
        for (DataModel d : dataModels) {
            listItems.add(d);
        }
        return listItems;
    }
}
