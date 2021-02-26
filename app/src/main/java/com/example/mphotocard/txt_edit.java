package com.example.mphotocard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class txt_edit extends AppCompatTextView {

    private boolean stroke = false; private float strokeWidth = 0.0f; private int strokeColor;


    public txt_edit(Context context) {
        super(context);
    }

    public txt_edit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public txt_edit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }



    private void initView(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.txt_edit);
        stroke = typeArray.getBoolean(R.styleable.txt_edit_textStroke, false);
        strokeWidth = typeArray.getFloat(R.styleable.txt_edit_textStrokeWidth, 0.0f);
        strokeColor = typeArray.getColor(R.styleable.txt_edit_textStrokeColor, 0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (stroke) {
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);

            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }

        super.onDraw(canvas);
    }





}
