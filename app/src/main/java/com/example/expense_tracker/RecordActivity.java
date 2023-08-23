package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.expense_tracker.adapter.RecordPagerAdapter;
import com.example.expense_tracker.frag_record.OutcomeRecordFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.expense_tracker.frag_record.IncomeRecordFragment;

public class RecordActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        initPager();
    }

    private void initPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        OutcomeRecordFragment outFrag = new OutcomeRecordFragment();
        IncomeRecordFragment inFrag = new IncomeRecordFragment();

        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.record_iv_back) {
            finish();
        }
    }
}