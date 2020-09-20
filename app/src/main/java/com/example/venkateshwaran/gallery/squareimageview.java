package com.example.venkateshwaran.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class squareimageview extends ImageView
{
    public squareimageview(Context context) {
        super(context);
    }

    public squareimageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public squareimageview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth());
    }
}


