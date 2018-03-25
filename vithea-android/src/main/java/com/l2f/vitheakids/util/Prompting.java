package com.l2f.vitheakids.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.l2f.vitheakids.R;
import com.l2f.vitheakids.VitheaKidsActivity;
import com.l2f.vitheakids.task.ReadTask;

import org.springframework.core.io.Resource;

import java.util.List;

/**
 * Created by silvi on 09/10/2017.
 */

public class Prompting {
    public static void setButtonColor(Context context, Button view){

        Log.d("pintei o botão", Integer.toString(view.getId()));

        view.setBackgroundDrawable(makeSelector(context, Color.parseColor("#66a3ff")));
    }

    public static void setSizeText(Context context, Button view){
        view.setTextSize(20);
    }

    public static void scratchText(Context context, View view){
        ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Button) view).setTextColor((Color.parseColor("#FF0000")));
        } else {
            ((Button) view).setTextColor((Color.parseColor("#FF0000")));
        }
    }

    public static void  readAnswers(List<String> currentAnswers){
        String answers="";
        for(int i = 0; i < currentAnswers.size(); i++){
            answers += currentAnswers.get(i)+ ".";
            new ReadTask().execute(answers);
        }
    }

    public static void setImageColor(Context context, ImageView view){
        FrameLayout fr = (FrameLayout) view.getParent();
        fr.setBackground(context.getResources().getDrawable(R.drawable.border));
    }

    public static void setImageSize(Context context, List<ImageView> views){
        for(ImageView view: views) {
            view.requestLayout();
            int currentHeight = view.getMeasuredHeight();
            int currentWidth = view.getMeasuredWidth();

            view.getLayoutParams().height = currentHeight - 50;
            view.getLayoutParams().width = currentWidth - 50;
        }
    }

    public static void scratchImage(Context context, ImageView view){
        FrameLayout fr = (FrameLayout) view.getParent();
        ImageView im  = (ImageView) fr.getChildAt(1);// getting imageView that contais cruz.jpg
        im.setVisibility(View.VISIBLE);
    }
    public static void resetScratchImage(Context context, ImageView view){
        FrameLayout fr = (FrameLayout) view.getParent();
        ImageView im  = (ImageView) fr.getChildAt(1);// getting imageView that contais cruz.jpg
        im.setVisibility(View.INVISIBLE);
    }


    /**
     * Generates a new drawable with a new color
     * @param color
     * @return StateListDrawable
     */
    public static StateListDrawable makeSelector(Context context, int color) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable buttonSelected = context.getResources().getDrawable(R.drawable.buttonselected);
        GradientDrawable buttonNormal = (GradientDrawable)context.getResources().getDrawable(R.drawable.bottonnormal);
        buttonNormal.setColor(color);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, buttonSelected );
        stateListDrawable.addState(new int[]{}, buttonNormal);
        return stateListDrawable;
    }


    public static void resetPrompting() {

    }
}
