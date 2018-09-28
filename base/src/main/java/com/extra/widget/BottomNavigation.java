package com.extra.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.R;
import com.extra.util.UIUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ALL IN ONE on 7/29/2017.
 */

public class BottomNavigation extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {

    float height;
    float width;

    float iWidth;
    float gapWidth;

    float iconPadding;

    float circleY;

    ViewPager pager;

    @ColorRes
    int selectedItemColor = R.color.colorSecondary;
    @ColorRes
    int itemColor = R.color.black;

    Paint paint;
    private boolean isClickListenerSet = false;
    private int position;
    private float positionOffset;

    List<Fragment> fragments = new ArrayList<>();

    private String TAG = getClass().getSimpleName();

    public BottomNavigation(Context context) {
        super(context);
        init(null);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        width = getWidth() - getPaddingLeft() - getPaddingRight();
        height = getHeight() - getPaddingTop() - getPaddingBottom();

        iWidth = UIUtils.convertDpToPixel(26, getContext());
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        pager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) getChildAt(i);
            if (getChildAt(i) == v) {
                pager.setCurrentItem(i);
                DrawableHelper.withContext(getContext())
                        .withColor(selectedItemColor)
                        .withDrawable(((ImageView) ll.getChildAt(0)).getDrawable())
                        .tint()
                        .applyTo((ImageView) ll.getChildAt(0));
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
            } else {
                DrawableHelper.withContext(getContext())
                        .withColor(itemColor)
                        .withDrawable(((ImageView) ll.getChildAt(0)).getDrawable())
                        .tint()
                        .applyTo((ImageView) ll.getChildAt(0));
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position = position;
        this.positionOffset = positionOffset;
        if (positionOffset >= 0.5) {
            select(position + 1);
        } else {
            select(position);
        }
        invalidate();
    }

    private void select(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) getChildAt(i);
            if (position == i) {
                DrawableHelper.withContext(getContext())
                        .withColor(selectedItemColor)
                        .withDrawable(((ImageView) ll.getChildAt(0)).getDrawable())
                        .tint()
                        .applyTo((ImageView) ll.getChildAt(0));
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
            } else {
                DrawableHelper.withContext(getContext())
                        .withColor(itemColor)
                        .withDrawable(((ImageView) ll.getChildAt(0)).getDrawable())
                        .tint()
                        .applyTo((ImageView) ll.getChildAt(0));
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void setCurrentItem(int i) {
        pager.setCurrentItem(i);
        position = i;
        positionOffset = 0;
        select(i);
    }

    public void addBottomNavigationItem(BottomNavigationItem item) {
        if (item == null) {
            throw new NullPointerException("BottomNavigationItem cant be null");
        }
        if ((item.getText() == null || item.getText().isEmpty()) && (item.getDrawable() == null && item.getDrawableRes() == 0)) {
            throw new RuntimeException("name and Drawable cannot be null or empty");
        }
        if (item.getText() == null || item.getText().isEmpty()) {
            Log.d(TAG, "itemText is empty : no name will be displayed");
        }
        if (item.getDrawableRes() != 0) {
            item.drawable = ResourcesCompat.getDrawable(getResources(), item.getDrawableRes(), null);
        }

        fragments.add(item.getFragment());
        addView(item);

    }

    private void addView(BottomNavigationItem item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_navigation, this, false);
        ImageView imageView = view.findViewById(R.id.image);
        if (item.getDrawable() == null) {
            imageView.setImageResource(item.getDrawableRes());
        } else {
            imageView.setImageDrawable(item.getDrawable());
        }
        TextView textView = view.findViewById(R.id.text);
        textView.setText(item.getText());
        view.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void addView(View child) {
        child.setOnClickListener(this);
        super.addView(child);
    }

    public class BottomNavigationItem {
        String text;
        Drawable drawable;
        private int drawableRes;
        Fragment fragment;

        public BottomNavigationItem(String text, Drawable drawable, Fragment fragment) {
            this.text = text;
            this.drawable = drawable;
            this.fragment = fragment;
        }

        public BottomNavigationItem(String text, @DrawableRes int drawableRes, Fragment fragment) {
            this.text = text;
            this.drawableRes = drawableRes;
            this.fragment = fragment;
        }


        public Drawable getDrawable() {
            return drawable;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public int getDrawableRes() {
            return drawableRes;
        }

        public String getText() {
            return text;
        }

    }

}