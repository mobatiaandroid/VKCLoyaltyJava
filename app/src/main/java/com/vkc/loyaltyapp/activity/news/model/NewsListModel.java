package com.vkc.loyaltyapp.activity.news.model;

import java.util.ArrayList;

public class NewsListModel {

    private String response;
    private String responsecode;
    private String id;

    private String state;

    private String message;

    private String endDate;

    private ArrayList<ProductModel> productDetails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<ProductModel> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ArrayList<ProductModel> productDetails) {
        this.productDetails = productDetails;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }


}