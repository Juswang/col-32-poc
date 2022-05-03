package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/* loaded from: classes2.dex */
public class WrappedDrawableApi14 extends Drawable implements Drawable.Callback, WrappedDrawable, TintAwareDrawable {
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    WrappedDrawableState mState;

    public WrappedDrawableApi14(@NonNull WrappedDrawableState state, @Nullable Resources res) {
        this.mState = state;
        updateLocalState(res);
    }

    public WrappedDrawableApi14(@Nullable Drawable dr) {
        this.mState = mutateConstantState();
        setWrappedDrawable(dr);
    }

    private void updateLocalState(@Nullable Resources res) {
        WrappedDrawableState wrappedDrawableState = this.mState;
        if (wrappedDrawableState != null && wrappedDrawableState.mDrawableState != null) {
            setWrappedDrawable(this.mState.mDrawableState.newDrawable(res));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(@NonNull Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setChangingConfigurations(int configs) {
        this.mDrawable.setChangingConfigurations(configs);
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        int changingConfigurations = super.getChangingConfigurations();
        WrappedDrawableState wrappedDrawableState = this.mState;
        return changingConfigurations | (wrappedDrawableState != null ? wrappedDrawableState.getChangingConfigurations() : 0) | this.mDrawable.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        this.mDrawable.setDither(dither);
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        this.mDrawable.setFilterBitmap(filter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mDrawable.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mDrawable.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        WrappedDrawableState wrappedDrawableState;
        ColorStateList tintList = (!isCompatTintEnabled() || (wrappedDrawableState = this.mState) == null) ? null : wrappedDrawableState.mTint;
        return (tintList != null && tintList.isStateful()) || this.mDrawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setState(@NonNull int[] stateSet) {
        boolean handled = this.mDrawable.setState(stateSet);
        boolean handled2 = updateTint(stateSet) || handled;
        return handled2;
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public int[] getState() {
        return this.mDrawable.getState();
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || this.mDrawable.setVisible(visible, restart);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(@NonNull Rect padding) {
        return this.mDrawable.getPadding(padding);
    }

    @Override // android.graphics.drawable.Drawable
    @RequiresApi(19)
    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    @Override // android.graphics.drawable.Drawable
    @RequiresApi(19)
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    @Override // android.graphics.drawable.Drawable
    @Nullable
    public Drawable.ConstantState getConstantState() {
        WrappedDrawableState wrappedDrawableState = this.mState;
        if (wrappedDrawableState == null || !wrappedDrawableState.canConstantState()) {
            return null;
        }
        this.mState.mChangingConfigurations = getChangingConfigurations();
        return this.mState;
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = mutateConstantState();
            Drawable drawable = this.mDrawable;
            if (drawable != null) {
                drawable.mutate();
            }
            WrappedDrawableState wrappedDrawableState = this.mState;
            if (wrappedDrawableState != null) {
                Drawable drawable2 = this.mDrawable;
                wrappedDrawableState.mDrawableState = drawable2 != null ? drawable2.getConstantState() : null;
            }
            this.mMutated = true;
        }
        return this;
    }

    @NonNull
    private WrappedDrawableState mutateConstantState() {
        return new WrappedDrawableState(this.mState);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(@NonNull Drawable who) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        unscheduleSelf(what);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int level) {
        return this.mDrawable.setLevel(level);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTint(int tint) {
        setTintList(ColorStateList.valueOf(tint));
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintList(ColorStateList tint) {
        this.mState.mTint = tint;
        updateTint(getState());
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        this.mState.mTintMode = tintMode;
        updateTint(getState());
    }

    private boolean updateTint(int[] state) {
        if (!isCompatTintEnabled()) {
            return false;
        }
        ColorStateList tintList = this.mState.mTint;
        PorterDuff.Mode tintMode = this.mState.mTintMode;
        if (tintList == null || tintMode == null) {
            this.mColorFilterSet = false;
            clearColorFilter();
        } else {
            int color = tintList.getColorForState(state, tintList.getDefaultColor());
            if (!(this.mColorFilterSet && color == this.mCurrentColor && tintMode == this.mCurrentMode)) {
                setColorFilter(color, tintMode);
                this.mCurrentColor = color;
                this.mCurrentMode = tintMode;
                this.mColorFilterSet = true;
                return true;
            }
        }
        return false;
    }

    @Override // androidx.core.graphics.drawable.WrappedDrawable
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    @Override // androidx.core.graphics.drawable.WrappedDrawable
    public final void setWrappedDrawable(Drawable dr) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            setVisible(dr.isVisible(), true);
            setState(dr.getState());
            setLevel(dr.getLevel());
            setBounds(dr.getBounds());
            WrappedDrawableState wrappedDrawableState = this.mState;
            if (wrappedDrawableState != null) {
                wrappedDrawableState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    protected boolean isCompatTintEnabled() {
        return true;
    }
}
