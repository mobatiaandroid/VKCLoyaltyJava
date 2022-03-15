/**
 *
 */
package com.vkc.loyaltyapp.utils;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vkc.loyaltyapp.R;

/**
 * @author mobatia-user
 */
public class CustomToast {

    Activity mActivity;
    TextView mTextView;
    Toast mToast;

    public CustomToast(Activity mActivity) {
        this.mActivity = mActivity;
        init();

    }

    public void init() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, null);
        mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

        mToast = new Toast(mActivity);
        mToast.setView(layouttoast);
    }

    public void show(int errorCode) {
        if (errorCode == 0) {
            mTextView.setText(mActivity.getResources().getString(
                    R.string.common_error));
        }
        if (errorCode == 1) {
            mTextView.setText("Successfully logged in.");
        }
        if (errorCode == 2) {
            mTextView.setText("Login failed.Please try again later");
        }
        if (errorCode == 3) {
            mTextView.setText("Successfully submitted login request");
        }
        if (errorCode == 4) {
            mTextView.setText("Invalid user type");
        }
        if (errorCode == 5) {
            mTextView.setText("No results found");
        }
        if (errorCode == 6) {
            mTextView.setText("Successfully added to cart");
        }
        if (errorCode == 7) {
            mTextView.setText("Already redeemed this gift");
        }
        if (errorCode == 8) {
            mTextView.setText("OTP Verification Successful");
        }
        if (errorCode == 9) {
            mTextView.setText("Incorrect OTP");
        }
        if (errorCode == 10) {
            mTextView.setText("Please select dealers");
        }
        if (errorCode == 11) {
            mTextView.setText("Cannot assign more than 10 Dealers");
        }
        if (errorCode == 12) {
            mTextView.setText("Dealers added successfully");
        }
        if (errorCode == 13) {
            mTextView.setText("No record found");
        }
        if (errorCode == 14) {
            mTextView.setText("Please select a retailer");
        }
        if (errorCode == 15) {
            mTextView.setText("Please enter coupon value");
        }
        if (errorCode == 16) {
            mTextView.setText("Coupon value should not be greater than credit value");
        }
        if (errorCode == 17) {
            mTextView.setText("Please enter coupon value to issue");
        }
        if (errorCode == 18) {
            mTextView.setText("Coupon issued successfully");
        }
        if (errorCode == 19) {
            mTextView.setText("Image uploaded successfully");
        }
        if (errorCode == 20) {
            mTextView.setText("You have exceeded the image upload limit for this week");
        }
        if (errorCode == 21) {
            mTextView.setText("Please capture an image to upload");
        }
        if (errorCode == 22) {
            mTextView.setText("Insufficient coupon balance to redeem the gift");
        }
        if (errorCode == 23) {
            mTextView.setText("This feature is only available for retailers");
        }
        if (errorCode == 24) {
            mTextView.setText("Failed.Try again later");
        }
        if (errorCode == 25) {
            mTextView.setText("Please select a distributor");
        }
        if (errorCode == 26) {
            mTextView.setText("Profile updated successfully");
        }
        if (errorCode == 27) {
            mTextView.setText("Profile updation failed");
        }
        if (errorCode == 28) {
            mTextView.setText("Your registration with VKC Loyalty is on hold.Please login after verification");
        }
        if (errorCode == 29) {
            mTextView.setText("Cannot login using multiple devices. Please contact VKC");
        }
        if (errorCode == 30) {
            mTextView.setText("Mobile number updated successfully. Please login using new mobile number");
        }
        if (errorCode == 31) {
            mTextView.setText("This feature is currently not available.");
        }
        if (errorCode == 32) {
            mTextView.setText("Please select state.");
        }
        if (errorCode == 33) {
            mTextView.setText("Please select district.");
        }
        if (errorCode == 34) {
            mTextView.setText("OTP sent successfully.");
        }
        if (errorCode == 35) {
            mTextView.setText("Already registered with scheme");
        }
        if (errorCode == 36) {
            mTextView.setText("Please enter search key");
        }
        if (errorCode == 37) {
            mTextView.setText("Please agree terms and conditions to continue.");
        }
        if (errorCode == 38) {
            mTextView.setText("Do not have enough coupons to add to cart");
        }
        if (errorCode == 39) {
            mTextView.setText("Add to cart failed");
        }
        if (errorCode == 40) {
            mTextView.setText("Please enter quantity value");
        }
        if (errorCode == 41) {
            mTextView.setText("Please select a voucher");
        }
        if (errorCode == 42) {
            mTextView.setText("Please enter the quantity value");
        }
        if (errorCode == 43) {
            mTextView.setText("No items in cart");
        }
        if (errorCode == 44) {
            mTextView.setText("No dealers found");
        }
        if (errorCode == 45) {
            mTextView.setText("Please select dealer");
        }
        if (errorCode == 46) {
            mTextView.setText("Not enough data to place order");
        }
        if (errorCode == 47) {
            mTextView.setText("Order Placed Successfully");
        }
        if (errorCode == 48) {
            mTextView.setText("Unable to place order. Try again");
        }
        if (errorCode == 49) {
            mTextView.setText("Unable to delete data. Try again");
        }
        if (errorCode == 50) {
            mTextView.setText("No messages found");
        }
        if (errorCode == 51) {
            mTextView.setText("No images uploaded this week");
        }
        if (errorCode == 52) {
            mTextView.setText("Image Deleted Successfully");
        }
        if (errorCode == 53) {
            mTextView.setText("Please enter mobile number");
        }
        if (errorCode == 54) {
            mTextView.setText("Invalid mobile number");
        }

        if (errorCode == 55) {
            mTextView.setText("Mobile number updated successfully. Please login again.");
        }
        if (errorCode == 56) {
            mTextView.setText("Updation Failed.");
        }
        if (errorCode == 57) {
            mTextView.setText("Please select user type");
        }
        if (errorCode == 58) {
            mTextView.setText("No internet connectivity");
        }
        if (errorCode == 59) {
            mTextView.setText("Please enter a valid quantity");
        }
        if (errorCode == 60) {
            mTextView.setText("Quantity cannot be 0");
        }
        if (errorCode == 61) {
            mTextView.setText("Unable to issue coupons,since there is no scheme running in your state");

        }
        if (errorCode == 62) {
            mTextView.setText("Unable to fetch cart count,since there is no scheme running in your state");

        }
        if (errorCode == 63) {
            mTextView.setText("Unable to update cart,since there is no scheme running in your state");

        }
        if (errorCode == 64) {
            mTextView.setText("Unable to add to cart,since there is no scheme running in your state");

        }
        if (errorCode == 65) {
            mTextView.setText("Please select retailer");

        }
        if (errorCode == 66) {
            mTextView.setText("No retailer redeem data found");

        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    /*
     * CustomToast toast = new CustomToast(mActivity); toast.show(18);
     */
}
