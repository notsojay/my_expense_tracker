package com.example.expense_tracker.utilities;
import com.example.expense_tracker.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class RecordCommentDialog extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button cancelBtn, confirmBtn;
    private OnEnsureListener onEnsureListener;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    public interface OnEnsureListener {
        public void onEnsure();
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public RecordCommentDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_record_notes);
        editText = findViewById(R.id.dialog_notes_edit);
        cancelBtn = findViewById(R.id.dialog_record_notes_cancel_btn);
        confirmBtn = findViewById(R.id.dialog_record_notes_confirm_btn);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if (vId == R.id.dialog_record_notes_cancel_btn) {
            cancel();
        }
        else
        {
            if (onEnsureListener != null) {
                onEnsureListener.onEnsure();
            }
        }
    }

    public String getEditText(){
        return editText.getText().toString().trim();
    }

    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1, 100);
    }
}
