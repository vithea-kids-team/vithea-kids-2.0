package com.l2f.vitheakids.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.l2f.vitheakids.model.SelectionArea;

/**
 * Created by silvi on 05/04/2018.
 */

public class ImageViewTouch extends android.support.v7.widget.AppCompatImageView{
    SelectionArea selectionArea ;


    public ImageViewTouch(Context context, SelectionArea selectionArea) {
        super(context);
        this.selectionArea = selectionArea;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(selectionArea.getStartX(),selectionArea.getStartY(),selectionArea.getStartX()+selectionArea.getEndX(),selectionArea.getEndY()+selectionArea.getStartY(),paint);

    }

}
