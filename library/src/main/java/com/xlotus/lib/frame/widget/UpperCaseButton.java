package com.xlotus.lib.frame.widget;

import android.content.Context;
import android.util.AttributeSet;

public class UpperCaseButton extends androidx.appcompat.widget.AppCompatButton {
    public UpperCaseButton(Context context) {
        super(context);
    }

    public UpperCaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpperCaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        text = text.toString().toUpperCase();
        super.setText(text, type);
    }
}
