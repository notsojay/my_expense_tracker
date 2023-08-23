package com.example.expense_tracker.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.expense_tracker.R;
import com.example.expense_tracker.adapter.ChartItemAdapter;
import com.example.expense_tracker.db.FinancialChartData;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseChartFragment extends Fragment {
    private ListView chartListView;
    protected int year;
    protected int month;
    protected List<FinancialChartData> data;
    private ChartItemAdapter itemAdapter;
    protected BarChart barChart; // Controls that represent bar charts
    protected TextView chartTv; // TextView displayed if there is no income and expenditure situation

    protected abstract void setAxisData(int year, int month);

    protected abstract void setYAxis(int year,int month);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_incom_chart, container, false);
        chartListView = view.findViewById(R.id.frag_chart_lv);

        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        data = new ArrayList<>();

        itemAdapter = new ChartItemAdapter(getContext(), data);
        chartListView.setAdapter(itemAdapter);

        addLVHeaderView();
        return view;
    }

    protected void addLVHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top, null);

        chartListView.addHeaderView(headerView);

        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);

        barChart.getDescription().setEnabled(false);

        barChart.setExtraOffsets(20, 20, 20, 20);
        setAxis(year,month);
        setAxisData(year,month);
    }

    protected  void setAxis(int year, final int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int val = (int) value;
                int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                // Handle February leap years (assuming `year` variable is available and contains the current year)
                if (month == 2 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) {
                    daysInMonth[1] = 29;
                }

                if (val == 0) {
                    return month + "-1";
                }
                else if (val == 14) {
                    return month + "-15";
                }
                else if (val == daysInMonth[month - 1] - 1) {
                    return month + "-" + daysInMonth[month - 1];
                }

                return "";
            }
        });

        xAxis.setYOffset(10);
        setYAxis(year,month);
    }


    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;

        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    }

    public void loadData(int year, int month, TransactionCategory.AccountType kind) {
        List<FinancialChartData> list = DBManager.getChartListFromAccountTable(year, month, kind);
        data.clear();
        data.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }
}