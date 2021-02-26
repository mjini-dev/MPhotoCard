package com.example.mphotocard;

import android.graphics.Typeface;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FontData implements Serializable {

    private Typeface typeface;

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public FontData(Typeface typeface) {
        this.typeface = typeface;
    }
}
