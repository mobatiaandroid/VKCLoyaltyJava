package com.vkc.loyaltyapp.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.vkc.loyaltyapp.R;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditTextBlack  extends AppCompatEditText {

    public CustomEditTextBlack(Context context) {
        super(context);
        setFont();
    }

    public CustomEditTextBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomEditTextBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.white));
    }


}
