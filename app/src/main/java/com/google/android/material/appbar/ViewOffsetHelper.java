package com.google.android.material.appbar;

import android.view.View;
import androidx.core.view.ViewCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ViewOffsetHelper {
    private int layoutLeft;
    private int layoutTop;
    private int offsetLeft;
    private int offsetTop;
    private final View view;
    private boolean verticalOffsetEnabled = true;
    private boolean horizontalOffsetEnabled = true;

    public ViewOffsetHelper(View view) {
        this.view = view;
    }

    public void onViewLayout() {
        this.layoutTop = this.view.getTop();
        this.layoutLeft = this.view.getLeft();
        updateOffsets();
    }

    private void updateOffsets() {
        View view = this.view;
        ViewCompat.offsetTopAndBottom(view, this.offsetTop - (view.getTop() - this.layoutTop));
        View view2 = this.view;
        ViewCompat.offsetLeftAndRight(view2, this.offsetLeft - (view2.getLeft() - this.layoutLeft));
    }

    public boolean setTopAndBottomOffset(int offset) {
        if (!this.verticalOffsetEnabled || this.offsetTop == offset) {
            return false;
        }
        this.offsetTop = offset;
        updateOffsets();
        return true;
    }

    public boolean setLeftAndRightOffset(int offset) {
        if (!this.horizontalOffsetEnabled || this.offsetLeft == offset) {
            return false;
        }
        this.offsetLeft = offset;
        updateOffsets();
        return true;
    }

    public int getTopAndBottomOffset() {
        return this.offsetTop;
    }

    public int getLeftAndRightOffset() {
        return this.offsetLeft;
    }

    public int getLayoutTop() {
        return this.layoutTop;
    }

    public int getLayoutLeft() {
        return this.layoutLeft;
    }

    public void setVerticalOffsetEnabled(boolean verticalOffsetEnabled) {
        this.verticalOffsetEnabled = verticalOffsetEnabled;
    }

    public boolean isVerticalOffsetEnabled() {
        return this.verticalOffsetEnabled;
    }

    public void setHorizontalOffsetEnabled(boolean horizontalOffsetEnabled) {
        this.horizontalOffsetEnabled = horizontalOffsetEnabled;
    }

    public boolean isHorizontalOffsetEnabled() {
        return this.horizontalOffsetEnabled;
    }
}
