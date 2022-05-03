package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

/* loaded from: classes2.dex */
public class MaterialCardView extends CardView implements Checkable {
    private static final String LOG_TAG = "MaterialCardView";
    private final MaterialCardViewHelper cardViewHelper;
    private boolean checked;
    private final FrameLayout contentLayout;
    private boolean dragged;
    private final boolean isParentCardViewDoneInitializing;
    private OnCheckedChangeListener onCheckedChangeListener;
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int[] DRAGGED_STATE_SET = {R.attr.state_dragged};
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CardView;

    /* loaded from: classes2.dex */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(MaterialCardView materialCardView, boolean z);
    }

    public MaterialCardView(Context context) {
        this(context, null);
    }

    public MaterialCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialCardViewStyle);
    }

    public MaterialCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(ThemeEnforcement.createThemedContext(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        this.checked = false;
        this.dragged = false;
        this.isParentCardViewDoneInitializing = true;
        Context context2 = getContext();
        TypedArray attributes = ThemeEnforcement.obtainStyledAttributes(context2, attrs, R.styleable.MaterialCardView, defStyleAttr, DEF_STYLE_RES, new int[0]);
        this.cardViewHelper = new MaterialCardViewHelper(this, attrs, defStyleAttr, DEF_STYLE_RES);
        this.cardViewHelper.setCardBackgroundColor(super.getCardBackgroundColor());
        this.cardViewHelper.setUserContentPadding(super.getContentPaddingLeft(), super.getContentPaddingTop(), super.getContentPaddingRight(), super.getContentPaddingBottom());
        this.cardViewHelper.loadFromAttributes(attributes);
        this.contentLayout = new FrameLayout(context2);
        super.addView(this.contentLayout, -1, new FrameLayout.LayoutParams(-1, -1));
        updateContentLayout();
        attributes.recycle();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MaterialCardView.class.getName());
        info.setCheckable(isCheckable());
        info.setLongClickable(isCheckable());
        info.setClickable(isClickable());
    }

    private void updateContentLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.cardViewHelper.createOutlineProvider(this.contentLayout);
        }
    }

    @Override // androidx.cardview.widget.CardView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.cardViewHelper.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setStrokeColor(@ColorInt int strokeColor) {
        this.cardViewHelper.setStrokeColor(strokeColor);
    }

    @ColorInt
    public int getStrokeColor() {
        return this.cardViewHelper.getStrokeColor();
    }

    public void setStrokeWidth(@Dimension int strokeWidth) {
        this.cardViewHelper.setStrokeWidth(strokeWidth);
        updateContentLayout();
    }

    @Dimension
    public int getStrokeWidth() {
        return this.cardViewHelper.getStrokeWidth();
    }

    @Override // androidx.cardview.widget.CardView
    public void setRadius(float radius) {
        super.setRadius(radius);
        this.cardViewHelper.setCornerRadius(radius);
        updateContentLayout();
    }

    @Override // androidx.cardview.widget.CardView
    public float getRadius() {
        return this.cardViewHelper.getCornerRadius();
    }

    public float getCardViewRadius() {
        return super.getRadius();
    }

    @Override // androidx.cardview.widget.CardView
    public void setContentPadding(int left, int top, int right, int bottom) {
        this.cardViewHelper.setUserContentPadding(left, top, right, bottom);
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingLeft() {
        return this.cardViewHelper.getUserContentPadding().left;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingTop() {
        return this.cardViewHelper.getUserContentPadding().top;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingRight() {
        return this.cardViewHelper.getUserContentPadding().right;
    }

    @Override // androidx.cardview.widget.CardView
    public int getContentPaddingBottom() {
        return this.cardViewHelper.getUserContentPadding().bottom;
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(@ColorInt int color) {
        this.cardViewHelper.setCardBackgroundColor(ColorStateList.valueOf(color));
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        this.cardViewHelper.setCardBackgroundColor(color);
    }

    @Override // androidx.cardview.widget.CardView
    public ColorStateList getCardBackgroundColor() {
        return this.cardViewHelper.getCardBackgroundColor();
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.contentLayout.getLayoutParams();
        if (params instanceof FrameLayout.LayoutParams) {
            layoutParams.gravity = ((FrameLayout.LayoutParams) params).gravity;
            this.contentLayout.requestLayout();
        }
    }

    @Override // android.view.View
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        this.cardViewHelper.updateClickable();
    }

    @Override // androidx.cardview.widget.CardView
    public void setCardElevation(float elevation) {
        super.setCardElevation(elevation);
        this.cardViewHelper.updateElevation();
    }

    @Override // androidx.cardview.widget.CardView
    public void setMaxCardElevation(float maxCardElevation) {
        super.setMaxCardElevation(maxCardElevation);
        this.cardViewHelper.updateInsets();
    }

    @Override // androidx.cardview.widget.CardView
    public void setUseCompatPadding(boolean useCompatPadding) {
        super.setUseCompatPadding(useCompatPadding);
        this.cardViewHelper.updateInsets();
        this.cardViewHelper.updateContentPadding();
    }

    @Override // androidx.cardview.widget.CardView
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        super.setPreventCornerOverlap(preventCornerOverlap);
        this.cardViewHelper.updateInsets();
        this.cardViewHelper.updateContentPadding();
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        this.contentLayout.addView(child, index, params);
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        this.contentLayout.removeAllViews();
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        this.contentLayout.removeView(view);
    }

    @Override // android.view.ViewGroup
    public void removeViewInLayout(View view) {
        this.contentLayout.removeViewInLayout(view);
    }

    @Override // android.view.ViewGroup
    public void removeViewsInLayout(int start, int count) {
        this.contentLayout.removeViewsInLayout(start, count);
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int index) {
        this.contentLayout.removeViewAt(index);
    }

    @Override // android.view.ViewGroup
    public void removeViews(int start, int count) {
        this.contentLayout.removeViews(start, count);
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (this.isParentCardViewDoneInitializing) {
            if (!this.cardViewHelper.isBackgroundOverwritten()) {
                Log.i(LOG_TAG, "Setting a custom background is not supported.");
                this.cardViewHelper.setBackgroundOverwritten(true);
            }
            super.setBackgroundDrawable(drawable);
        }
    }

    public void setBackgroundInternal(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    public void setContentPaddingInternal(int left, int top, int right, int bottom) {
        super.setContentPadding(left, top, right, bottom);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.checked;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            toggle();
        }
    }

    public void setDragged(boolean dragged) {
        if (this.dragged != dragged) {
            this.dragged = dragged;
            refreshDrawableState();
            invalidate();
        }
    }

    public boolean isDragged() {
        return this.dragged;
    }

    public boolean isCheckable() {
        return this.cardViewHelper.isCheckable();
    }

    public void setCheckable(boolean checkable) {
        this.cardViewHelper.setCheckable(checkable);
    }

    @Override // android.widget.Checkable
    public void toggle() {
        if (isCheckable() && isEnabled()) {
            this.checked = !this.checked;
            refreshDrawableState();
            if (Build.VERSION.SDK_INT > 26) {
                this.cardViewHelper.forceRippleRedraw();
            }
            OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(this, this.checked);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        if (isDragged()) {
            mergeDrawableStates(drawableState, DRAGGED_STATE_SET);
        }
        return drawableState;
    }

    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setRippleColor(@Nullable ColorStateList rippleColor) {
        this.cardViewHelper.setRippleColor(rippleColor);
    }

    public void setRippleColorResource(@ColorRes int rippleColorResourceId) {
        this.cardViewHelper.setRippleColor(AppCompatResources.getColorStateList(getContext(), rippleColorResourceId));
    }

    public ColorStateList getRippleColor() {
        return this.cardViewHelper.getRippleColor();
    }

    @Nullable
    public Drawable getCheckedIcon() {
        return this.cardViewHelper.getCheckedIcon();
    }

    public void setCheckedIconResource(@DrawableRes int id) {
        this.cardViewHelper.setCheckedIcon(AppCompatResources.getDrawable(getContext(), id));
    }

    public void setCheckedIcon(@Nullable Drawable checkedIcon) {
        this.cardViewHelper.setCheckedIcon(checkedIcon);
    }

    @Nullable
    public ColorStateList getCheckedIconTint() {
        return this.cardViewHelper.getCheckedIconTint();
    }

    public void setCheckedIconTint(@Nullable ColorStateList checkedIconTint) {
        this.cardViewHelper.setCheckedIconTint(checkedIconTint);
    }
}
