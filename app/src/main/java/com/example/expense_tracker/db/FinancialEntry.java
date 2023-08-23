package com.example.expense_tracker.db;

public class FinancialEntry {
    private int id;
    private String transactionType;
    private int defaultCategoryIconId;
    private String description;
    private float money;
    private String time;
    private int year;
    private int month;
    private int day;
    private TransactionCategory.AccountType accountType;

    public FinancialEntry() {

    }

    public FinancialEntry(int id, String transactionType, int defaultCategoryIconId, String description, float money, String time, int year, int month, int day, TransactionCategory.AccountType accountType) {
        this.id = id;
        this.transactionType = transactionType;
        this.defaultCategoryIconId = defaultCategoryIconId;
        this.description = description;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.accountType = accountType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDefaultCategoryIconId(int defaultCategoryIconId) {
        this.defaultCategoryIconId = defaultCategoryIconId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setAccountType(TransactionCategory.AccountType accountType) {
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getDefaultCategoryIconId() {
        return defaultCategoryIconId;
    }

    public String getDescription() {
        return description;
    }

    public float getMoney() {
        return money;
    }

    public String getTime() {
        return time;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public TransactionCategory.AccountType getAccountType() {
        return accountType;
    }
}
