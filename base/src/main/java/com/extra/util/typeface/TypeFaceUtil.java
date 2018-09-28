package com.extra.util.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;

/**
 * Created by shayan on 7/2/2017.
 */

public class TypeFaceUtil {
    public static SpannableString setTypeFace(Context context, String str) {
        return setTypeFace(context, str, 18f);
    }

    public static SpannableString setTypeFace(Context context, String str, float i) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/iran_sans.ttf");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new TypefaceSpan(context, tf, i)
                , 0, str.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
