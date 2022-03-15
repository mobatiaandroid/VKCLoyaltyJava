package com.vkc.loyaltyapp.activity.news.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkc.loyaltyapp.R;
import com.vkc.loyaltyapp.activity.news.model.ColorModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {
    private ArrayList<ColorModel> colorList;

    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageColor;
        TextView txtColorName;

        RecyclerView recyclerColor;

        public MyViewHolder(View view) {
            super(view);

           /* textDate = (TextView) view.findViewById(R.id.textDate);
            textType = (TextView) view.findViewById(R.id.textType);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textStatus = (TextView) view.findViewById(R.id.textStatus);*/

            txtColorName = (TextView) view.findViewById(R.id.txtColorName);
            imageColor = (ImageView) view.findViewById(R.id.imgColor);

        }
    }


    public ColorAdapter(Activity mActivity, ArrayList<ColorModel> colorList) {
        this.colorList = colorList;
        this.activity = mActivity;


    }

    public ColorAdapter(Activity mActivity) {

        this.activity = mActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String Color_value = colorList.get(i).getCode();
        if (Color_value.contains("#")) {
            int fillColor = Color.parseColor(Color_value);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(fillColor);
            myViewHolder.imageColor.setBackground(shape);
            myViewHolder.txtColorName.setText(colorList.get(i).getName());
        }
        // GradientDrawable shape = drawCircle(fillColor);
       /* GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(fillColor);*/


    }


    @Override
    public int getItemCount() {
        return colorList.size();
    }//return newsList.size();

    /*public static GradientDrawable drawCircle(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(backgroundColor);
        return shape;
    }*/
}