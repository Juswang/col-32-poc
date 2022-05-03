package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.DescendantOffsetUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.AttachedBehavior {
    private static final int ANIM_STATE_HIDING = 1;
    private static final int ANIM_STATE_NONE = 0;
    private static final int ANIM_STATE_SHOWING = 2;
    private static final long COLLAPSE_RESIZE_ANIMATION_DURATION_MS = 200;
    private static final long HIDE_ANIMATION_DURATION_MS = 75;
    private static final long SHOW_ANIMATION_DURATION_MS = 150;
    private static final float SHOW_ANIMATION_SCALE_FROM = 0.8f;
    private int animState;
    private final CoordinatorLayout.Behavior<ExtendedFloatingActionButton> behavior;
    @Nullable
    private Animator currentCollapseExpandAnimator;
    @Nullable
    private Animator currentShowHideAnimator;
    @Nullable
    private ArrayList<Animator.AnimatorListener> extendListeners;
    @Nullable
    private ArrayList<Animator.AnimatorListener> hideListeners;
    private boolean isExtended;
    private final Rect shadowPadding;
    @Nullable
    private ArrayList<Animator.AnimatorListener> showListeners;
    @Nullable
    private ArrayList<Animator.AnimatorListener> shrinkListeners;
    private int userSetVisibility;

    /* loaded from: classes2.dex */
    public static abstract class OnChangedListener {
        public void onShown(ExtendedFloatingActionButton extendedFab) {
        }

        public void onHidden(ExtendedFloatingActionButton extendedFab) {
        }

        public void onExtended(ExtendedFloatingActionButton extendedFab) {
        }

        public void onShrunken(ExtendedFloatingActionButton extendedFab) {
        }
    }

    public ExtendedFloatingActionButton(Context context) {
        this(context, null);
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.extendedFloatingActionButtonStyle);
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.shadowPadding = new Rect();
        this.animState = 0;
        this.isExtended = true;
        this.behavior = new ExtendedFloatingActionButtonBehavior(context, attrs);
        this.userSetVisibility = getVisibility();
        setHorizontallyScrolling(true);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isExtended && TextUtils.isEmpty(getText()) && getIcon() != null) {
            this.isExtended = false;
            shrinkNow();
        }
    }

    @Override // com.google.android.material.button.MaterialButton, android.widget.TextView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setCornerRadius(getAdjustedRadius(getMeasuredHeight()));
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    @NonNull
    public CoordinatorLayout.Behavior<ExtendedFloatingActionButton> getBehavior() {
        return this.behavior;
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        internalSetVisibility(visibility, true);
    }

    public void internalSetVisibility(int visibility, boolean fromUser) {
        super.setVisibility(visibility);
        if (fromUser) {
            this.userSetVisibility = visibility;
        }
    }

    public final int getUserSetVisibility() {
        return this.userSetVisibility;
    }

    public void addOnShowAnimationListener(@NonNull Animator.AnimatorListener listener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList<>();
        }
        this.showListeners.add(listener);
    }

    public void removeOnShowAnimationListener(@NonNull Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void addOnHideAnimationListener(@NonNull Animator.AnimatorListener listener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList<>();
        }
        this.hideListeners.add(listener);
    }

    public void removeOnHideAnimationListener(@NonNull Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void addOnShrinkAnimationListener(@NonNull Animator.AnimatorListener listener) {
        if (this.shrinkListeners == null) {
            this.shrinkListeners = new ArrayList<>();
        }
        this.shrinkListeners.add(listener);
    }

    public void removeOnShrinkAnimationListener(@NonNull Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.shrinkListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void addOnExtendAnimationListener(@NonNull Animator.AnimatorListener listener) {
        if (this.extendListeners == null) {
            this.extendListeners = new ArrayList<>();
        }
        this.extendListeners.add(listener);
    }

    public void removeOnExtendAnimationListener(@NonNull Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.extendListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animate) {
        hide(true, animate, null);
    }

    public void hide(@Nullable OnChangedListener listener) {
        hide(true, true, listener);
    }

    public void hide(final boolean fromUser, boolean animate, @Nullable final OnChangedListener listener) {
        if (!isOrWillBeHidden()) {
            Animator animator = this.currentShowHideAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (!animate || !shouldAnimateVisibilityChange()) {
                internalSetVisibility(fromUser ? 8 : 4, fromUser);
                if (listener != null) {
                    listener.onHidden(this);
                    return;
                }
                return;
            }
            Animator hideAnimation = createHideAnimation();
            hideAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.1
                private boolean cancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    ExtendedFloatingActionButton.this.internalSetVisibility(0, fromUser);
                    ExtendedFloatingActionButton.this.animState = 1;
                    ExtendedFloatingActionButton.this.currentShowHideAnimator = animation;
                    this.cancelled = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    this.cancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ExtendedFloatingActionButton.this.animState = 0;
                    ExtendedFloatingActionButton.this.currentShowHideAnimator = null;
                    if (!this.cancelled) {
                        ExtendedFloatingActionButton.this.internalSetVisibility(fromUser ? 8 : 4, fromUser);
                        OnChangedListener onChangedListener = listener;
                        if (onChangedListener != null) {
                            onChangedListener.onHidden(ExtendedFloatingActionButton.this);
                        }
                    }
                }
            });
            ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
            if (arrayList != null) {
                Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    Animator.AnimatorListener l = it.next();
                    hideAnimation.addListener(l);
                }
            }
            hideAnimation.start();
        }
    }

    public void show() {
        show(true);
    }

    public void show(boolean animate) {
        show(true, animate, null);
    }

    public void show(@Nullable OnChangedListener listener) {
        show(true, true, listener);
    }

    public void show(final boolean fromUser, boolean animate, @Nullable final OnChangedListener listener) {
        if (!isOrWillBeShown()) {
            Animator animator = this.currentShowHideAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (!animate || !shouldAnimateVisibilityChange()) {
                internalSetVisibility(0, fromUser);
                setAlpha(1.0f);
                setScaleY(1.0f);
                setScaleX(1.0f);
                if (listener != null) {
                    listener.onShown(this);
                    return;
                }
                return;
            }
            Animator showAnimation = createShowAnimation();
            showAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    ExtendedFloatingActionButton.this.internalSetVisibility(0, fromUser);
                    ExtendedFloatingActionButton.this.animState = 2;
                    ExtendedFloatingActionButton.this.currentShowHideAnimator = animation;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ExtendedFloatingActionButton.this.animState = 0;
                    ExtendedFloatingActionButton.this.currentShowHideAnimator = null;
                    OnChangedListener onChangedListener = listener;
                    if (onChangedListener != null) {
                        onChangedListener.onShown(ExtendedFloatingActionButton.this);
                    }
                }
            });
            ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
            if (arrayList != null) {
                Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    Animator.AnimatorListener l = it.next();
                    showAnimation.addListener(l);
                }
            }
            showAnimation.start();
        }
    }

    public void extend() {
        extend(true);
    }

    public void extend(boolean animate) {
        setExtended(true, animate, null);
    }

    public void extend(@Nullable OnChangedListener listener) {
        setExtended(true, true, listener);
    }

    public void shrink() {
        shrink(true);
    }

    public void shrink(boolean animate) {
        setExtended(false, animate, null);
    }

    public void shrink(@Nullable OnChangedListener listener) {
        setExtended(false, true, listener);
    }

    private void setExtended(final boolean extended, boolean animate, @Nullable final OnChangedListener listener) {
        if (extended != this.isExtended && getIcon() != null && !TextUtils.isEmpty(getText())) {
            this.isExtended = extended;
            Animator animator = this.currentCollapseExpandAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (animate && shouldAnimateVisibilityChange()) {
                measure(0, 0);
                Animator collapseExpandAnimator = this.isExtended ? createExtendAnimation() : createShrinkAnimation();
                collapseExpandAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.3
                    private boolean cancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animation) {
                        ExtendedFloatingActionButton.this.currentCollapseExpandAnimator = animation;
                        this.cancelled = false;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animation) {
                        this.cancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        OnChangedListener onChangedListener;
                        ExtendedFloatingActionButton.this.currentCollapseExpandAnimator = null;
                        if (!this.cancelled && (onChangedListener = listener) != null) {
                            if (extended) {
                                onChangedListener.onExtended(ExtendedFloatingActionButton.this);
                            } else {
                                onChangedListener.onShrunken(ExtendedFloatingActionButton.this);
                            }
                        }
                    }
                });
                ArrayList<Animator.AnimatorListener> listeners = extended ? this.extendListeners : this.shrinkListeners;
                if (listeners != null) {
                    Iterator<Animator.AnimatorListener> it = listeners.iterator();
                    while (it.hasNext()) {
                        Animator.AnimatorListener l = it.next();
                        collapseExpandAnimator.addListener(l);
                    }
                }
                collapseExpandAnimator.start();
            } else if (extended) {
                extendNow();
                if (listener != null) {
                    listener.onExtended(this);
                }
            } else {
                shrinkNow();
                if (listener != null) {
                    listener.onShrunken(this);
                }
            }
        }
    }

    private Animator createShowAnimation() {
        List<Animator> animators = new ArrayList<>();
        Animator animator = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f);
        animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animators.add(animator);
        Animator animator2 = ObjectAnimator.ofFloat(this, View.SCALE_X, SHOW_ANIMATION_SCALE_FROM, 1.0f);
        animator2.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        animators.add(animator2);
        Animator animator3 = ObjectAnimator.ofFloat(this, View.SCALE_Y, SHOW_ANIMATION_SCALE_FROM, 1.0f);
        animator3.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        animators.add(animator3);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(150L);
        AnimatorSetCompat.playTogether(set, animators);
        return set;
    }

    private Animator createHideAnimation() {
        Animator animator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f);
        animator.setDuration(75L);
        animator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        return animator;
    }

    private Animator createShrinkAnimation() {
        List<Animator> animators = new ArrayList<>();
        int collapsedSize = (ViewCompat.getPaddingStart(this) * 2) + getIconSize();
        ValueAnimator animator = ValueAnimator.ofInt(getMeasuredWidth(), collapsedSize);
        animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.getLayoutParams().width = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ExtendedFloatingActionButton.this.requestLayout();
            }
        });
        animators.add(animator);
        ValueAnimator animator2 = ValueAnimator.ofInt(getMeasuredHeight(), collapsedSize);
        animator2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator2.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ExtendedFloatingActionButton.this.requestLayout();
            }
        });
        animators.add(animator2);
        ValueAnimator animator3 = ValueAnimator.ofInt(getCornerRadius(), getAdjustedRadius(collapsedSize));
        animator3.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator3.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.setCornerRadius(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        animators.add(animator3);
        AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, animators);
        return set;
    }

    private Animator createExtendAnimation() {
        List<Animator> animators = new ArrayList<>();
        ValueAnimator animator = ValueAnimator.ofInt(getWidth(), getMeasuredWidth());
        animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.getLayoutParams().width = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ExtendedFloatingActionButton.this.requestLayout();
            }
        });
        animators.add(animator);
        ValueAnimator animator2 = ValueAnimator.ofInt(getHeight(), getMeasuredHeight());
        animator2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator2.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.getLayoutParams().height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ExtendedFloatingActionButton.this.requestLayout();
            }
        });
        animators.add(animator2);
        ValueAnimator animator3 = ValueAnimator.ofInt(getCornerRadius(), getAdjustedRadius(getHeight()));
        animator3.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator3.setDuration(COLLAPSE_RESIZE_ANIMATION_DURATION_MS);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ExtendedFloatingActionButton.this.setCornerRadius(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        animators.add(animator3);
        AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, animators);
        return set;
    }

    private boolean isOrWillBeShown() {
        return getVisibility() != 0 ? this.animState == 2 : this.animState != 1;
    }

    private boolean isOrWillBeHidden() {
        return getVisibility() == 0 ? this.animState == 1 : this.animState != 2;
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this) && !isInEditMode();
    }

    private void shrinkNow() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            int collapsedSize = (ViewCompat.getPaddingStart(this) * 2) + getIconSize();
            layoutParams.width = collapsedSize;
            layoutParams.height = collapsedSize;
            requestLayout();
        }
    }

    private void extendNow() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            measure(0, 0);
            layoutParams.width = getMeasuredWidth();
            layoutParams.height = getMeasuredHeight();
            requestLayout();
        }
    }

    private int getAdjustedRadius(int value) {
        return (value - 1) / 2;
    }

    /* loaded from: classes2.dex */
    public static class ExtendedFloatingActionButtonBehavior<T extends ExtendedFloatingActionButton> extends CoordinatorLayout.Behavior<T> {
        private static final boolean AUTO_HIDE_DEFAULT = false;
        private static final boolean AUTO_SHRINK_DEFAULT = true;
        private boolean autoHideEnabled;
        private boolean autoShrinkEnabled;
        @Nullable
        private OnChangedListener internalAutoHideListener;
        @Nullable
        private OnChangedListener internalAutoShrinkListener;
        private Rect tmpRect;

        public ExtendedFloatingActionButtonBehavior() {
            this.autoHideEnabled = false;
            this.autoShrinkEnabled = AUTO_SHRINK_DEFAULT;
        }

        public ExtendedFloatingActionButtonBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedFloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = a.getBoolean(R.styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoHide, false);
            this.autoShrinkEnabled = a.getBoolean(R.styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoShrink, AUTO_SHRINK_DEFAULT);
            a.recycle();
        }

        public void setAutoHideEnabled(boolean autoHide) {
            this.autoHideEnabled = autoHide;
        }

        public boolean isAutoHideEnabled() {
            return this.autoHideEnabled;
        }

        public void setAutoShrinkEnabled(boolean autoShrink) {
            this.autoShrinkEnabled = autoShrink;
        }

        public boolean isAutoShrinkEnabled() {
            return this.autoShrinkEnabled;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams lp) {
            if (lp.dodgeInsetEdges == 0) {
                lp.dodgeInsetEdges = 80;
            }
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, ExtendedFloatingActionButton child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child);
                return false;
            } else if (!isBottomSheet(dependency)) {
                return false;
            } else {
                updateFabVisibilityForBottomSheet(dependency, child);
                return false;
            }
        }

        private static boolean isBottomSheet(@NonNull View view) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                return ((CoordinatorLayout.LayoutParams) lp).getBehavior() instanceof BottomSheetBehavior;
            }
            return false;
        }

        @VisibleForTesting
        public void setInternalAutoHideListener(@Nullable OnChangedListener listener) {
            this.internalAutoHideListener = listener;
        }

        @VisibleForTesting
        public void setInternalAutoShrinkListener(@Nullable OnChangedListener listener) {
            this.internalAutoShrinkListener = listener;
        }

        private boolean shouldUpdateVisibility(View dependency, ExtendedFloatingActionButton child) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if ((this.autoHideEnabled || this.autoShrinkEnabled) && lp.getAnchorId() == dependency.getId() && child.getUserSetVisibility() == 0) {
                return AUTO_SHRINK_DEFAULT;
            }
            return false;
        }

        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent, AppBarLayout appBarLayout, ExtendedFloatingActionButton child) {
            if (!shouldUpdateVisibility(appBarLayout, child)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                shrinkOrHide(child);
                return AUTO_SHRINK_DEFAULT;
            }
            extendOrShow(child);
            return AUTO_SHRINK_DEFAULT;
        }

        private boolean updateFabVisibilityForBottomSheet(View bottomSheet, ExtendedFloatingActionButton child) {
            if (!shouldUpdateVisibility(bottomSheet, child)) {
                return false;
            }
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (bottomSheet.getTop() < (child.getHeight() / 2) + lp.topMargin) {
                shrinkOrHide(child);
                return AUTO_SHRINK_DEFAULT;
            }
            extendOrShow(child);
            return AUTO_SHRINK_DEFAULT;
        }

        protected void shrinkOrHide(@NonNull ExtendedFloatingActionButton fab) {
            if (this.autoShrinkEnabled) {
                fab.shrink(this.internalAutoShrinkListener);
            } else if (this.autoHideEnabled) {
                fab.hide(false, AUTO_SHRINK_DEFAULT, this.internalAutoHideListener);
            }
        }

        protected void extendOrShow(@NonNull ExtendedFloatingActionButton fab) {
            if (this.autoShrinkEnabled) {
                fab.extend(this.internalAutoShrinkListener);
            } else if (this.autoHideEnabled) {
                fab.show(false, AUTO_SHRINK_DEFAULT, this.internalAutoHideListener);
            }
        }

        public boolean onLayoutChild(CoordinatorLayout parent, ExtendedFloatingActionButton child, int layoutDirection) {
            List<View> dependencies = parent.getDependencies(child);
            int count = dependencies.size();
            for (int i = 0; i < count; i++) {
                View dependency = dependencies.get(i);
                if (!(dependency instanceof AppBarLayout)) {
                    if (isBottomSheet(dependency) && updateFabVisibilityForBottomSheet(dependency, child)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child)) {
                    break;
                }
            }
            parent.onLayoutChild(child, layoutDirection);
            offsetIfNeeded(parent, child);
            return AUTO_SHRINK_DEFAULT;
        }

        public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull ExtendedFloatingActionButton child, @NonNull Rect rect) {
            Rect shadowPadding = child.shadowPadding;
            rect.set(child.getLeft() + shadowPadding.left, child.getTop() + shadowPadding.top, child.getRight() - shadowPadding.right, child.getBottom() - shadowPadding.bottom);
            return AUTO_SHRINK_DEFAULT;
        }

        private void offsetIfNeeded(CoordinatorLayout parent, ExtendedFloatingActionButton fab) {
            Rect padding = fab.shadowPadding;
            if (padding != null && padding.centerX() > 0 && padding.centerY() > 0) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                int offsetTB = 0;
                int offsetLR = 0;
                if (fab.getRight() >= parent.getWidth() - lp.rightMargin) {
                    offsetLR = padding.right;
                } else if (fab.getLeft() <= lp.leftMargin) {
                    offsetLR = -padding.left;
                }
                if (fab.getBottom() >= parent.getHeight() - lp.bottomMargin) {
                    offsetTB = padding.bottom;
                } else if (fab.getTop() <= lp.topMargin) {
                    offsetTB = -padding.top;
                }
                if (offsetTB != 0) {
                    ViewCompat.offsetTopAndBottom(fab, offsetTB);
                }
                if (offsetLR != 0) {
                    ViewCompat.offsetLeftAndRight(fab, offsetLR);
                }
            }
        }
    }
}
