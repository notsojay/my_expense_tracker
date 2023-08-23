package com.example.expense_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense_tracker.adapter.AccountAdapter;
import com.example.expense_tracker.db.FinancialEntry;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.utilities.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyListView;
    private TextView timeTextView;
    private List<FinancialEntry> datas;
    private AccountAdapter accountAdapter;
    private int year, month;
    private int dialogSelPos = -1;
    private int dialogSelMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListView = findViewById(R.id.history_lv);
        timeTextView = findViewById(R.id.history_tv_time);
        datas = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, datas);
        historyListView.setAdapter(accountAdapter);
        initTime();
        timeTextView.setText(month + "/" + year);
        loadData(year, month);
        setListViewClickListener();
    }

    private void setListViewClickListener() {
        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FinancialEntry financialEntry = datas.get(position);
                deleteItem(financialEntry);
                return false;
            }
        });
    }

    private void deleteItem(final FinancialEntry financialEntry) {
        final int delId = financialEntry.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning")
                .setMessage("Delete this record？")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteItemFromAccountTableById(delId);
                        datas.remove(financialEntry);
                        accountAdapter.notifyDataSetChanged();
                    }
                });

        builder.create().show();
    }

    private void loadData(int year,int month) {
        List<FinancialEntry> list = DBManager.getAccountListMonthFromAccountTable(year, month);
        datas.clear();
        datas.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if(viewId == R.id.history_iv_back) {
            finish();
        }
        else if (viewId == R.id.history_iv_rili) {
            CalendarDialog dialog = new CalendarDialog(this,dialogSelPos, dialogSelMonth);
            dialog.show();
            dialog.setDialogSize();

            dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                @Override
                public void onRefresh(int selPos, int year, int month) {
                    timeTextView.setText(month + "/" + year);
                    loadData(year,month);
                    dialogSelPos = selPos;
                    dialogSelMonth = month;
                }
            });
        }
    }
}