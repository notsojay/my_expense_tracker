package com.example.expense_tracker.utilities;
import com.example.expense_tracker.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    private EditText hourEdit, minuteEdit;
    private DatePicker datePicker;
    private Button confirmBtn, cancelBtn;
    private OnEnsureListener onEnsureListener;

    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        hourEdit = findViewById(R.id.dialog_hour_ed);
        minuteEdit = findViewById(R.id.dialog_minute_ed);
        datePicker = findViewById(R.id.dialog_time_dp);
        confirmBtn = findViewById(R.id.dialog_time_confirm_btn);
        cancelBtn = findViewById(R.id.dialog_time_cancel_btn);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if (vId == R.id.dialog_time_cancel_btn) {
            cancel();
        }
        else if (vId == R.id.dialog_time_confirm_btn) {
            int year = datePicker.getYear();
            int month = datePicker.getMonth()+1;
            int dayOfMonth = datePicker.getDayOfMonth();

            String monthStr = String.valueOf(month);
            if (month < 10){
                monthStr = "0" + month;
            }

            String dayStr = String.valueOf(dayOfMonth);
            if (dayOfMonth < 10){
                dayStr = "0" + dayOfMonth;
            }

            String hourStr = hourEdit.getText().toString();
            int hour = 0;
            if (!TextUtils.isEmpty(hourStr)) {
                hour = Integer.parseInt(hourStr);
                hour = hour % 24;
            }

            String minuteStr = minuteEdit.getText().toString();
            int minute = 0;
            if (!TextUtils.isEmpty(minuteStr)) {
                minute = Integer.parseInt(minuteStr);
                minute = minute % 60;
            }

            hourStr = String.valueOf(hour);
            if (hour < 10) {
                hourStr = "0" + hour;
            }

            minuteStr = String.valueOf(minute);
            if (minute<10) {
                minuteStr = "0" + minute;
            }

            String timeFormat = year + "/" + monthStr + "/"+ dayStr +"/ "+ hourStr + ":" + minuteStr;
            if (onEnsureListener != null) {
                onEnsureListener.onEnsure(timeFormat, year, month, dayOfMonth);
            }

            cancel();
        }
    }

    // hide DatePicker's Hide Header Layout
    private void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }

        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }

        // 5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }

        // 6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
