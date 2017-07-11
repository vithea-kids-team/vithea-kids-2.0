package com.l2f.vitheakids.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * Created by Claudia on 29/04/2017.
 */

public final class CanvasUtil {

    @SuppressWarnings("deprecation")
    public static Integer getHeight(Display display) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return display.getHeight();
        } else {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
    }

    public static int convertToDP(float value, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
    }
}
