package com.example.expense_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.expense_tracker.db.DBManager;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.setting_iv_back) {
            finish();
        }
        else if (viewId == R.id.setting_tv_clear) {
            showDeleteDialog();
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning")
                .setMessage("Delete all records?\nUnrecoverable after deletion")
                .setPositiveButton("Cancel",null)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllAccount();
                        Toast.makeText(SettingActivity.this,"Deleted successfully!",Toast.LENGTH_SHORT).show();
                    }
                });

        builder.create().show();
    }
}