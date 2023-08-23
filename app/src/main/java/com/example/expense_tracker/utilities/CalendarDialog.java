package com.example.expense_tracker.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.expense_tracker.R;
import com.example.expense_tracker.adapter.CalendarAdapter;
import com.example.expense_tracker.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    private ImageView errorIv;
    private GridView gridView;
    private LinearLayout hsvLayout;
    private List<TextView> hsvViewList;
    private List<Integer> yearList;
    private int selectPos = -1;
    private CalendarAdapter calendarAdapter;
    private int selectMonth = -1;

    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gridView = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        errorIv.setOnClickListener(this);
        addViewToLayout();
        initGridView();
        setGVListener();
    }

    private void setGVListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calendarAdapter.selPos = position;
                calendarAdapter.notifyDataSetInvalidated();
                int month = position + 1;
                int year = calendarAdapter.getYear();
                onRefreshListener.onRefresh(selectPos, year, month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        calendarAdapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            calendarAdapter.selPos = month;
        }else {
            calendarAdapter.selPos = selectMonth-1;
        }
        gridView.setAdapter(calendarAdapter);
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>();
        yearList = DBManager.getYearListFromAccountTable();

        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            hsvLayout.addView(view);
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year + "");
            hsvViewList.add(hsvTv);
        }
        if (selectPos == -1) {
            selectPos = hsvViewList.size()-1;
        }

        changeTvbg(selectPos);
        setHSVClickListener();
    }

    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView view = hsvViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvbg(pos);
                    selectPos = pos;
                    int year = yearList.get(selectPos);
                    calendarAdapter.setYear(year);
                }
            });
        }
    }

    private void changeTvbg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }

        TextView selView = hsvViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.main_record_btn_bg);
        selView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == R.id.dialog_calendar_iv) {
            cancel();
        }
    }


    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}