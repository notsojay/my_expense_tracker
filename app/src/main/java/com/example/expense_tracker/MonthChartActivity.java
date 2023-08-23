package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.expense_tracker.adapter.ChartViewPagerAdapter;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;
import com.example.expense_tracker.frag_chart.IncomChartFragment;
import com.example.expense_tracker.frag_chart.OutcomChartFragment;
import com.example.expense_tracker.utilities.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    private Button inBtn, outBtn;
    private TextView dateTextView, incomeTextView, outcomeTextView;
    private ViewPager chartViewPger;
    private int year;
    private int month;
    private int selectPos = -1, selectMonth = -1;
    private List<Fragment> chartFragList;
    private IncomChartFragment incomChartFragment;
    private OutcomChartFragment outcomChartFragment;
    private ChartViewPagerAdapter chartViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year, month);
        initFragment();
        setViewPagerSelectListener();
    }

    private void setViewPagerSelectListener() {
        chartViewPger.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFragment() {
        chartFragList = new ArrayList<>();

        incomChartFragment = new IncomChartFragment();
        outcomChartFragment = new OutcomChartFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomChartFragment.setArguments(bundle);
        outcomChartFragment.setArguments(bundle);

        chartFragList.add(outcomChartFragment);
        chartFragList.add(incomChartFragment);

        chartViewPagerAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), chartFragList);
        chartViewPger.setAdapter(chartViewPagerAdapter);
    }

    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, TransactionCategory.AccountType.INCOME);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, TransactionCategory.AccountType.EXPENSE);
        int incountItemOneMonth = DBManager.getCountItemMonthly(year, month, TransactionCategory.AccountType.INCOME);
        int outcountItemOneMonth = DBManager.getCountItemMonthly(year, month, TransactionCategory.AccountType.EXPENSE);
        dateTextView.setText("Billing for " + month + "/" + year);
        incomeTextView.setText("A total of " + incountItemOneMonth + " incomes, $" + inMoneyOneMonth);
        outcomeTextView.setText("A total of " + outcountItemOneMonth + " expenses, $" + outMoneyOneMonth);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTextView = findViewById(R.id.chart_tv_date);
        incomeTextView = findViewById(R.id.chart_tv_in);
        outcomeTextView = findViewById(R.id.chart_tv_out);
        chartViewPger = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.chart_iv_back) {
            finish();
        }
        else if (viewId == R.id.chart_iv_rili) {
            showCalendarDialog();
        }
        else if (viewId == R.id.chart_btn_in) {
            setButtonStyle(1);
            chartViewPger.setCurrentItem(1);
        }
        else if (viewId == R.id.chart_btn_out) {
            setButtonStyle(0);
            chartViewPger.setCurrentItem(0);
        }
    }


    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year, month);
                incomChartFragment.setDate(year, month);
                outcomChartFragment.setDate(year, month);
            }
        });
    }

    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_record_btn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }
        else if (kind == 1) {
            inBtn.setBackgroundResource(R.drawable.main_record_btn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}