package com.vkc.loyaltyapp.activity.pointhistory.model;

import java.util.ArrayList;

public class TransactionModel {

    private ArrayList<HistoryModel> listHistory;
    private String userName;
    private String totPoints;

    public ArrayList<HistoryModel> getListHistory() {
        return listHistory;
    }

    public void setListHistory(ArrayList<HistoryModel> listHistory) {
        this.listHistory = listHistory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTotPoints() {
        return totPoints;
    }

    public void setTotPoints(String totPoints) {
        this.totPoints = totPoints;
    }
}
