package com.extra.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.base.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ALL IN ONE on 7/12/2017.
 */

public class CountDownView extends android.support.v7.widget.AppCompatTextView {
    int min;
    int second;

    Timer timer;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            CountDownView.this.run();
        }
    };
    private OnCountDownListener countDownListener;

    public CountDownView(Context context) {
        super(context);
        init(null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setText(getSecond() + " : " + getMin());
        super.onDraw(canvas);


    }

    private String getMin() {
        if (min < 10) return "0" + min;
        return "" + min;
    }

    private String getSecond() {
        if (second < 10) return "0" + second;
        return "" + second;
    }

    public void init(AttributeSet attributeSet) {
        timer = new Timer();
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.CountDownView,
                0, 0);
        int time = array.getInt(R.styleable.CountDownView_timeInSeconds, 59);
        setTime(time);
    }

    public void start() {
        stop();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                CountDownView.this.run();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        countDownListener.onCounterStart();
    }

    public void stop() {
        timer.cancel();
        timer.purge();
    }


    public void run() {
        if (second > 0) {
            second--;
            return;
        }
        if (second == 0 && min > 0) {
            min--;
            second = 59;
            return;
        }
        if (second == 0 && min == 0) {
            second = 0;
            min = 0;
            countDownListener.onReachZero();
            stop();
        }
    }

    public void setTime(int time) {
        min = time / 60;
        second = time % 60;
    }

    public void setCountDownListener(OnCountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public interface OnCountDownListener {
        void onReachZero();

        void onCounterStart();
    }
}
