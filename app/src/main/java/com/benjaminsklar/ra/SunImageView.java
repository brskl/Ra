package com.benjaminsklar.ra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ben on 3/5/2017.
 */

public class SunImageView extends ImageView {

    private int iValue = 0;

    public SunImageView(Context context) {
        super(context);
    }

    public SunImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SunImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getiValue() {
        return iValue;
    }

    public void setiValue(int iValue) {
        if (this.iValue != iValue) {
            if (getVisibility() == View.VISIBLE) {
                invalidate();
            }
        }
        this.iValue = iValue;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(30); // TODO: make size dependent on layout attribute
            // add getHeight() / 10 to offset text slightly to better fit image
            canvas.drawText(Integer.toString(iValue), getWidth() / 2, getHeight() / 2 + getHeight() / 10, paint);
       }
}