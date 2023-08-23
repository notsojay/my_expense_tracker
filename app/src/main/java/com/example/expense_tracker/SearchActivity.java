package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expense_tracker.adapter.AccountAdapter;
import com.example.expense_tracker.db.FinancialEntry;
import com.example.expense_tracker.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ListView searchListView;
    private EditText searchEditText;
    private TextView emptyTextView;
    private List<FinancialEntry> datas;
    private AccountAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        datas = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, datas);
        searchListView.setAdapter(accountAdapter);
        searchListView.setEmptyView(emptyTextView);
    }

    private void initView() {
        searchEditText = findViewById(R.id.search_et);
        searchListView = findViewById(R.id.search_lv);
        emptyTextView = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.search_iv_back) {
            finish();
        }
        else if (viewId == R.id.search_iv_sh) {
            String msg = searchEditText.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {
                Toast.makeText(this,"Input cannot be empty！",Toast.LENGTH_SHORT).show();
                return;
            }
            List<FinancialEntry> list = DBManager.getAccountListByRemarkFromAccountTable(msg);
            datas.clear();
            datas.addAll(list);
            accountAdapter.notifyDataSetChanged();
        }
    }
}