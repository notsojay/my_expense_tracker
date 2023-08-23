package com.example.expense_tracker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.expense_tracker.db.TransactionCategory;
import com.example.expense_tracker.R;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    private Context context;
    private List<TransactionCategory> datas;
    private int selectPos = 0;

    public TypeBaseAdapter(Context context, List<TransactionCategory> datas) {
        this.context = context;
        this.datas = datas;
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
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        TransactionCategory transactionCategory = datas.get(position);
        tv.setText(transactionCategory.getTransactionType());

        if (selectPos == position) {
            iv.setImageResource(transactionCategory.getSelectedCategoryIconId());
        }else{
            iv.setImageResource(transactionCategory.getDefaultCategoryIconId());
        }

        return convertView;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }
}
