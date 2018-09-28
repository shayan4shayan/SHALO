package com.extra.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.extra.util.LanguageUtils;

/**
 * Created by shayan on 6/30/2017.
 */

public class IranSansEditText extends android.support.v7.widget.AppCompatEditText {
    public IranSansEditText(Context context) {
        super(context);
        setFont();
    }

    public IranSansEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public IranSansEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public void setPersianText(String text) {
        super.setText(LanguageUtils.getPersianNumbers(text));
    }

    /**
     * set Type fade to view
     */
    void setFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans.ttf");
        setTypeface(tf);
    }
}
