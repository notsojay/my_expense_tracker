package com.example.expense_tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expense_tracker.db.FinancialChartData;
import com.example.expense_tracker.utilities.FloatUtils;
import com.example.expense_tracker.R;

import java.util.List;

public class ChartItemAdapter extends BaseAdapter {
    private Context context;
    private List<FinancialChartData> data;
    private LayoutInflater layoutInflater;

    public ChartItemAdapter(Context context, List<FinancialChartData> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_chartfrag_lv,parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        FinancialChartData bean = data.get(position);
        holder.iv.setImageResource(bean.getSelectImageId());
        holder.typeTextView.setText(bean.getType());
        float ratio = bean.getRatio();
        String pert = FloatUtils.ratioToPercent(ratio);
        holder.ratioTextView.setText(pert);

        holder.totalTextView.setText("$ " + bean.getTotalMoney());
        return convertView;
    }

    class ViewHolder {
        private TextView typeTextView, ratioTextView, totalTextView;
        private ImageView iv;
        public ViewHolder(View view){
            typeTextView = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioTextView = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTextView = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}