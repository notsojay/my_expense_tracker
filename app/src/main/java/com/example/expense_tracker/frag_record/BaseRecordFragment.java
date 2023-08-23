package com.example.expense_tracker.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.expense_tracker.R;
import com.example.expense_tracker.adapter.TypeBaseAdapter;
import com.example.expense_tracker.db.FinancialEntry;
import com.example.expense_tracker.db.TransactionCategory;
import com.example.expense_tracker.utilities.KeyboardUtils;
import com.example.expense_tracker.utilities.RecordCommentDialog;
import com.example.expense_tracker.utilities.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseRecordFragment} factory method to
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {
    protected KeyboardView keyboardView;
    protected EditText moneyEditText;
    protected ImageView typeImageView;
    protected TextView typeTextView, notesTextView, timeTextView;
    protected GridView typeGridView;
    protected List<TransactionCategory> typeList;
    protected TypeBaseAdapter typeBaseAdapter;
    protected FinancialEntry financialEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financialEntry = new FinancialEntry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_record_fragment, container, false);
        initView(view);
        setInitTime();
        loadDataToGV();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setGVListener();
    }

    protected void loadDataToGV() {
        typeList = new ArrayList<>();
        typeBaseAdapter = new TypeBaseAdapter(getContext(), typeList);
        typeGridView.setAdapter(typeBaseAdapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEditText = view.findViewById(R.id.frag_record_et_money);
        typeImageView = view.findViewById(R.id.frag_record_iv);
        typeTextView = view.findViewById(R.id.frag_record_tv_type);
        notesTextView = view.findViewById(R.id.frag_record_tv_notes);
        timeTextView = view.findViewById(R.id.frag_record_tv_time);
        typeGridView = view.findViewById(R.id.frag_record_gv);
        notesTextView.setOnClickListener(this);
        timeTextView.setOnClickListener(this);

        KeyboardUtils keyboardUtils = new KeyboardUtils(keyboardView, moneyEditText);

        keyboardUtils.showKeyboard();

        keyboardUtils.setOnEnsureListener(() -> {
            String moneyStr = moneyEditText.getText().toString();

            if (moneyStr.isEmpty()) {
                Toast.makeText(getActivity(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            float money = Float.parseFloat(moneyStr);
            financialEntry.setMoney(money);
            saveAccountToDB();
            getActivity().finish();
        });
    }

    // Get current time
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String time = sdf.format(date);
        timeTextView.setText(time);
        financialEntry.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        financialEntry.setYear(year);
        financialEntry.setMonth(month);
        financialEntry.setDay(day);
    }

    private void setGVListener() {
        typeGridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            typeBaseAdapter.setSelectPos(position);
            typeBaseAdapter.notifyDataSetInvalidated();
            TransactionCategory transactionCategory = typeList.get(position);
            String typename = transactionCategory.getTransactionType();
            typeTextView.setText(typename);
            financialEntry.setTransactionType(typename);
            int selectImageId = transactionCategory.getSelectedCategoryIconId();
            typeImageView.setImageResource(selectImageId);
            financialEntry.setDefaultCategoryIconId(selectImageId);
        });
    }

    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if (vId == R.id.frag_record_tv_time) {
            showCalendarDialog();
        }
        else if(vId == R.id.frag_record_tv_notes) {
            showNotesDialog();
        }
    }

    private void showCalendarDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        dialog.setOnEnsureListener((time, year, month, day) -> {
            timeTextView.setText(time);
            financialEntry.setTime(time);
            financialEntry.setYear(year);
            financialEntry.setMonth(month);
            financialEntry.setDay(day);
        });
    }

    private void showNotesDialog() {
        final RecordCommentDialog dialog = new RecordCommentDialog(getContext());
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(() -> {
            String msg = dialog.getEditText();
            if (!TextUtils.isEmpty(msg)) {
                notesTextView.setText(msg);
                financialEntry.setDescription(msg);
            }
            dialog.cancel();
        });
    }
}