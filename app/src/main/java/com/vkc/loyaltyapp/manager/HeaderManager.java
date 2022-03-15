package com.vkc.loyaltyapp.manager;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;

import java.io.Serializable;

/**
 * Created by gayatri on 18/4/17.
 */
public class HeaderManager implements Serializable {

    /**
     * The context.
     */
    private AppCompatActivity context;

    /**
     * The inflator.
     */
    private LayoutInflater inflator;

    /**
     * The header view.
     */
    public View headerView;

    /**
     * The m heading1.
     */
    private TextView mHeading1;

    /**
     * The relative params.
     */
    private RelativeLayout.LayoutParams relativeParams;

    /**
     * The heading1.
     */
    private String heading1;

    /**
     * The is cancel.
     */
    private boolean isCancel = false;

    private EditText edtText;
    /* FOR HOME SCREEN */
    /**
     * The is home.
     */
    private boolean isHome = false;

    /**
     * The m right text.
     */
    private TextView mLeftText, mRightText;

    /**
     * The m left.
     */
    private ImageView mLeftImage, mRightImage, mRight, mLeft;

    /**
     * Instantiates a new headermanager.
     *
     * @param context  the context
     * @param heading1 the heading1
     */

    public HeaderManager(AppCompatActivity context, String heading1) {
        this.setContext(context);

        inflator = LayoutInflater.from(context);
        this.heading1 = heading1;

    }

	/*
     * public Headermanager(Activity context,String heading1) {
	 * this.setContext(context); inflator = LayoutInflater.from(context);
	 * this.heading1=heading1; this.isCancel=isCancel; }
	 */

    /**
     * Instantiates a new headermanager.
     *
     * @param home    the home
     * @param context the context
     */
    public HeaderManager(boolean home, AppCompatActivity context) {
        this.setContext(context);
        inflator = LayoutInflater.from(context);
        this.isHome = home;
    }

    /**
     * Gets the left text.
     *
     * @return the left text
     */
    public TextView getLeftText() {
        return mLeftText;
    }

    /**
     * Sets the left text.
     *
     * @param mLeftText the new left text
     */
    public void setLeftText(TextView mLeftText) {
        this.mLeftText = mLeftText;
    }

    /**
     * Gets the right text.
     *
     * @return the right text
     */
    public TextView getRightText() {
        return mRightText;
    }

    /**
     * Sets the right text.
     *
     * @param mLeftText the new right text
     */
    public void setRightText(TextView mLeftText) {
        this.mRightText = mLeftText;
    }

    // image view

    /**
     * Gets the left image.
     *
     * @return the left image
     */
    public ImageView getLeftImage() {
        return mLeftImage;
    }

    /**
     * Sets the left image.
     *
     * @param mLeftImage the new left image
     */
    public void setLeftImage(ImageView mLeftImage) {
        this.mLeftImage = mLeftImage;
    }

    /**
     * Gets the right image.
     *
     * @return the right image
     */
    public ImageView getRightImage() {
        return mLeftImage;
    }

    /**
     * Sets the right image.
     *
     * @param mLeftImage the new right image
     */
    public void setRightImage(ImageView mLeftImage) {
        this.mRightImage = mLeftImage;
    }

    /**
     * Sets the visible.
     *
     * @param v the new visible
     */
    public void setVisible(View v) {
        v.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the invisible.
     */
    public void setInvisible() {
        headerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets the invisible.
     *
     * @param v the new invisible
     */
    public void setInvisible(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    /**
     * Gets the header.
     *
     * @param headerHolder the header holder
     * @return the header
     */
    public int getHeader(LinearLayout headerHolder, int type) {
        initializeUI(type);
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        headerHolder.addView(headerView, relativeParams);
        return headerView.getId();
    }

    /**
     * Gets the header.
     *
     * @param headerHolder the header holder
     * @return the header
     */
    public int getHeader(LinearLayout headerHolder, boolean getHeading,
                         int type) {
        initializeUI(getHeading, type);
        relativeParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(LinearLayout.VERTICAL);
        headerHolder.addView(headerView, relativeParams);
        return headerView.getId();
    }

    /**
     * Initialize ui.
     */
    private void initializeUI(int type) {
        inflator = LayoutInflater.from(getContext());
        // System.out.println("htype" + type);
        headerView = inflator.inflate(R.layout.common_header_single, null);
        mHeading1 = (TextView) headerView.findViewById(R.id.heading);
        mHeading1.setText(heading1);
        // mHeading1.setTextColor(IAPIThemesVariables.RGB_VALUE_TITLETEXT);
        // SetTheme.setTextColor(mHeading1);

        mRight = (ImageView) headerView.findViewById(R.id.btn_right);
        mLeft = (ImageView) headerView.findViewById(R.id.btn_left);
//        RelativeLayout logoHeader = ((RelativeLayout) headerView
//                .findViewById(R.id.relative_logo_header));
//        View view = ((View) headerView.findViewById(R.id.view));

//		SetTheme.setBaseTitle(headerViewr, context);
        if (type == 0) {
//          logoHeader.setBackgroundResource(R.drawable.titilebar);// two
            //logoHeader.setBackgroundResource(R.color.login_button_bg);// two
            mRight.setVisibility(View.INVISIBLE);
            mLeft.setVisibility(View.INVISIBLE);
            // buttons
        } else if (type == 1) {
//          logoHeader.setBackgroundResource(R.drawable.titilebar);// left
            mRight.setVisibility(View.INVISIBLE);
            mLeft.setVisibility(View.VISIBLE);
            //logoHeader.setBackgroundResource(R.color.login_button_bg);
            // button
        } else if (type == 2) {
//            logoHeader.setBackgroundResource(R.drawable.titilebar);// left
            mRight.setVisibility(View.VISIBLE);
            mLeft.setVisibility(View.INVISIBLE);
        } else if (type == 3) {
            mRight.setVisibility(View.VISIBLE);
            mLeft.setVisibility(View.VISIBLE);
        }
//		view.setBackgroundResource(R.drawable.line);

//		edtText = (EditText) headerView.findViewById(R.id.edtTxt_Header);
    }

    /**
     * Initialize ui.
     */
    private void initializeUI(boolean getHeading, int type) {
        inflator = LayoutInflater.from(getContext());

        headerView = inflator.inflate(R.layout.common_header_single, null);

//        RelativeLayout logoHeader = ((RelativeLayout) headerView
//                .findViewById(R.id.relative_logo_header));
//        if (type == 0) {
//           logoHeader.setBackgroundResource(R.drawable.titilebar);
//            //logoHeader.setBackgroundResource(R.color.login_button_bg);
//        } else if (type == 1) {
//          logoHeader.setBackgroundResource(R.drawable.titilebar);
//            //logoHeader.setBackgroundResource(R.color.login_button_bg);
//            mHeading1 = (TextView) headerView.findViewById(R.id.heading);
//            mHeading1.setVisibility(View.GONE);
//
//        }
//        else if(type==2) {
//         logoHeader.setBackgroundResource(R.drawable.titilebar);
//            //logoHeader.setBackgroundResource(R.color.login_button_bg);
//            }


//		edtText = (EditText) headerView.findViewById(R.id.edtTxt_Header);
        mHeading1 = (TextView) headerView.findViewById(R.id.heading);
        mHeading1.setText(heading1);
        mRight = (ImageView) headerView.findViewById(R.id.btn_right);
        mLeft = (ImageView) headerView.findViewById(R.id.btn_left);
        if (type == 0) {
            mRight.setVisibility(View.GONE);
            mLeft.setVisibility(View.GONE);
        } else if (type == 1) {
            mRight.setVisibility(View.GONE);
            mLeft.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            mRight.setVisibility(View.VISIBLE);
            mLeft.setVisibility(View.GONE);
        } else if (type == 3) {
            mRight.setVisibility(View.VISIBLE);
            mLeft.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the title bar.
     *
     * @param titleBar the new title bar
     */
    public void setTitleBar(int titleBar) {
        this.headerView.setBackgroundResource(titleBar);
    }

    public void setTitle(String title) {
        mHeading1.setText(title);
    }

    /**
     * Gets the left button.
     *
     * @return the left button
     */
    public ImageView getLeftButton() {
        return mLeft;
    }

    /**
     * Sets the left button.
     *
     * @param right the new left button
     */
    public void setLeftButton(ImageView right) {
        this.mLeft = right;
    }

    /**
     * Gets the right button.
     *
     * @return the right button
     */
    public ImageView getRightButton() {
        return mRight;
    }

    public EditText getEditText() {
        mHeading1.setVisibility(View.GONE);
        edtText.setVisibility(View.VISIBLE);
        return edtText;
    }

    /**
     * Sets the edits the text.
     *
     * @param editText the new edits the text
     */
    public void setEditText(EditText editText) {
        this.edtText = editText;
        setVisible(edtText);
    }

    /**
     * Sets the right button.
     *
     * @param right the new right button
     */
    public void setRightButton(ImageView right) {
        this.mRight = right;
    }

    /**
     * Sets the button right selector.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     */
    public void setButtonRightSelector(int normalStateResID,
                                       int pressedStateResID) {
        mRight.setImageDrawable(getButtonDrawableByScreenCathegory(
                normalStateResID, pressedStateResID));
        setVisible(mRight);
    }

    /**
     * Sets the button left selector.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     */
    public void setButtonLeftSelector(int normalStateResID,
                                      int pressedStateResID) {
        mLeft.setImageDrawable(getButtonDrawableByScreenCathegory(
                normalStateResID, pressedStateResID));
        setVisible(mLeft);
    }

    /**
     * Gets the button drawable by screen cathegory.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     * @return the button drawable by screen cathegory
     */
    public Drawable getButtonDrawableByScreenCathegory(int normalStateResID,
                                                       int pressedStateResID) {

        Drawable state_normal = context.getResources()
                .getDrawable(normalStateResID).mutate();

        Drawable state_pressed = context.getResources()
                .getDrawable(pressedStateResID).mutate();

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_pressed},
                state_pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal);

        return drawable;
    }

    // public void setCancelButton()
    // {
    // mRight.setBackgroundResource(R.drawable.close_button_selector);
    // setVisible(mRight);
    // }

    /**
     * Sets the context.
     *
     * @param context the new context
     */
    public void setContext(AppCompatActivity context) {
        this.context = context;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public AppCompatActivity getContext() {
        return context;
    }

}
