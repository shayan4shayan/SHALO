package com.extra.util.typeface;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by shayan on 7/2/2017.
 */

public class TypefaceSpan extends MetricAffectingSpan {

    private Typeface mTypeface;
    private Context mContext;
    private float mTextSize;

    public TypefaceSpan(Context context, Typeface typeface, float textSize) {
        mContext = context;
        mTypeface = typeface;
        mTextSize = textSize;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        final float scale = mContext.getResources().getDisplayMetrics().density;

        int textSize = (int) (mTextSize * scale + 0.5f);
        p.setTypeface(mTypeface);
        p.setTextSize(textSize);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint p) {
        final float scale = mContext.getResources().getDisplayMetrics().density;

        int textSize = (int) (mTextSize * scale + 0.5f);
        p.setTypeface(mTypeface);
        p.setTextSize(textSize);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

}