package com.example.expense_tracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expense_tracker.R;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private int year;
    public int selPos = -1;

    public void setYear(int year) {
        this.year = year;
        datas.clear();
        loadDatas(year);
        notifyDataSetChanged();
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        datas = new ArrayList<>();
        loadDatas(year);
    }

    private void loadDatas(int year) {
        for (int i = 1; i < 13; i++) {
            String data = i + "/" + year;
            datas.add(data);
        }
    }

    public int getYear() {
        return year;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, parent, false);
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);
        tv.setText(datas.get(position));
        tv.setBackgroundResource(R.color.grey_f3f3f3);
        tv.setTextColor(Color.BLACK);
        if (position == selPos) {
            tv.setBackgroundResource(R.color.green_006400);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}