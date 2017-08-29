package com.envisability.catchi.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.envisability.catchi.R;

/**
 * Created by Тарас on 02.06.2017.
 */
public class CatchiTextView extends TextView {
    private static final String TAG = "CatchiTextView";

    public CatchiTextView(Context context) {
        super(context);
    }

    public CatchiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        checkSpacing();
        setCustomFont(context, attrs);
    }


    public CatchiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        checkSpacing();
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        Log.d(TAG,"is set custom");
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CatchiTextView);
        Log.d(TAG,"is set custom 1");
        if(a==null)
            return;
        String customFont = a.getString(R.styleable.CatchiTextView_customFont);
        Log.d(TAG,"is set custom 2");
        setCustomFont(ctx, customFont);
        Log.d(TAG,"is set custom 3");
        a.recycle();
    }



    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.d(TAG,"is set custom false");
            return false;
        }

        setTypeface(tf);
        return true;
    }

    @TargetApi(21)
    public void checkSpacing()
    {
        if (android.os.Build.VERSION.SDK_INT >= 21 && (getTypeface().getStyle() == R.style.InitialsBarney || getTypeface().getStyle() == R.style.InitialsPurple
                || getTypeface().getStyle() == R.style.ChatStyle|| getTypeface().getStyle() == R.style.InitialsBlue)) {
            setLetterSpacing(-0.05f);


        }
    }
}
