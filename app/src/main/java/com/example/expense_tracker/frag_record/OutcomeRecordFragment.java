package com.example.expense_tracker.frag_record;

import android.os.Bundle;

import com.example.expense_tracker.R;
import com.example.expense_tracker.db.DBManager;
import com.example.expense_tracker.db.TransactionCategory;

import java.util.List;

public class OutcomeRecordFragment extends BaseRecordFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financialEntry.setTransactionType("Other");
        financialEntry.setDefaultCategoryIconId(R.mipmap.ic_qita_fs);
    }

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TransactionCategory> outcomeList = DBManager.getTypeList(TransactionCategory.AccountType.EXPENSE);
        typeList.addAll(outcomeList);
        typeBaseAdapter.notifyDataSetChanged();
        typeTextView.setText(R.string.other);
        typeImageView.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        financialEntry.setAccountType(TransactionCategory.AccountType.EXPENSE);
        DBManager.insertItemToAccountTable(financialEntry);
    }
}
