package com.looksphere.goindia.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.looksphere.goindia.R;

/**
 * Created by SPARK on 12/10/14.
 */
public class TickCheckBox extends CheckBox {
    public TickCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }

    @Override
    public void setChecked(boolean t) {
        if (t) {
            this.setBackgroundResource(R.drawable.tickmark);
        } else {
            this.setBackgroundResource(android.R.color.transparent);
        }
        super.setChecked(t);
    }
}