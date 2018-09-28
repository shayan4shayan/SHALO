package com.extra.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;

import com.base.R;
import com.extra.util.UIUtils;


/**
 * Created by shayan on 7/4/2017.
 */

public class PinEditText extends IranSansEditText {

    float mSpace = 12; //24 dp by default
    float mCharSize = 24;
    float mNumChars = 6;
    float mLineSpacing = 8; //8dp by default

    private OnClickListener mClickListener;

    public PinEditText(Context context) {
        super(context);
    }

    public PinEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attributeSet) {
        setBackgroundResource(0);
        float multi = context.getResources().getDisplayMetrics().density;
        mSpace = multi * mSpace; //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing; //convert to pixels

        //When tapped, move cursor to end of the text
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.PinEditText,
                0, 0);

        try {
            mNumChars = a.getInt(R.styleable.PinEditText_pinSize,6);
            mSpace = a.getDimensionPixelSize(R.styleable.PinEditText_gapWidth,12);
            int width = getPaddingLeft() + getPaddingRight();
//            int space = (int) UIUtils.convertDpToPixel(mSpace,context);
            int charSize = (int) UIUtils.convertDpToPixel(mCharSize,context);
            width += mSpace*(mNumChars-1);
            width +=charSize*mNumChars;
            setWidth(width);
        } finally {
            a.recycle();
        }

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = getPaint();
        paint.setColor(Color.WHITE);


        int availableWidth =
                getWidth() - getPaddingRight() - getPaddingLeft();
        if (mSpace < 0) {
            mCharSize = (availableWidth / (length() * 2 - 1));
        } else {
            mCharSize =
                    (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        for (int i = 0; i < mNumChars; i++) {
            if (i < getText().length()) {
                paint.setAlpha(1000);
                canvas.drawLine(
                        startX, bottom, startX + mCharSize, bottom, paint);
                canvas.drawLine(startX, bottom - 1, startX + mCharSize, bottom - 1, paint);
                canvas.drawLine(startX, bottom - 2, startX + mCharSize, bottom - 2, paint);
            } else {
                paint.setAlpha(100);
                canvas.drawLine(startX, bottom, startX + mCharSize, bottom, paint);
                canvas.drawLine(startX, bottom - 1, startX + mCharSize, bottom - 1, paint);
                canvas.drawLine(startX, bottom - 2, startX + mCharSize, bottom - 2, paint);
            }


            Editable text = getText();
            int textLength = text.length();
            float[] textWidths = new float[textLength];
            getPaint().getTextWidths(getText(), 0, textLength, textWidths);
            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text,
                        i,
                        i + 1,
                        middle - textWidths[0] / 2,
                        bottom - mLineSpacing,
                        paint);
            }

            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }
}
