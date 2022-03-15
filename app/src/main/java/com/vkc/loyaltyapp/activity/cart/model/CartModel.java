package com.vkc.loyaltyapp.activity.cart.model;

/**
 * Created by user2 on 1/11/17.
 */
public class CartModel {
    private String id;
    private String gift_id;
    private String gift_title;
    private String gift_image;
    private String point;
    private String quantity;
    private String gift_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_title() {
        return gift_title;
    }

    public void setGift_title(String gift_title) {
        this.gift_title = gift_title;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }
}
