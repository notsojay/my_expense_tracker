package com.example.expense_tracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.expense_tracker.R;

public class DBCreator extends SQLiteOpenHelper {
    public DBCreator(@Nullable Context context) {
        super(context, "expense_tracker.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table typeTable ("
                    + "id integer primary key autoincrement,"
                    + "typename varchar(10),"
                    + "imageId integer,"
                    + "selectImageId integer,"
                    + "kind varchar(10))";

        db.execSQL(sql);
        insertType(db);

        sql = "create table accountTable ("
                + "id integer primary key autoincrement, "
                + "typename varchar(10), "
                + "selectImageId integer, "
                + "note varchar(80), "
                + "money float, "
                + "time varchar(60), "
                + "year integer, "
                + "month integer, "
                + "day integer, "
                + "kind varchar(10))";

        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        String sql = "insert into typeTable ("
                    + "typename, "
                    + "imageId, "
                    + "selectImageId, "
                    + "kind) "
                    + "values (?,?,?,?)";

        String expenseType = TransactionCategory.AccountType.EXPENSE.name();
        db.execSQL(sql, new Object[]{"other", R.mipmap.ic_qita, R.mipmap.ic_qita_fs, expenseType});
        db.execSQL(sql, new Object[]{"Food", R.mipmap.ic_canyin, R.mipmap.ic_canyin_fs, expenseType});
        db.execSQL(sql, new Object[]{"Traffic", R.mipmap.ic_jiaotong, R.mipmap.ic_jiaotong_fs, expenseType});
        db.execSQL(sql, new Object[]{"Shopping", R.mipmap.ic_gouwu, R.mipmap.ic_gouwu_fs, expenseType});
        db.execSQL(sql, new Object[]{"Outfits", R.mipmap.ic_fushi, R.mipmap.ic_fushi_fs, expenseType});
        db.execSQL(sql, new Object[]{"Daily", R.mipmap.ic_riyongpin, R.mipmap.ic_riyongpin_fs, expenseType});
        db.execSQL(sql, new Object[]{"Entmt", R.mipmap.ic_yule, R.mipmap.ic_yule_fs, expenseType});
        db.execSQL(sql, new Object[]{"Snacks", R.mipmap.ic_lingshi, R.mipmap.ic_lingshi_fs, expenseType});
        db.execSQL(sql, new Object[]{"Beverage", R.mipmap.ic_yanjiu, R.mipmap.ic_yanjiu_fs, expenseType});
        db.execSQL(sql, new Object[]{"Education", R.mipmap.ic_xuexi, R.mipmap.ic_xuexi_fs, expenseType});
        db.execSQL(sql, new Object[]{"Health", R.mipmap.ic_yiliao, R.mipmap.ic_yiliao_fs, expenseType});
        db.execSQL(sql, new Object[]{"Housing", R.mipmap.ic_zhufang, R.mipmap.ic_zhufang_fs, expenseType});
        db.execSQL(sql, new Object[]{"Power", R.mipmap.ic_shuidianfei, R.mipmap.ic_shuidianfei_fs, expenseType});
        db.execSQL(sql, new Object[]{"Cellular", R.mipmap.ic_tongxun, R.mipmap.ic_tongxun_fs, expenseType});
        db.execSQL(sql, new Object[]{"Social", R.mipmap.ic_renqingwanglai, R.mipmap.ic_renqingwanglai_fs, expenseType});

        String incomeType = TransactionCategory.AccountType.INCOME.name();
        db.execSQL(sql, new Object[]{"Other", R.mipmap.in_qt, R.mipmap.in_qt_fs, incomeType});
        db.execSQL(sql, new Object[]{"Salary", R.mipmap.in_xinzi, R.mipmap.in_xinzi_fs, incomeType});
        db.execSQL(sql, new Object[]{"Bonus", R.mipmap.in_jiangjin, R.mipmap.in_jiangjin_fs, incomeType});
        db.execSQL(sql, new Object[]{"Loan", R.mipmap.in_jieru, R.mipmap.in_jieru_fs, incomeType});
        db.execSQL(sql, new Object[]{"Repayment", R.mipmap.in_shouzhai, R.mipmap.in_shouzhai_fs, incomeType});
        db.execSQL(sql, new Object[]{"Interest", R.mipmap.in_lixifuji, R.mipmap.in_lixifuji_fs, incomeType});
        db.execSQL(sql, new Object[]{"Investment", R.mipmap.in_touzi, R.mipmap.in_touzi_fs, incomeType});
        db.execSQL(sql, new Object[]{"Reselling", R.mipmap.in_ershoushebei, R.mipmap.in_ershoushebei_fs, incomeType});
        db.execSQL(sql, new Object[]{"Windfall", R.mipmap.in_yiwai, R.mipmap.in_yiwai_fs, incomeType});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
