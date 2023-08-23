package com.example.expense_tracker.db;

public class TransactionCategory {
    public enum AccountType {
        EXPENSE, INCOME
    }

    private int id;
    private String transactionType;
    private int defaultCategoryIconId;
    private int selectedCategoryIconId;
    private AccountType accountType;

    public TransactionCategory(int id, String transactionType, int defaultCategoryIconId, int selectedCategoryIconId, AccountType accountType) {
        this.id = id;
        this.transactionType = transactionType;
        this.defaultCategoryIconId = defaultCategoryIconId;
        this.selectedCategoryIconId = selectedCategoryIconId;
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

    public void setSelectedCategoryIconId(int selectedCategoryIconId) {
        this.selectedCategoryIconId = selectedCategoryIconId;
    }

    public void setAccountType(AccountType accountType) {
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

    public int getSelectedCategoryIconId() {
        return selectedCategoryIconId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
