package com.extra.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.extra.util.LanguageUtils;


/**
 * Created by ALL IN ONE on 7/12/2017.
 */

public class IranSansAutoCompeleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public IranSansAutoCompeleteTextView(Context context) {
        super(context);
        setFont();
    }

    private void setFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans.ttf");
        setTypeface(tf);
    }

    public IranSansAutoCompeleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public void setPersianText(String text) {
        super.setText(LanguageUtils.getPersianNumbers(text));
    }

    public IranSansAutoCompeleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }
}
