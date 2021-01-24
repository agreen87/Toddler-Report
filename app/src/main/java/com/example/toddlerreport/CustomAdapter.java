package com.example.toddlerreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataModel> dataModels;

    String phoneOne;
    String phoneTwo;

    public CustomAdapter(Context context, ArrayList<DataModel> dataModels) {
        this.context = context;
        this.dataModels = dataModels;
    }

    @Override
    public int getCount() {
        return dataModels.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_items, parent, false);

        TextView textView;
        textView = view.findViewById(R.id.textView);

        DataModel d = (DataModel) this.getItem(position);

        textView.setText(d.getName());
        phoneOne = d.getPhoneOne();
        phoneTwo = d.getPhoneTwo();

        return view;
    }
}
