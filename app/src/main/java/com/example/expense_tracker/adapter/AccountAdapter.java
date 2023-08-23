package com.example.expense_tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expense_tracker.R;
import com.example.expense_tracker.db.FinancialEntry;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private Context context;
    private List<FinancialEntry> financialEntries;
    private LayoutInflater inflater;
    private int year, month, day;
    private class ViewHolder{
        private ImageView typeImageView;
        private TextView typeTextView, notesTextView, timeTextView, moneyTextView;

        public ViewHolder(View view) {
            typeImageView = view.findViewById(R.id.item_main_lv_iv);
            typeTextView = view.findViewById(R.id.item_main_lv_tv_title);
            timeTextView = view.findViewById(R.id.item_main_lv_tv_time);
            notesTextView = view.findViewById(R.id.item_main_lv_tv_note);
            moneyTextView = view.findViewById(R.id.item_main_lv_tv_money);
        }
    }

    public AccountAdapter(Context context, List<FinancialEntry> financialEntries) {
        this.context = context;
        this.financialEntries = financialEntries;
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return financialEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return financialEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_main_list_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        FinancialEntry bean = financialEntries.get(position);
        holder.typeImageView.setImageResource(bean.getDefaultCategoryIconId());
        holder.typeTextView.setText(bean.getTransactionType());
        holder.notesTextView.setText(bean.getDescription());
        holder.moneyTextView.setText("$ "+ bean.getMoney());

        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTextView.setText("Today "+ time);
        }
        else {
            holder.timeTextView.setText(bean.getTime());
        }

        return convertView;
    }
}