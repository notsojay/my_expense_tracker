package com.example.expense_tracker;

import android.app.Application;

import com.example.expense_tracker.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext());
    }
}