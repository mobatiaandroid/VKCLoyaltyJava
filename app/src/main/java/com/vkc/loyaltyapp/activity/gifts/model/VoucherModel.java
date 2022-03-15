package com.vkc.loyaltyapp.activity.gifts.model;

/**
 * Created by user2 on 31/10/17.
 */
public class VoucherModel {

    private String id;
    private String voucher_value;
    private String coupon_value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucher_value() {
        return voucher_value;
    }

    public void setVoucher_value(String voucher_value) {
        this.voucher_value = voucher_value;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }
}
