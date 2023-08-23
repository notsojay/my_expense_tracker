package com.example.expense_tracker.frag_record;

import android.os.Bundle;

import com.example.expense_tracker.R;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;

import java.util.List;

public class IncomeRecordFragment extends BaseRecordFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financialEntry.setTransactionType("Other");
        financialEntry.setDefaultCategoryIconId(R.mipmap.in_qt_fs);
    }

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TransactionCategory> incomeList = DBManager.getTypeList(TransactionCategory.AccountType.INCOME);
        typeList.addAll(incomeList);
        typeBaseAdapter.notifyDataSetChanged();
        typeTextView.setText(R.string.other);
        typeImageView.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        financialEntry.setAccountType(TransactionCategory.AccountType.INCOME);
        DBManager.insertItemToAccountTable(financialEntry);
    }
}