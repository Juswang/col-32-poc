package com.google.android.material.dialog;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AlertDialog;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class InsetDialogOnTouchListener implements View.OnTouchListener {
    private final Dialog dialog;
    private final int leftInset;
    private final int topInset;

    public InsetDialogOnTouchListener(AlertDialog dialog, Rect insets) {
        this.dialog = dialog;
        this.leftInset = insets.left;
        this.topInset = insets.top;
    }

    public InsetDialogOnTouchListener(android.app.AlertDialog dialog, Rect insets) {
        this.dialog = dialog;
        this.leftInset = insets.left;
        this.topInset = insets.top;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent event) {
        View insetView = view.findViewById(16908290);
        int insetLeft = this.leftInset + insetView.getLeft();
        int insetRight = insetView.getWidth() + insetLeft;
        int insetTop = this.topInset + insetView.getTop();
        int insetBottom = insetView.getHeight() + insetTop;
        RectF dialogWindow = new RectF(insetLeft, insetTop, insetRight, insetBottom);
        if (dialogWindow.contains(event.getX(), event.getY())) {
            return false;
        }
        MotionEvent outsideEvent = MotionEvent.obtain(event);
        outsideEvent.setAction(4);
        view.performClick();
        if (Build.VERSION.SDK_INT >= 28) {
            return this.dialog.onTouchEvent(outsideEvent);
        }
        this.dialog.onBackPressed();
        return true;
    }
}
