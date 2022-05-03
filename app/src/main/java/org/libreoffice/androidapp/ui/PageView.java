package org.libreoffice.androidapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import org.libreoffice.androidapp.debug.R;

/* loaded from: classes.dex */
public class PageView extends View {
    private Paint mPaintBlack;
    private String tag = "PageView";
    private Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_page);

    public PageView(Context context) {
        super(context);
        intialise();
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(this.tag, this.bmp.toString());
        intialise();
    }

    public PageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        intialise();
    }

    private void intialise() {
        this.mPaintBlack = new Paint();
        this.mPaintBlack.setARGB(255, 0, 0, 0);
        Log.d(this.tag, " Doing some set-up");
    }

    public void setBitmap(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(this.tag, "Draw");
        Log.d(this.tag, Integer.toString(this.bmp.getHeight()));
        if (this.bmp != null) {
            int horizontalMargin = (int) (canvas.getWidth() * 0.1d);
            Bitmap bitmap = this.bmp;
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), this.bmp.getHeight()), new Rect(horizontalMargin, horizontalMargin, canvas.getWidth() - horizontalMargin, canvas.getHeight() - horizontalMargin), this.mPaintBlack);
        }
        if (this.bmp == null) {
            canvas.drawText(getContext().getString(R.string.bmp_null), 100.0f, 100.0f, new Paint());
        }
    }
}
