package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.expense_tracker.adapter.AccountAdapter;
import com.example.expense_tracker.db.FinancialEntry;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;
import com.example.expense_tracker.utilities.BudgetDialog;
import com.example.expense_tracker.utilities.MoreActionsDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView recordListView;
    private ImageView searchImageView;
    private Button editBtn;
    private ImageButton moreBtn;
    private List<FinancialEntry> accountData;
    private AccountAdapter accountAdapter;
    private int year, month, day;
    private View headerView;
    private TextView topOutTextView, topInTextView, topBudgetTextView, topConTextView;
    private ImageView topShowImageView;
    private SharedPreferences preferences;
    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        addListViewToHeaderView();
        accountData = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, accountData);
        recordListView.setAdapter(accountAdapter);
    }

    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTextView();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.main_iv_search) {
            Intent it = new Intent(this, SearchActivity.class);
            startActivity(it);
        }
        else if (viewId == R.id.main_btn_edit) {
            Intent it1 = new Intent(this, RecordActivity.class);
            startActivity(it1);
        }
        else if (viewId == R.id.main_btn_more) {
            MoreActionsDialog moreActionsDialog = new MoreActionsDialog(this);
            moreActionsDialog.show();
            moreActionsDialog.setDialogSize();
        }
        else if (viewId == R.id.item_main_lv_top_tv_budget) {
            showBudgetDialog();
        }
        else if(viewId == R.id.item_main_lv_top_iv_hide) {
            toggleShow();
        }

        if (view == headerView) {
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        recordListView = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchImageView = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchImageView.setOnClickListener(this);
        setListViewLongClickListener();
    }

    private void loadDBData() {
        List<FinancialEntry> list = DBManager.getAccountListMonthFromAccountTable(year, month);
        accountData.clear();
        accountData.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    private void addListViewToHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_main_list_view_top, null);
        recordListView.addHeaderView(headerView);
        topOutTextView = headerView.findViewById(R.id.item_main_lv_top_tv_out);
        topInTextView = headerView.findViewById(R.id.item_main_lv_top_tv_in);
        topBudgetTextView = headerView.findViewById(R.id.item_main_lv_top_tv_budget);
        topConTextView = headerView.findViewById(R.id.item_main_lv_top_tv_day);
        topShowImageView = headerView.findViewById(R.id.item_main_lv_top_iv_hide);

        topBudgetTextView.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowImageView.setOnClickListener(this);
    }

    private void setTopTextView() {
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, TransactionCategory.AccountType.INCOME);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, TransactionCategory.AccountType.EXPENSE);
        String infoOneDay = "Today's expenses $" + outcomeOneDay + "  income $" + incomeOneDay;
        topConTextView.setText(infoOneDay);

        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, TransactionCategory.AccountType.INCOME);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, TransactionCategory.AccountType.EXPENSE);
        topInTextView.setText("$" + incomeOneMonth);
        topOutTextView.setText("$" + outcomeOneMonth);

        float budgetMoney = preferences.getFloat("budgetMoney", 0);
        if (budgetMoney <= 0) {
            topBudgetTextView.setText("$ 0");
        }
        else {
            float remainMoney = Math.max(0, budgetMoney - outcomeOneMonth);
            topBudgetTextView.setText("$" + remainMoney);
        }
    }

    private void setListViewLongClickListener() {
        recordListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position - 1;
                FinancialEntry clickBean = accountData.get(pos);

                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(final FinancialEntry clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alert messages")
                .setMessage("Delete this record?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    int click_id = clickBean.getId();
                    DBManager.deleteItemFromAccountTableById(click_id);
                    accountData.remove(clickBean);
                    accountAdapter.notifyDataSetChanged();
                    setTopTextView();
                });

        builder.create().show();
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("budgetMoney", money);
                editor.commit();

                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, TransactionCategory.AccountType.EXPENSE);
                float remainMoney = money - outcomeOneMonth;
                if(remainMoney < 0) {
                    Toast.makeText(MainActivity.this,"Budget amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                topBudgetTextView.setText("$" + remainMoney);
            }
        });
    }

    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTextView.setTransformationMethod(passwordMethod);
            topOutTextView.setTransformationMethod(passwordMethod);
            topBudgetTextView.setTransformationMethod(passwordMethod);
            topShowImageView.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }
        else {
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTextView.setTransformationMethod(hideMethod);
            topOutTextView.setTransformationMethod(hideMethod);
            topBudgetTextView.setTransformationMethod(hideMethod);
            topShowImageView.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}