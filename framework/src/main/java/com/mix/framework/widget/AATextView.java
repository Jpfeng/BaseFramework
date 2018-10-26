package com.mix.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.mix.framework.R;

/**
 * 默认开启抗锯齿的 TextView
 * Author ltt
 * Email: litt@mixotc.com
 * Date:  2018/5/10.
 */
public class AATextView extends androidx.appcompat.widget.AppCompatTextView {

    public AATextView(Context context) {
        this(context, null);
    }

    public AATextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.AATextViewStyle);
    }

    public AATextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.AATextView, defStyleAttr, R.style.DefaultAATextViewStyle);
        boolean aa = ta.getBoolean(R.styleable.AATextView_aatv_anti_alias, true);
        boolean underline = ta.getBoolean(R.styleable.AATextView_aatv_underline, false);
        ta.recycle();
        setAntiAlias(aa);
        setUnderline(underline);
    }

    public void setAntiAlias(boolean aa) {
        getPaint().setAntiAlias(aa);
    }

    public void setUnderline(boolean underline) {
        getPaint().setFlags(underline
                ? getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
                : getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }
}
