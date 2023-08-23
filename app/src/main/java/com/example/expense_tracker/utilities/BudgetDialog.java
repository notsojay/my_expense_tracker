package com.example.expense_tracker.utilities;

import com.example.expense_tracker.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    private ImageView cancelIv;
    private Button ensureBtn;
    private EditText moneyEt;

    public interface OnEnsureListener{
        public void onEnsure(float money);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.dialog_budget_iv_error) {
            cancel();;
        }
        else if (viewId == R.id.dialog_budget_btn_ensure) {
            String data = moneyEt.getText().toString();
            if (TextUtils.isEmpty(data)) {
                Toast.makeText(getContext(),"Input data cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            float money = Float.parseFloat(data);
            if (money < 0) {
                Toast.makeText(getContext(),"Budget amount cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }
            if (onEnsureListener != null) {
                onEnsureListener.onEnsure(money);
            }
            cancel();
        }
    }

    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}