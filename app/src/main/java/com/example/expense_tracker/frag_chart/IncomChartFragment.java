package com.example.expense_tracker.frag_chart;

import android.graphics.Color;
import android.view.View;

import com.example.expense_tracker.db.MonthlyBarData;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class IncomChartFragment extends BaseChartFragment {

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, TransactionCategory.AccountType.INCOME);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        List<MonthlyBarData> list = DBManager.getSumMoneyDailyInSelectMonth(year, month, TransactionCategory.AccountType.INCOME);

        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }
        else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {

                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }

            for (int i = 0; i < list.size(); i++) {
                MonthlyBarData itemBean = list.get(i);
                int day = itemBean.getDay();
                int xIndex = day-1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }

            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK);
            barDataSet1.setValueTextSize(8f);
            barDataSet1.setColor(Color.parseColor("#006400"));

            barDataSet1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getPointLabel(Entry entry) {
                    float value = entry.getY();
                    if (value == 0) {
                        return "";
                    }
                    return String.valueOf(value);
                }
            });

            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f);
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        float maxMoney = DBManager.getMaxMoneyDailyInSelectMonth(year, month, TransactionCategory.AccountType.INCOME);
        float max = (float) Math.ceil(maxMoney);

        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);
        yAxis_right.setAxisMinimum(0f);
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);


        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year, month, TransactionCategory.AccountType.INCOME);
    }
}