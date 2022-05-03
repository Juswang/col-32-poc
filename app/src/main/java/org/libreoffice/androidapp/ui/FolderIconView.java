package org.libreoffice.androidapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.util.Stack;

/* loaded from: classes.dex */
public class FolderIconView extends View {
    private String LOGTAG = "FolderIconView";
    private File dir;
    private Paint mPaintBlack;
    private Paint mPaintGray;
    private Paint mPaintShadow;

    public FolderIconView(Context context) {
        super(context);
        initialisePaints();
    }

    public FolderIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialisePaints();
    }

    public FolderIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialisePaints();
    }

    private void initialisePaints() {
        this.mPaintBlack = new Paint();
        this.mPaintBlack.setColor(-12303292);
        this.mPaintBlack.setAntiAlias(true);
        this.mPaintGray = new Paint();
        this.mPaintGray.setColor(-7829368);
        this.mPaintGray.setAntiAlias(true);
        this.mPaintShadow = new Paint();
        this.mPaintShadow.setColor(Color.parseColor("#88888888"));
        this.mPaintShadow.setAntiAlias(true);
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        File[] contents;
        int size;
        int i;
        super.onDraw(canvas);
        Log.d(this.LOGTAG, "onDraw");
        float width = getWidth();
        float height = getHeight();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        float outerRadius = 0.4f * width;
        float innerRadius = 0.35f * width;
        float thumbHeight = 1.25f * outerRadius;
        float thumbWidth = ((float) (1.0d / Math.sqrt(2.0d))) * thumbHeight;
        float DZx = outerRadius * 0.2f;
        float DZy = 0.2f * outerRadius;
        Log.i(this.LOGTAG, Float.toString(width) + "x" + Float.toString(height));
        canvas.drawCircle(centerX, centerY, outerRadius, this.mPaintGray);
        canvas.drawCircle(centerX, centerY, innerRadius, this.mPaintBlack);
        File file = this.dir;
        if (file != null && (contents = file.listFiles()) != null) {
            Stack<Bitmap> thumbs = new Stack<>();
            new BitmapFactory();
            int length = contents.length;
            int i2 = 0;
            while (true) {
                float height2 = height;
                if (i2 >= length) {
                    break;
                }
                File file2 = contents[i2];
                if (!FileUtilities.isThumbnail(file2)) {
                    i = length;
                } else {
                    thumbs.push(BitmapFactory.decodeFile(file2.getAbsolutePath()));
                    i = length;
                    if (thumbs.size() > 3) {
                        break;
                    }
                }
                i2++;
                height = height2;
                length = i;
            }
            Log.i(this.LOGTAG, Integer.toString(thumbs.size()));
            if (!thumbs.isEmpty() && (size = thumbs.size()) != 0) {
                if (size == 1) {
                    float left = centerX - (thumbWidth * 0.5f);
                    float top = centerY - (thumbHeight * 0.5f);
                    float right = left + thumbWidth;
                    RectF dest = new RectF(left, top, right, top + thumbHeight);
                    RectF shadowBox = new RectF(dest);
                    shadowBox.inset(-1.0f, -1.0f);
                    canvas.drawRect(shadowBox, this.mPaintShadow);
                    canvas.drawBitmap(thumbs.pop(), (Rect) null, dest, (Paint) null);
                } else if (size == 2) {
                    float left2 = (centerX - (thumbWidth * 0.5f)) + (DZx * 0.5f);
                    float top2 = (centerY - (thumbHeight * 0.5f)) - (0.5f * DZy);
                    float right2 = left2 + thumbWidth;
                    float bottom = top2 + thumbHeight;
                    RectF dest2 = new RectF(left2, top2, right2, bottom);
                    RectF shadowBox2 = new RectF(dest2);
                    shadowBox2.inset(-1.0f, -1.0f);
                    int size2 = thumbs.size();
                    int i3 = 1;
                    while (i3 <= size2) {
                        canvas.drawRect(shadowBox2, this.mPaintShadow);
                        canvas.drawBitmap(thumbs.pop(), (Rect) null, dest2, (Paint) null);
                        dest2.offset(-DZx, DZy);
                        shadowBox2.offset(-DZx, DZy);
                        i3++;
                        size2 = size2;
                        bottom = bottom;
                    }
                } else if (size == 3) {
                    float left3 = (centerX - (thumbWidth * 0.5f)) + DZx;
                    float top3 = (centerY - (thumbHeight * 0.5f)) - DZy;
                    float right3 = left3 + thumbWidth;
                    float bottom2 = top3 + thumbHeight;
                    RectF dest3 = new RectF(left3, top3, right3, bottom2);
                    RectF shadowBox3 = new RectF(dest3);
                    shadowBox3.inset(-1.0f, -1.0f);
                    int size3 = thumbs.size();
                    int i4 = 1;
                    while (i4 <= size3) {
                        canvas.drawRect(shadowBox3, this.mPaintShadow);
                        canvas.drawBitmap(thumbs.pop(), (Rect) null, dest3, (Paint) null);
                        dest3.offset(-DZx, DZy);
                        shadowBox3.offset(-DZx, DZy);
                        i4++;
                        size3 = size3;
                        bottom2 = bottom2;
                    }
                } else if (size == 4) {
                    float left4 = (centerX - (thumbWidth * 0.5f)) + (DZx * 1.5f);
                    float top4 = (centerY - (0.5f * thumbHeight)) - (1.5f * DZy);
                    float right4 = left4 + thumbWidth;
                    float bottom3 = top4 + thumbHeight;
                    RectF dest4 = new RectF(left4, top4, right4, bottom3);
                    RectF shadowBox4 = new RectF(dest4);
                    shadowBox4.inset(-1.0f, -1.0f);
                    int size4 = thumbs.size();
                    int i5 = 1;
                    while (i5 <= size4) {
                        canvas.drawRect(shadowBox4, this.mPaintShadow);
                        canvas.drawBitmap(thumbs.pop(), (Rect) null, dest4, (Paint) null);
                        dest4.offset(-DZx, DZy);
                        shadowBox4.offset(-DZx, DZy);
                        i5++;
                        size4 = size4;
                        bottom3 = bottom3;
                    }
                }
            }
        }
    }
}
