package com.example.expense_tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.expense_tracker.utilities.FloatUtils;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBCreator helper = new DBCreator(context);
        db = helper.getWritableDatabase();
    }

    public static List<TransactionCategory> getTypeList(TransactionCategory.AccountType kind) {
        List<TransactionCategory> typelist = new ArrayList<>();
        String sql = "select * from typeTable where kind = '" + kind.name() + "'";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int index = 0, id = 0, selectImageId = 0, imageId = 0;
            String typename = "", kind1 = "";

            index = cursor.getColumnIndex("typename");
            if (index != -1) {
                typename = cursor.getString(index);
            }

            index = cursor.getColumnIndex("imageId");
            if (index != -1) {
                imageId = cursor.getInt(index);
            }

            index = cursor.getColumnIndex("selectImageId");
            if (index != -1) {
                selectImageId = cursor.getInt(index);
            }

            index = cursor.getColumnIndex("kind");
            if (index != -1) {
                kind1 = cursor.getString(index);
            }

            index = cursor.getColumnIndex("id");
            if (index != -1) {
                id = cursor.getInt(index);
            }

            TransactionCategory transactionCategory = new TransactionCategory(id, typename, imageId, selectImageId, kind);
            typelist.add(transactionCategory);
        }

        cursor.close();
        return typelist;
    }

    public static void insertItemToAccountTable(FinancialEntry bean) {
        ContentValues values = new ContentValues();

        values.put("typename", bean.getTransactionType());
        values.put("selectImageId", bean.getDefaultCategoryIconId());
        values.put("note", bean.getDescription());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getAccountType().name());

        long checkIfSucc = db.insert("accountTable",null, values);
        if(checkIfSucc == -1) Log.d("jiahal", "insertItemToAccountTable: failed");
        else Log.d("jiahal", "insertItemToAccountTable: succ");
    }

    // Utility methods to retrieve data for specific time frames
    public static float getSumMoneyOneDay(int year, int month, int day, TransactionCategory.AccountType kind) {
        return getSumMoney(year, month, day, kind);
    }

    public static float getSumMoneyOneMonth(int year, int month, TransactionCategory.AccountType kind) {
        return getSumMoney(year, month, null, kind);
    }

    public static float getSumMoneyOneYear(int year, TransactionCategory.AccountType kind) {
        return getSumMoney(year, null, null, kind);
    }

    public static float getSumMoney(int year, Integer month, Integer day, TransactionCategory.AccountType kind) {
        float total = 0.0f;

        // Build the SQL query dynamically based on provided parameters
        StringBuilder sqlBuilder = new StringBuilder("select sum(money) from accountTable where year=? and kind=?");
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(year));
        params.add(kind.name());

        if (month != null) {
            sqlBuilder.append(" and month=?");
            params.add(String.valueOf(month));

            if (day != null) {
                sqlBuilder.append(" and day=?");
                params.add(String.valueOf(day));
            }
        }

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sqlBuilder.toString(), params.toArray(new String[0]));

            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("sum(money)");
                float money = cursor.getFloat(index);
                total = money;
            }
        }
        catch (Exception e) {
            // Handle or log the exception
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return total;
    }

    public static List<FinancialEntry> getAccountListMonthFromAccountTable(int year, int month) {
        String sql = "select * from accountTable where year=? and month=? order by year desc, month desc, day desc, id desc";
        return getAccountListByQuery(sql, Integer.toString(year), Integer.toString(month));
    }

    public static List<FinancialEntry> getAccountListDayFromAccountTable(int year, int month, int day) {
        String sql = "select * from accountTable where year=? and month=? and day=? order by year desc, month desc, day desc, id desc";
        return getAccountListByQuery(sql, Integer.toString(year), Integer.toString(month), Integer.toString(day));
    }

    public static List<FinancialEntry> getAccountListByRemarkFromAccountTable(String noteMsg) {
        String sql = "select * from accountTable where note like ?";
        return getAccountListByQuery(sql, "%" + noteMsg + "%");
    }

    private static List<FinancialEntry> getAccountListByQuery(String sql, String... args) {
        List<FinancialEntry> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()) {
            FinancialEntry financialEntry = extractBeanFromCursor(cursor);
            list.add(financialEntry);
        }

        cursor.close();
        return list;
    }

    private static FinancialEntry extractBeanFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
        String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        int selectImageId = cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId"));
        float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
        int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        String kindStr = cursor.getString(cursor.getColumnIndexOrThrow("kind"));
        TransactionCategory.AccountType kind = kindStr.equals("EXPENSE") ? TransactionCategory.AccountType.EXPENSE : TransactionCategory.AccountType.INCOME;

        return new FinancialEntry(id, typename, selectImageId, note, money, time, year, month, day, kind);
    }

    public static List<Integer> getYearListFromAccountTable() {
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from accountTable order by year asc";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int yearIndex = cursor.getColumnIndex("year");
            int year = cursor.getInt(yearIndex);
            list.add(year);
        }

        cursor.close();
        return list;
    }

    public static int deleteItemFromAccountTableById(int id) {
        int i = db.delete("accountTable", "id=?", new String[]{id + ""});
        return i;
    }

    public static void deleteAllAccount() {
        String sql = "delete from accountTable";
        db.execSQL(sql);
    }

    public static List<FinancialChartData> getChartListFromAccountTable(int year, int month, TransactionCategory.AccountType kind) {
        List<FinancialChartData>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);
        String kindStr = kind.name();
        String sql = "select typename, selectImageId, sum(money) as total from accountTable where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kindStr + ""});

        while (cursor.moveToNext()) {
            int selectImageIdIndex = cursor.getColumnIndex("selectImageId");
            int typenameIndex = cursor.getColumnIndex("typename");
            int totalIndex = cursor.getColumnIndex("total");

            int selectImageId = cursor.getInt(selectImageIdIndex);
            String typename = cursor.getString(typenameIndex);
            float total = cursor.getFloat(totalIndex);

            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            FinancialChartData bean = new FinancialChartData(selectImageId, typename, ratio, total);
            list.add(bean);
        }

        cursor.close();
        return list;
    }


    public static float getMaxMoneyDailyInSelectMonth(int year, int month, TransactionCategory.AccountType kind) {
        String kindStr = kind.name();
        String sql = "select sum(money) from accountTable where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kindStr + ""});

        if (cursor.moveToFirst()) {
            int moneyIndex = cursor.getColumnIndex("sum(money)");
            float money = cursor.getFloat(moneyIndex);
            return money;
        }

        cursor.close();
        return 0;
    }

    public static List<MonthlyBarData> getSumMoneyDailyInSelectMonth(int year, int month, TransactionCategory.AccountType kind) {
        String kindStr = kind.name();
        String sql = "select day, sum(money) from accountTable where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kindStr + ""});
        List<MonthlyBarData>list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int dayIndex = cursor.getColumnIndex("day");
            int sumMoneyIndex = cursor.getColumnIndex("sum(money)");

            int day = cursor.getInt(dayIndex);
            float sumMoney = cursor.getFloat(sumMoneyIndex);
            MonthlyBarData itemBean = new MonthlyBarData(year, month, day, sumMoney);
            list.add(itemBean);
        }

        cursor.close();
        return list;
    }

    public static int getCountItemMonthly(int year, int month, TransactionCategory.AccountType kind) {
        int total = 0;
        String kindStr = kind.name();
        String sql = "select count(money) from accountTable where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kindStr + ""});

        if (cursor.moveToFirst()) {
            int countIndex = cursor.getColumnIndex("count(money)");
            int count = cursor.getInt(countIndex);
            total = count;
        }

        cursor.close();
        return total;
    }
}
