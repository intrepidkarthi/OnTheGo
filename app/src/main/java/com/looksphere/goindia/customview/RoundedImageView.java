package com.looksphere.goindia.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.looksphere.goindia.R;
import com.looksphere.goindia.application.SwachhApplication;


/**
 * Created by Karthi on 4/13/2014.
 */
public class RoundedImageView extends ImageView {

    private boolean hasBorder = false;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
        hasBorder = array.getBoolean(R.styleable.RoundedImageView_hasBorder, false);
    }

    public void setHasBorder(boolean border) {
        hasBorder = border;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b =  ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap;

        boolean isDefaultImage = false;
        if(b == null) {
            int id = getResources().getIdentifier("ic_launcher", "drawable", SwachhApplication.getContext().getPackageName());
            bitmap = BitmapFactory.decodeResource(SwachhApplication.getContext().getResources(), id);
            isDefaultImage = true;
        }
        else {
            bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        }

        int w = getWidth();
        int h = getHeight();

        float borderWidth = 0;
        if(hasBorder) {
            borderWidth = 2;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#99ffffff"));
            canvas.drawCircle(w/2, h/2, w/2, paint);
            w -= 2*borderWidth;
        }

        Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, borderWidth, borderWidth, null);

        roundBitmap.recycle();
        if(!isDefaultImage)
            bitmap.recycle();
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        boolean isScaled = false;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
            isScaled = true;
        }
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        if(isScaled)
            sbmp.recycle();

        return output;
    }
}