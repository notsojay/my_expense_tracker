package com.example.expense_tracker.db;

public class FinancialChartData {
    int selectImageId;
    String type;
    float ratio;
    float totalMoney;

    public FinancialChartData() {

    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public String getType() {
        return type;
    }

    public float getRatio() {
        return ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public FinancialChartData(int selectImageId, String type, float ratio, float totalMoney) {
        this.selectImageId = selectImageId;
        this.type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }
}