package com.google.android.material.chip;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.AnimatorRes;
import androidx.annotation.BoolRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import com.google.android.material.ripple.RippleUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes2.dex */
public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate {
    private static final int CLOSE_ICON_VIRTUAL_ID = 0;
    private static final int MIN_TOUCH_TARGET_DP = 48;
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String TAG = "Chip";
    @Nullable
    private ChipDrawable chipDrawable;
    private boolean closeIconFocused;
    private boolean closeIconHovered;
    private boolean closeIconPressed;
    private boolean deferredCheckedValue;
    private boolean ensureMinTouchTargetSize;
    private int focusedVirtualView;
    private final TextAppearanceFontCallback fontCallback;
    @Nullable
    private InsetDrawable insetBackgroundDrawable;
    private int lastLayoutDirection;
    @Dimension(unit = 1)
    private int minTouchTargetSize;
    @Nullable
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
    @Nullable
    private View.OnClickListener onCloseIconClickListener;
    private final Rect rect;
    private final RectF rectF;
    @Nullable
    private RippleDrawable ripple;
    private final ChipTouchHelper touchHelper;
    private static final Rect EMPTY_BOUNDS = new Rect();
    private static final int[] SELECTED_STATE = {16842913};

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.chipStyle);
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.focusedVirtualView = Integer.MIN_VALUE;
        this.rect = new Rect();
        this.rectF = new RectF();
        this.fontCallback = new TextAppearanceFontCallback() { // from class: com.google.android.material.chip.Chip.1
            @Override // com.google.android.material.resources.TextAppearanceFontCallback
            public void onFontRetrieved(@NonNull Typeface typeface, boolean fontResolvedSynchronously) {
                Chip chip = Chip.this;
                chip.setText(chip.chipDrawable.shouldDrawText() ? Chip.this.chipDrawable.getText() : Chip.this.getText());
                Chip.this.requestLayout();
                Chip.this.invalidate();
            }

            @Override // com.google.android.material.resources.TextAppearanceFontCallback
            public void onFontRetrievalFailed(int reason) {
            }
        };
        validateAttributes(attrs);
        ChipDrawable drawable = ChipDrawable.createFromAttributes(context, attrs, defStyleAttr, R.style.Widget_MaterialComponents_Chip_Action);
        initMinTouchTarget(context, attrs, defStyleAttr);
        setChipDrawable(drawable);
        TypedArray a = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.Chip, defStyleAttr, R.style.Widget_MaterialComponents_Chip_Action, new int[0]);
        if (Build.VERSION.SDK_INT < 23) {
            setTextColor(MaterialResources.getColorStateList(context, a, R.styleable.Chip_android_textColor));
        }
        boolean hasShapeAppearanceAttribute = a.hasValue(R.styleable.Chip_shapeAppearance);
        a.recycle();
        this.touchHelper = new ChipTouchHelper(this);
        if (Build.VERSION.SDK_INT >= 24) {
            ViewCompat.setAccessibilityDelegate(this, this.touchHelper);
        } else {
            updateAccessibilityDelegate();
        }
        if (!hasShapeAppearanceAttribute) {
            initOutlineProvider();
        }
        setChecked(this.deferredCheckedValue);
        drawable.setShouldDrawText(false);
        setText(drawable.getText());
        setEllipsize(drawable.getEllipsize());
        setIncludeFontPadding(false);
        updateTextPaintDrawState();
        if (!this.chipDrawable.shouldDrawText()) {
            setSingleLine();
        }
        setGravity(8388627);
        updatePaddingInternal();
        if (shouldEnsureMinTouchTargetSize()) {
            setMinHeight(this.minTouchTargetSize);
        }
        this.lastLayoutDirection = ViewCompat.getLayoutDirection(this);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Chip.class.getName());
        info.setCheckable(isCheckable());
        info.setClickable(isClickable());
    }

    private void updateAccessibilityDelegate() {
        if (Build.VERSION.SDK_INT < 24) {
            if (!hasCloseIcon() || !isCloseIconVisible()) {
                ViewCompat.setAccessibilityDelegate(this, null);
            } else {
                ViewCompat.setAccessibilityDelegate(this, this.touchHelper);
            }
        }
    }

    private boolean shouldEnsureMinTouchTargetSize() {
        return this.ensureMinTouchTargetSize;
    }

    private void initMinTouchTarget(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.Chip, defStyleAttr, R.style.Widget_MaterialComponents_Chip_Action, new int[0]);
            this.ensureMinTouchTargetSize = a.getBoolean(R.styleable.Chip_ensureMinTouchTargetSize, false);
            float defaultMinTouchTargetSize = (float) Math.ceil(ViewUtils.dpToPx(getContext(), 48));
            this.minTouchTargetSize = (int) Math.ceil(a.getDimension(R.styleable.Chip_chipMinTouchTargetSize, defaultMinTouchTargetSize));
            a.recycle();
        }
    }

    private void updatePaddingInternal() {
        ChipDrawable chipDrawable;
        if (!TextUtils.isEmpty(getText()) && (chipDrawable = this.chipDrawable) != null) {
            int paddingEnd = (int) (chipDrawable.getChipEndPadding() + this.chipDrawable.getTextEndPadding() + this.chipDrawable.calculateCloseIconWidth());
            int paddingStart = (int) (this.chipDrawable.getChipStartPadding() + this.chipDrawable.getTextStartPadding() + this.chipDrawable.calculateChipIconWidth());
            ViewCompat.setPaddingRelative(this, paddingStart, getPaddingTop(), paddingEnd, getPaddingBottom());
        }
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(17)
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (this.lastLayoutDirection != layoutDirection) {
            this.lastLayoutDirection = layoutDirection;
            updatePaddingInternal();
        }
    }

    private void validateAttributes(@Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "background") != null) {
                Log.w(TAG, "Do not set the background; Chip manages its own background drawable.");
            }
            if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableLeft") != null) {
                throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableStart") != null) {
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableEnd") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableRight") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (!attributeSet.getAttributeBooleanValue(NAMESPACE_ANDROID, "singleLine", true) || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "lines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "minLines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "maxLines", 1) != 1) {
                throw new UnsupportedOperationException("Chip does not support multi-line text");
            } else if (attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "gravity", 8388627) != 8388627) {
                Log.w(TAG, "Chip text must be vertically center and start aligned");
            }
        }
    }

    private void initOutlineProvider() {
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: com.google.android.material.chip.Chip.2
                @Override // android.view.ViewOutlineProvider
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    if (Chip.this.chipDrawable != null) {
                        Chip.this.chipDrawable.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
    }

    public Drawable getChipDrawable() {
        return this.chipDrawable;
    }

    public void setChipDrawable(@NonNull ChipDrawable drawable) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != drawable) {
            unapplyChipDrawable(chipDrawable);
            this.chipDrawable = drawable;
            applyChipDrawable(this.chipDrawable);
            ensureAccessibleTouchTarget(this.minTouchTargetSize);
            updateBackgroundDrawable();
        }
    }

    private void updateBackgroundDrawable() {
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            updateFrameworkRippleBackground();
            return;
        }
        this.chipDrawable.setUseCompatRipple(true);
        ViewCompat.setBackground(this, getBackgroundDrawable());
        ensureChipDrawableHasCallback();
    }

    private void ensureChipDrawableHasCallback() {
        if (getBackgroundDrawable() == this.insetBackgroundDrawable && this.chipDrawable.getCallback() == null) {
            this.chipDrawable.setCallback(this.insetBackgroundDrawable);
        }
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.insetBackgroundDrawable;
        if (insetDrawable == null) {
            return this.chipDrawable;
        }
        return insetDrawable;
    }

    private void updateFrameworkRippleBackground() {
        this.ripple = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(this.chipDrawable.getRippleColor()), getBackgroundDrawable(), null);
        this.chipDrawable.setUseCompatRipple(false);
        ViewCompat.setBackground(this, this.ripple);
    }

    private void unapplyChipDrawable(@Nullable ChipDrawable chipDrawable) {
        if (chipDrawable != null) {
            chipDrawable.setDelegate(null);
        }
    }

    private void applyChipDrawable(@NonNull ChipDrawable chipDrawable) {
        chipDrawable.setDelegate(this);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(state, SELECTED_STATE);
        }
        return state;
    }

    @Override // android.widget.TextView
    public void setGravity(int gravity) {
        if (gravity != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(gravity);
        }
    }

    @Override // android.view.View
    public void setBackgroundTintList(@Nullable ColorStateList tint) {
        Log.w(TAG, "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        Log.w(TAG, "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        Log.w(TAG, "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundResource(int resid) {
        Log.w(TAG, "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackground(Drawable background) {
        if (background == getBackgroundDrawable() || background == this.ripple) {
            super.setBackground(background);
        } else {
            Log.w(TAG, "Do not set the background; Chip manages its own background drawable.");
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundDrawable(Drawable background) {
        if (background == getBackgroundDrawable() || background == this.ripple) {
            super.setBackgroundDrawable(background);
        } else {
            Log.w(TAG, "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (left != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (right == null) {
            super.setCompoundDrawables(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        if (left != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (right == 0) {
            super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (left != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        } else if (right == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        if (start != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == null) {
            super.setCompoundDrawablesRelative(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        if (start != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == 0) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        if (start != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getEllipsize();
        }
        return null;
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt where) {
        if (this.chipDrawable != null) {
            if (where != TextUtils.TruncateAt.MARQUEE) {
                super.setEllipsize(where);
                ChipDrawable chipDrawable = this.chipDrawable;
                if (chipDrawable != null) {
                    chipDrawable.setEllipsize(where);
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean singleLine) {
        if (singleLine) {
            super.setSingleLine(singleLine);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setLines(int lines) {
        if (lines <= 1) {
            super.setLines(lines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMinLines(int minLines) {
        if (minLines <= 1) {
            super.setMinLines(minLines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxLines(int maxLines) {
        if (maxLines <= 1) {
            super.setMaxLines(maxLines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxWidth(@Px int maxWidth) {
        super.setMaxWidth(maxWidth);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setMaxWidth(maxWidth);
        }
    }

    @Override // com.google.android.material.chip.ChipDrawable.Delegate
    public void onChipDrawableSizeChange() {
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
        updateBackgroundDrawable();
        updatePaddingInternal();
        requestLayout();
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean checked) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            this.deferredCheckedValue = checked;
        } else if (chipDrawable.isCheckable()) {
            boolean wasChecked = isChecked();
            super.setChecked(checked);
            if (wasChecked != checked && (onCheckedChangeListener = this.onCheckedChangeListenerInternal) != null) {
                onCheckedChangeListener.onCheckedChanged(this, checked);
            }
        }
    }

    public void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener listener) {
        this.onCheckedChangeListenerInternal = listener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener listener) {
        this.onCloseIconClickListener = listener;
    }

    @CallSuper
    public boolean performCloseIconClick() {
        boolean result;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.onCloseIconClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            result = true;
        } else {
            result = false;
        }
        this.touchHelper.sendEventForVirtualView(0, 1);
        return result;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001f, code lost:
        if (r1 != 3) goto L20;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            r0 = 0
            int r1 = r7.getActionMasked()
            android.graphics.RectF r2 = r6.getCloseIconTouchBounds()
            float r3 = r7.getX()
            float r4 = r7.getY()
            boolean r2 = r2.contains(r3, r4)
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L39
            if (r1 == r4) goto L2d
            r5 = 2
            if (r1 == r5) goto L22
            r5 = 3
            if (r1 == r5) goto L35
            goto L3f
        L22:
            boolean r5 = r6.closeIconPressed
            if (r5 == 0) goto L3f
            if (r2 != 0) goto L2b
            r6.setCloseIconPressed(r3)
        L2b:
            r0 = 1
            goto L3f
        L2d:
            boolean r5 = r6.closeIconPressed
            if (r5 == 0) goto L35
            r6.performCloseIconClick()
            r0 = 1
        L35:
            r6.setCloseIconPressed(r3)
            goto L3f
        L39:
            if (r2 == 0) goto L3f
            r6.setCloseIconPressed(r4)
            r0 = 1
        L3f:
            if (r0 != 0) goto L47
            boolean r5 = super.onTouchEvent(r7)
            if (r5 == 0) goto L48
        L47:
            r3 = 1
        L48:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 7) {
            setCloseIconHovered(getCloseIconTouchBounds().contains(event.getX(), event.getY()));
        } else if (action == 10) {
            setCloseIconHovered(false);
        }
        return super.onHoverEvent(event);
    }

    @SuppressLint({"PrivateApi"})
    private boolean handleAccessibilityExit(MotionEvent event) {
        if (event.getAction() == 10) {
            try {
                Field f = ExploreByTouchHelper.class.getDeclaredField("mHoveredVirtualViewId");
                f.setAccessible(true);
                int mHoveredVirtualViewId = ((Integer) f.get(this.touchHelper)).intValue();
                if (mHoveredVirtualViewId != Integer.MIN_VALUE) {
                    Method m = ExploreByTouchHelper.class.getDeclaredMethod("updateHoveredVirtualView", Integer.TYPE);
                    m.setAccessible(true);
                    m.invoke(this.touchHelper, Integer.MIN_VALUE);
                    return true;
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Unable to send Accessibility Exit event", e);
            } catch (NoSuchFieldException e2) {
                Log.e(TAG, "Unable to send Accessibility Exit event", e2);
            } catch (NoSuchMethodException e3) {
                Log.e(TAG, "Unable to send Accessibility Exit event", e3);
            } catch (InvocationTargetException e4) {
                Log.e(TAG, "Unable to send Accessibility Exit event", e4);
            }
        }
        return false;
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return handleAccessibilityExit(event) || this.touchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        return this.touchHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            setFocusedVirtualView(-1);
        } else {
            setFocusedVirtualView(Integer.MIN_VALUE);
        }
        invalidate();
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.touchHelper.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean focusChanged = false;
        int keyCode2 = event.getKeyCode();
        if (keyCode2 != 61) {
            if (keyCode2 != 66) {
                switch (keyCode2) {
                    case 21:
                        if (event.hasNoModifiers()) {
                            focusChanged = moveFocus(ViewUtils.isLayoutRtl(this));
                            break;
                        }
                        break;
                    case 22:
                        if (event.hasNoModifiers()) {
                            focusChanged = moveFocus(!ViewUtils.isLayoutRtl(this));
                            break;
                        }
                        break;
                }
            }
            int i = this.focusedVirtualView;
            if (i == -1) {
                performClick();
                return true;
            } else if (i == 0) {
                performCloseIconClick();
                return true;
            }
        } else {
            int focusChangeDirection = 0;
            if (event.hasNoModifiers()) {
                focusChangeDirection = 2;
            } else if (event.hasModifiers(1)) {
                focusChangeDirection = 1;
            }
            if (focusChangeDirection != 0) {
                ViewParent parent = getParent();
                View nextFocus = this;
                do {
                    nextFocus = nextFocus.focusSearch(focusChangeDirection);
                    if (nextFocus == null || nextFocus == this) {
                        break;
                    }
                } while (nextFocus.getParent() == parent);
                if (nextFocus != null) {
                    nextFocus.requestFocus();
                    return true;
                }
            }
        }
        if (!focusChanged) {
            return super.onKeyDown(keyCode, event);
        }
        invalidate();
        return true;
    }

    private boolean moveFocus(boolean positive) {
        ensureFocus();
        if (positive) {
            if (this.focusedVirtualView != -1) {
                return false;
            }
            setFocusedVirtualView(0);
            return true;
        } else if (this.focusedVirtualView != 0) {
            return false;
        } else {
            setFocusedVirtualView(-1);
            return true;
        }
    }

    private void ensureFocus() {
        if (this.focusedVirtualView == Integer.MIN_VALUE) {
            setFocusedVirtualView(-1);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect r) {
        if (this.focusedVirtualView == 0) {
            r.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(r);
        }
    }

    private void setFocusedVirtualView(int virtualView) {
        int i = this.focusedVirtualView;
        if (i != virtualView) {
            if (i == 0) {
                setCloseIconFocused(false);
            }
            this.focusedVirtualView = virtualView;
            if (virtualView == 0) {
                setCloseIconFocused(true);
            }
        }
    }

    private void setCloseIconPressed(boolean pressed) {
        if (this.closeIconPressed != pressed) {
            this.closeIconPressed = pressed;
            refreshDrawableState();
        }
    }

    private void setCloseIconHovered(boolean hovered) {
        if (this.closeIconHovered != hovered) {
            this.closeIconHovered = hovered;
            refreshDrawableState();
        }
    }

    private void setCloseIconFocused(boolean focused) {
        if (this.closeIconFocused != focused) {
            this.closeIconFocused = focused;
            refreshDrawableState();
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        boolean changed = false;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null && chipDrawable.isCloseIconStateful()) {
            changed = this.chipDrawable.setCloseIconState(createCloseIconDrawableState());
        }
        if (changed) {
            invalidate();
        }
    }

    private int[] createCloseIconDrawableState() {
        int count = 0;
        if (isEnabled()) {
            count = 0 + 1;
        }
        if (this.closeIconFocused) {
            count++;
        }
        if (this.closeIconHovered) {
            count++;
        }
        if (this.closeIconPressed) {
            count++;
        }
        if (isChecked()) {
            count++;
        }
        int[] stateSet = new int[count];
        int i = 0;
        if (isEnabled()) {
            stateSet[0] = 16842910;
            i = 0 + 1;
        }
        if (this.closeIconFocused) {
            stateSet[i] = 16842908;
            i++;
        }
        if (this.closeIconHovered) {
            stateSet[i] = 16843623;
            i++;
        }
        if (this.closeIconPressed) {
            stateSet[i] = 16842919;
            i++;
        }
        if (isChecked()) {
            stateSet[i] = 16842913;
            int i2 = i + 1;
        }
        return stateSet;
    }

    public boolean hasCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return (chipDrawable == null || chipDrawable.getCloseIcon() == null) ? false : true;
    }

    public RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        if (hasCloseIcon()) {
            this.chipDrawable.getCloseIconTouchBounds(this.rectF);
        }
        return this.rectF;
    }

    public Rect getCloseIconTouchBoundsInt() {
        RectF bounds = getCloseIconTouchBounds();
        this.rect.set((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
        return this.rect;
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    @TargetApi(24)
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (!getCloseIconTouchBounds().contains(event.getX(), event.getY()) || !isEnabled()) {
            return null;
        }
        return PointerIcon.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND);
    }

    /* loaded from: classes2.dex */
    public class ChipTouchHelper extends ExploreByTouchHelper {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ChipTouchHelper(Chip view) {
            super(view);
            Chip.this = r1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float x, float y) {
            return (!Chip.this.hasCloseIcon() || !Chip.this.getCloseIconTouchBounds().contains(x, y)) ? -1 : 0;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            if (Chip.this.hasCloseIcon()) {
                virtualViewIds.add(0);
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {
            CharSequence charSequence = "";
            if (Chip.this.hasCloseIcon()) {
                CharSequence closeIconContentDescription = Chip.this.getCloseIconContentDescription();
                if (closeIconContentDescription != null) {
                    node.setContentDescription(closeIconContentDescription);
                } else {
                    CharSequence chipText = Chip.this.getText();
                    Context context = Chip.this.getContext();
                    int i = R.string.mtrl_chip_close_icon_content_description;
                    Object[] objArr = new Object[1];
                    if (!TextUtils.isEmpty(chipText)) {
                        charSequence = chipText;
                    }
                    objArr[0] = charSequence;
                    node.setContentDescription(context.getString(i, objArr).trim());
                }
                node.setBoundsInParent(Chip.this.getCloseIconTouchBoundsInt());
                node.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                node.setEnabled(Chip.this.isEnabled());
                return;
            }
            node.setContentDescription(charSequence);
            node.setBoundsInParent(Chip.EMPTY_BOUNDS);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat node) {
            node.setCheckable(Chip.this.isCheckable());
            node.setClickable(Chip.this.isClickable());
            node.setClassName(Chip.class.getName());
            CharSequence chipText = Chip.this.getText();
            if (Build.VERSION.SDK_INT >= 23) {
                node.setText(chipText);
            } else {
                node.setContentDescription(chipText);
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            if (action == 16 && virtualViewId == 0) {
                return Chip.this.performCloseIconClick();
            }
            return false;
        }
    }

    @Nullable
    public ColorStateList getChipBackgroundColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipBackgroundColor();
        }
        return null;
    }

    public void setChipBackgroundColorResource(@ColorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColorResource(id);
        }
    }

    public void setChipBackgroundColor(@Nullable ColorStateList chipBackgroundColor) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColor(chipBackgroundColor);
        }
    }

    public float getChipMinHeight() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipMinHeight();
        }
        return 0.0f;
    }

    public void setChipMinHeightResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeightResource(id);
        }
    }

    public void setChipMinHeight(float minHeight) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeight(minHeight);
        }
    }

    public float getChipCornerRadius() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipCornerRadius();
        }
        return 0.0f;
    }

    @Deprecated
    public void setChipCornerRadiusResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadiusResource(id);
        }
    }

    @Deprecated
    public void setChipCornerRadius(float chipCornerRadius) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadius(chipCornerRadius);
        }
    }

    @Nullable
    public ColorStateList getChipStrokeColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipStrokeColor();
        }
        return null;
    }

    public void setChipStrokeColorResource(@ColorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColorResource(id);
        }
    }

    public void setChipStrokeColor(@Nullable ColorStateList chipStrokeColor) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColor(chipStrokeColor);
        }
    }

    public float getChipStrokeWidth() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipStrokeWidth();
        }
        return 0.0f;
    }

    public void setChipStrokeWidthResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidthResource(id);
        }
    }

    public void setChipStrokeWidth(float chipStrokeWidth) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidth(chipStrokeWidth);
        }
    }

    @Nullable
    public ColorStateList getRippleColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getRippleColor();
        }
        return null;
    }

    public void setRippleColorResource(@ColorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColorResource(id);
            if (!this.chipDrawable.getUseCompatRipple()) {
                updateFrameworkRippleBackground();
            }
        }
    }

    public void setRippleColor(@Nullable ColorStateList rippleColor) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColor(rippleColor);
        }
        if (!this.chipDrawable.getUseCompatRipple()) {
            updateFrameworkRippleBackground();
        }
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    @Override // android.view.View
    public void setLayoutDirection(int layoutDirection) {
        if (this.chipDrawable != null && Build.VERSION.SDK_INT >= 17) {
            super.setLayoutDirection(layoutDirection);
        }
    }

    @Override // android.widget.TextView
    public void setText(CharSequence text, TextView.BufferType type) {
        if (this.chipDrawable != null) {
            if (text == null) {
                text = "";
            }
            super.setText(this.chipDrawable.shouldDrawText() ? null : text, type);
            ChipDrawable chipDrawable = this.chipDrawable;
            if (chipDrawable != null) {
                chipDrawable.setText(text);
            }
        }
    }

    @Deprecated
    public void setChipTextResource(@StringRes int id) {
        setText(getResources().getString(id));
    }

    @Deprecated
    public void setChipText(@Nullable CharSequence chipText) {
        setText(chipText);
    }

    public void setTextAppearanceResource(@StyleRes int id) {
        setTextAppearance(getContext(), id);
    }

    public void setTextAppearance(@Nullable TextAppearance textAppearance) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearance(textAppearance);
        }
        updateTextPaintDrawState();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(resId);
        }
        updateTextPaintDrawState();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int resId) {
        super.setTextAppearance(resId);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(resId);
        }
        updateTextPaintDrawState();
    }

    private void updateTextPaintDrawState() {
        TextPaint textPaint = getPaint();
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            textPaint.drawableState = chipDrawable.getState();
        }
        TextAppearance textAppearance = getTextAppearance();
        if (textAppearance != null) {
            textAppearance.updateDrawState(getContext(), textPaint, this.fontCallback);
        }
    }

    @Nullable
    private TextAppearance getTextAppearance() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getTextAppearance();
        }
        return null;
    }

    public boolean isChipIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isChipIconVisible();
    }

    @Deprecated
    public boolean isChipIconEnabled() {
        return isChipIconVisible();
    }

    public void setChipIconVisible(@BoolRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(id);
        }
    }

    public void setChipIconVisible(boolean chipIconVisible) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(chipIconVisible);
        }
    }

    @Deprecated
    public void setChipIconEnabledResource(@BoolRes int id) {
        setChipIconVisible(id);
    }

    @Deprecated
    public void setChipIconEnabled(boolean chipIconEnabled) {
        setChipIconVisible(chipIconEnabled);
    }

    @Nullable
    public Drawable getChipIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipIcon();
        }
        return null;
    }

    public void setChipIconResource(@DrawableRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconResource(id);
        }
    }

    public void setChipIcon(@Nullable Drawable chipIcon) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIcon(chipIcon);
        }
    }

    @Nullable
    public ColorStateList getChipIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipIconTint();
        }
        return null;
    }

    public void setChipIconTintResource(@ColorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTintResource(id);
        }
    }

    public void setChipIconTint(@Nullable ColorStateList chipIconTint) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTint(chipIconTint);
        }
    }

    public float getChipIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipIconSize();
        }
        return 0.0f;
    }

    public void setChipIconSizeResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSizeResource(id);
        }
    }

    public void setChipIconSize(float chipIconSize) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSize(chipIconSize);
        }
    }

    public boolean isCloseIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCloseIconVisible();
    }

    @Deprecated
    public boolean isCloseIconEnabled() {
        return isCloseIconVisible();
    }

    public void setCloseIconVisible(@BoolRes int id) {
        setCloseIconVisible(getResources().getBoolean(id));
    }

    public void setCloseIconVisible(boolean closeIconVisible) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconVisible(closeIconVisible);
        }
        updateAccessibilityDelegate();
    }

    @Deprecated
    public void setCloseIconEnabledResource(@BoolRes int id) {
        setCloseIconVisible(id);
    }

    @Deprecated
    public void setCloseIconEnabled(boolean closeIconEnabled) {
        setCloseIconVisible(closeIconEnabled);
    }

    @Nullable
    public Drawable getCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIcon();
        }
        return null;
    }

    public void setCloseIconResource(@DrawableRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconResource(id);
        }
        updateAccessibilityDelegate();
    }

    public void setCloseIcon(@Nullable Drawable closeIcon) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIcon(closeIcon);
        }
        updateAccessibilityDelegate();
    }

    @Nullable
    public ColorStateList getCloseIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIconTint();
        }
        return null;
    }

    public void setCloseIconTintResource(@ColorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTintResource(id);
        }
    }

    public void setCloseIconTint(@Nullable ColorStateList closeIconTint) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTint(closeIconTint);
        }
    }

    public float getCloseIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIconSize();
        }
        return 0.0f;
    }

    public void setCloseIconSizeResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSizeResource(id);
        }
    }

    public void setCloseIconSize(float closeIconSize) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSize(closeIconSize);
        }
    }

    public void setCloseIconContentDescription(@Nullable CharSequence closeIconContentDescription) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconContentDescription(closeIconContentDescription);
        }
    }

    @Nullable
    public CharSequence getCloseIconContentDescription() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIconContentDescription();
        }
        return null;
    }

    public boolean isCheckable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCheckable();
    }

    public void setCheckableResource(@BoolRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckableResource(id);
        }
    }

    public void setCheckable(boolean checkable) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckable(checkable);
        }
    }

    public boolean isCheckedIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCheckedIconVisible();
    }

    @Deprecated
    public boolean isCheckedIconEnabled() {
        return isCheckedIconVisible();
    }

    public void setCheckedIconVisible(@BoolRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(id);
        }
    }

    public void setCheckedIconVisible(boolean checkedIconVisible) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(checkedIconVisible);
        }
    }

    @Deprecated
    public void setCheckedIconEnabledResource(@BoolRes int id) {
        setCheckedIconVisible(id);
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean checkedIconEnabled) {
        setCheckedIconVisible(checkedIconEnabled);
    }

    @Nullable
    public Drawable getCheckedIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCheckedIcon();
        }
        return null;
    }

    public void setCheckedIconResource(@DrawableRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconResource(id);
        }
    }

    public void setCheckedIcon(@Nullable Drawable checkedIcon) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIcon(checkedIcon);
        }
    }

    @Nullable
    public MotionSpec getShowMotionSpec() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getShowMotionSpec();
        }
        return null;
    }

    public void setShowMotionSpecResource(@AnimatorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpecResource(id);
        }
    }

    public void setShowMotionSpec(@Nullable MotionSpec showMotionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpec(showMotionSpec);
        }
    }

    @Nullable
    public MotionSpec getHideMotionSpec() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getHideMotionSpec();
        }
        return null;
    }

    public void setHideMotionSpecResource(@AnimatorRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpecResource(id);
        }
    }

    public void setHideMotionSpec(@Nullable MotionSpec hideMotionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpec(hideMotionSpec);
        }
    }

    public float getChipStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipStartPadding();
        }
        return 0.0f;
    }

    public void setChipStartPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPaddingResource(id);
        }
    }

    public void setChipStartPadding(float chipStartPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPadding(chipStartPadding);
        }
    }

    public float getIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getIconStartPadding();
        }
        return 0.0f;
    }

    public void setIconStartPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPaddingResource(id);
        }
    }

    public void setIconStartPadding(float iconStartPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPadding(iconStartPadding);
        }
    }

    public float getIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getIconEndPadding();
        }
        return 0.0f;
    }

    public void setIconEndPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPaddingResource(id);
        }
    }

    public void setIconEndPadding(float iconEndPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPadding(iconEndPadding);
        }
    }

    public float getTextStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getTextStartPadding();
        }
        return 0.0f;
    }

    public void setTextStartPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPaddingResource(id);
        }
    }

    public void setTextStartPadding(float textStartPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPadding(textStartPadding);
        }
    }

    public float getTextEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getTextEndPadding();
        }
        return 0.0f;
    }

    public void setTextEndPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPaddingResource(id);
        }
    }

    public void setTextEndPadding(float textEndPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPadding(textEndPadding);
        }
    }

    public float getCloseIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIconStartPadding();
        }
        return 0.0f;
    }

    public void setCloseIconStartPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPaddingResource(id);
        }
    }

    public void setCloseIconStartPadding(float closeIconStartPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPadding(closeIconStartPadding);
        }
    }

    public float getCloseIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getCloseIconEndPadding();
        }
        return 0.0f;
    }

    public void setCloseIconEndPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPaddingResource(id);
        }
    }

    public void setCloseIconEndPadding(float closeIconEndPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPadding(closeIconEndPadding);
        }
    }

    public float getChipEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.getChipEndPadding();
        }
        return 0.0f;
    }

    public void setChipEndPaddingResource(@DimenRes int id) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPaddingResource(id);
        }
    }

    public void setChipEndPadding(float chipEndPadding) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPadding(chipEndPadding);
        }
    }

    public boolean getEnsureMinTouchTargetSize() {
        return this.ensureMinTouchTargetSize;
    }

    public void setEnsureMinTouchTargetSize(boolean flag) {
        this.ensureMinTouchTargetSize = flag;
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
    }

    public boolean ensureAccessibleTouchTarget(@Dimension int minTargetPx) {
        this.minTouchTargetSize = minTargetPx;
        int deltaY = 0;
        if (!shouldEnsureMinTouchTargetSize()) {
            return false;
        }
        int deltaHeight = Math.max(0, minTargetPx - this.chipDrawable.getIntrinsicHeight());
        int deltaWidth = Math.max(0, minTargetPx - this.chipDrawable.getIntrinsicWidth());
        if (deltaWidth > 0 || deltaHeight > 0) {
            int deltaX = deltaWidth > 0 ? deltaWidth / 2 : 0;
            if (deltaHeight > 0) {
                deltaY = deltaHeight / 2;
            }
            if (this.insetBackgroundDrawable != null) {
                Rect padding = new Rect();
                this.insetBackgroundDrawable.getPadding(padding);
                if (padding.top == deltaY && padding.bottom == deltaY && padding.left == deltaX && padding.right == deltaX) {
                    return true;
                }
            }
            if (Build.VERSION.SDK_INT < 16) {
                setMinHeight(minTargetPx);
            } else if (getMinHeight() != minTargetPx) {
                setMinHeight(minTargetPx);
            }
            insetChipBackgroundDrawable(deltaX, deltaY, deltaX, deltaY);
            return true;
        }
        this.insetBackgroundDrawable = null;
        return false;
    }

    private void insetChipBackgroundDrawable(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        this.insetBackgroundDrawable = new InsetDrawable((Drawable) this.chipDrawable, insetLeft, insetTop, insetRight, insetBottom);
    }
}
