package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class FloatingActionButtonImpl {
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final long ELEVATION_ANIM_DELAY = 100;
    static final long ELEVATION_ANIM_DURATION = 100;
    private static final float ELEVATION_MULTIPLIER = 0.75f;
    private static final float HIDE_ICON_SCALE = 0.0f;
    private static final float HIDE_OPACITY = 0.0f;
    private static final float HIDE_SCALE = 0.0f;
    private static final float OFFSET_MULTIPLIER = 0.25f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    private static final float SHOW_ICON_SCALE = 1.0f;
    private static final float SHOW_OPACITY = 1.0f;
    private static final float SHOW_SCALE = 1.0f;
    BorderDrawable borderDrawable;
    Drawable contentBackground;
    @Nullable
    Animator currentAnimator;
    @Nullable
    private MotionSpec defaultHideMotionSpec;
    @Nullable
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    private ArrayList<Animator.AnimatorListener> hideListeners;
    @Nullable
    MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    private InsetDrawable insetDrawable;
    int maxImageSize;
    int minTouchTargetSize;
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    final ShadowViewDelegate shadowViewDelegate;
    @Nullable
    ShapeAppearanceModel shapeAppearance;
    MaterialShapeDrawable shapeDrawable;
    private ArrayList<Animator.AnimatorListener> showListeners;
    @Nullable
    MotionSpec showMotionSpec;
    private ArrayList<InternalTransformationListener> transformationListeners;
    boolean usingDefaultCorner;
    final FloatingActionButton view;
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] PRESSED_ENABLED_STATE_SET = {16842919, 16842910};
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET = {16843623, 16842908, 16842910};
    static final int[] FOCUSED_ENABLED_STATE_SET = {16842908, 16842910};
    static final int[] HOVERED_ENABLED_STATE_SET = {16843623, 16842910};
    static final int[] ENABLED_STATE_SET = {16842910};
    static final int[] EMPTY_STATE_SET = new int[0];
    int animState = 0;
    float imageMatrixScale = 1.0f;
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    private final Matrix tmpMatrix = new Matrix();
    private final StateListAnimator stateListAnimator = new StateListAnimator();

    /* loaded from: classes2.dex */
    public interface InternalTransformationListener {
        void onScaleChanged();

        void onTranslationChanged();
    }

    /* loaded from: classes2.dex */
    public interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    public FloatingActionButtonImpl(FloatingActionButton view, ShadowViewDelegate shadowViewDelegate) {
        this.view = view;
        this.shadowViewDelegate = shadowViewDelegate;
        this.stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation()));
        this.stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation()));
        this.rotation = this.view.getRotation();
    }

    public void setBackgroundDrawable(ColorStateList backgroundTint, PorterDuff.Mode backgroundTintMode, ColorStateList rippleColor, int borderWidth) {
        this.shapeDrawable = createShapeDrawable();
        this.shapeDrawable.setTintList(backgroundTint);
        if (backgroundTintMode != null) {
            this.shapeDrawable.setTintMode(backgroundTintMode);
        }
        this.shapeDrawable.setShadowColor(-12303292);
        MaterialShapeDrawable touchFeedbackShape = createShapeDrawable();
        touchFeedbackShape.setTintList(RippleUtils.convertToRippleDrawableColor(rippleColor));
        this.rippleDrawable = touchFeedbackShape;
        Drawable[] layers = {this.shapeDrawable, this.rippleDrawable};
        this.contentBackground = new LayerDrawable(layers);
        this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
    }

    public void setBackgroundTintList(ColorStateList tint) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintList(tint);
        }
        BorderDrawable borderDrawable = this.borderDrawable;
        if (borderDrawable != null) {
            borderDrawable.setBorderTint(tint);
        }
    }

    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            DrawableCompat.setTintMode(materialShapeDrawable, tintMode);
        }
    }

    public void setMinTouchTargetSize(int minTouchTargetSize) {
        this.minTouchTargetSize = minTouchTargetSize;
    }

    public void setRippleColor(ColorStateList rippleColor) {
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, RippleUtils.convertToRippleDrawableColor(rippleColor));
        }
    }

    public final void setElevation(float elevation) {
        if (this.elevation != elevation) {
            this.elevation = elevation;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    public float getElevation() {
        return this.elevation;
    }

    public float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    public float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    public final void setHoveredFocusedTranslationZ(float translationZ) {
        if (this.hoveredFocusedTranslationZ != translationZ) {
            this.hoveredFocusedTranslationZ = translationZ;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    public final void setPressedTranslationZ(float translationZ) {
        if (this.pressedTranslationZ != translationZ) {
            this.pressedTranslationZ = translationZ;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    public final void setMaxImageSize(int maxImageSize) {
        if (this.maxImageSize != maxImageSize) {
            this.maxImageSize = maxImageSize;
            updateImageMatrixScale();
        }
    }

    public final void updateImageMatrixScale() {
        setImageMatrixScale(this.imageMatrixScale);
    }

    final void setImageMatrixScale(float scale) {
        this.imageMatrixScale = scale;
        Matrix matrix = this.tmpMatrix;
        calculateImageMatrixFromScale(scale, matrix);
        this.view.setImageMatrix(matrix);
    }

    private void calculateImageMatrixFromScale(float scale, Matrix matrix) {
        matrix.reset();
        Drawable drawable = this.view.getDrawable();
        if (drawable != null && this.maxImageSize != 0) {
            RectF drawableBounds = this.tmpRectF1;
            RectF imageBounds = this.tmpRectF2;
            drawableBounds.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            int i = this.maxImageSize;
            imageBounds.set(0.0f, 0.0f, i, i);
            matrix.setRectToRect(drawableBounds, imageBounds, Matrix.ScaleToFit.CENTER);
            int i2 = this.maxImageSize;
            matrix.postScale(scale, scale, i2 / 2.0f, i2 / 2.0f);
        }
    }

    public final void setShapeAppearance(ShapeAppearanceModel shapeAppearance, boolean usingDefaultCorner) {
        if (usingDefaultCorner) {
            shapeAppearance.setCornerRadius(this.view.getSizeDimension() / 2);
        }
        this.shapeAppearance = shapeAppearance;
        this.usingDefaultCorner = usingDefaultCorner;
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearance);
        }
        Drawable drawable = this.rippleDrawable;
        if (drawable instanceof MaterialShapeDrawable) {
            ((MaterialShapeDrawable) drawable).setShapeAppearanceModel(shapeAppearance);
        }
        BorderDrawable borderDrawable = this.borderDrawable;
        if (borderDrawable != null) {
            borderDrawable.setShapeAppearanceModel(shapeAppearance);
        }
    }

    @Nullable
    public final ShapeAppearanceModel getShapeAppearance() {
        return this.shapeAppearance;
    }

    @Nullable
    public final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    public final void setShowMotionSpec(@Nullable MotionSpec spec) {
        this.showMotionSpec = spec;
    }

    @Nullable
    public final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    public final void setHideMotionSpec(@Nullable MotionSpec spec) {
        this.hideMotionSpec = spec;
    }

    public final boolean isAccessible() {
        return this.view.getSizeDimension() >= this.minTouchTargetSize;
    }

    void onElevationsChanged(float elevation, float hoveredFocusedTranslationZ, float pressedTranslationZ) {
        updatePadding();
        updateShadow(elevation);
    }

    public void updateShadow(float elevation) {
        this.shapeDrawable.setElevation((float) Math.ceil(ELEVATION_MULTIPLIER * elevation));
        this.shapeDrawable.setShadowVerticalOffset((int) Math.ceil(OFFSET_MULTIPLIER * elevation));
    }

    public void onDrawableStateChanged(int[] state) {
        this.stateListAnimator.setState(state);
    }

    public void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
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

    public void hide(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeHidden()) {
            Animator animator = this.currentAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (shouldAnimateVisibilityChange()) {
                MotionSpec motionSpec = this.hideMotionSpec;
                if (motionSpec == null) {
                    motionSpec = getDefaultHideMotionSpec();
                }
                AnimatorSet set = createAnimator(motionSpec, 0.0f, 0.0f, 0.0f);
                set.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.1
                    private boolean cancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
                        FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
                        floatingActionButtonImpl.animState = 1;
                        floatingActionButtonImpl.currentAnimator = animation;
                        this.cancelled = false;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animation) {
                        this.cancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
                        floatingActionButtonImpl.animState = 0;
                        floatingActionButtonImpl.currentAnimator = null;
                        if (!this.cancelled) {
                            floatingActionButtonImpl.view.internalSetVisibility(fromUser ? 8 : 4, fromUser);
                            InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                            if (internalVisibilityChangedListener != null) {
                                internalVisibilityChangedListener.onHidden();
                            }
                        }
                    }
                });
                ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
                if (arrayList != null) {
                    Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                    while (it.hasNext()) {
                        Animator.AnimatorListener l = it.next();
                        set.addListener(l);
                    }
                }
                set.start();
                return;
            }
            this.view.internalSetVisibility(fromUser ? 8 : 4, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        }
    }

    public void show(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeShown()) {
            Animator animator = this.currentAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (shouldAnimateVisibilityChange()) {
                if (this.view.getVisibility() != 0) {
                    this.view.setAlpha(0.0f);
                    this.view.setScaleY(0.0f);
                    this.view.setScaleX(0.0f);
                    setImageMatrixScale(0.0f);
                }
                MotionSpec motionSpec = this.showMotionSpec;
                if (motionSpec == null) {
                    motionSpec = getDefaultShowMotionSpec();
                }
                AnimatorSet set = createAnimator(motionSpec, 1.0f, 1.0f, 1.0f);
                set.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
                        FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
                        floatingActionButtonImpl.animState = 2;
                        floatingActionButtonImpl.currentAnimator = animation;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
                        floatingActionButtonImpl.animState = 0;
                        floatingActionButtonImpl.currentAnimator = null;
                        InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onShown();
                        }
                    }
                });
                ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
                if (arrayList != null) {
                    Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                    while (it.hasNext()) {
                        Animator.AnimatorListener l = it.next();
                        set.addListener(l);
                    }
                }
                set.start();
                return;
            }
            this.view.internalSetVisibility(0, fromUser);
            this.view.setAlpha(1.0f);
            this.view.setScaleY(1.0f);
            this.view.setScaleX(1.0f);
            setImageMatrixScale(1.0f);
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec);
        }
        return this.defaultShowMotionSpec;
    }

    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec);
        }
        return this.defaultHideMotionSpec;
    }

    @NonNull
    private AnimatorSet createAnimator(@NonNull MotionSpec spec, float opacity, float scale, float iconScale) {
        List<Animator> animators = new ArrayList<>();
        Animator animator = ObjectAnimator.ofFloat(this.view, View.ALPHA, opacity);
        spec.getTiming("opacity").apply(animator);
        animators.add(animator);
        Animator animator2 = ObjectAnimator.ofFloat(this.view, View.SCALE_X, scale);
        spec.getTiming("scale").apply(animator2);
        animators.add(animator2);
        Animator animator3 = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, scale);
        spec.getTiming("scale").apply(animator3);
        animators.add(animator3);
        calculateImageMatrixFromScale(iconScale, this.tmpMatrix);
        Animator animator4 = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.3
            @Override // com.google.android.material.animation.MatrixEvaluator
            public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
                FloatingActionButtonImpl.this.imageMatrixScale = fraction;
                return super.evaluate(fraction, startValue, endValue);
            }
        }, new Matrix(this.tmpMatrix));
        spec.getTiming("iconScale").apply(animator4);
        animators.add(animator4);
        AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, animators);
        return set;
    }

    public void addTransformationListener(@NonNull InternalTransformationListener listener) {
        if (this.transformationListeners == null) {
            this.transformationListeners = new ArrayList<>();
        }
        this.transformationListeners.add(listener);
    }

    public void removeTransformationListener(@NonNull InternalTransformationListener listener) {
        ArrayList<InternalTransformationListener> arrayList = this.transformationListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void onTranslationChanged() {
        ArrayList<InternalTransformationListener> arrayList = this.transformationListeners;
        if (arrayList != null) {
            Iterator<InternalTransformationListener> it = arrayList.iterator();
            while (it.hasNext()) {
                InternalTransformationListener l = it.next();
                l.onTranslationChanged();
            }
        }
    }

    public void onScaleChanged() {
        ArrayList<InternalTransformationListener> arrayList = this.transformationListeners;
        if (arrayList != null) {
            Iterator<InternalTransformationListener> it = arrayList.iterator();
            while (it.hasNext()) {
                InternalTransformationListener l = it.next();
                l.onScaleChanged();
            }
        }
    }

    public final Drawable getContentBackground() {
        return this.contentBackground;
    }

    public void onCompatShadowChanged() {
    }

    public void updateSize() {
        if (this.usingDefaultCorner) {
            ShapeAppearanceModel shapeAppearanceModel = this.shapeDrawable.getShapeAppearanceModel();
            shapeAppearanceModel.setCornerRadius(this.view.getSizeDimension() / 2);
        }
    }

    public final void updatePadding() {
        Rect rect = this.tmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void getPadding(Rect rect) {
        int minPadding = (this.minTouchTargetSize - this.view.getSizeDimension()) / 2;
        float maxShadowSize = getElevation() + this.pressedTranslationZ;
        int hPadding = Math.max(minPadding, (int) Math.ceil(maxShadowSize));
        int vPadding = Math.max(minPadding, (int) Math.ceil(SHADOW_MULTIPLIER * maxShadowSize));
        rect.set(hPadding, vPadding, hPadding, vPadding);
    }

    void onPaddingUpdated(Rect padding) {
        if (shouldAddPadding()) {
            this.insetDrawable = new InsetDrawable(this.contentBackground, padding.left, padding.top, padding.right, padding.bottom);
            this.shadowViewDelegate.setBackgroundDrawable(this.insetDrawable);
            return;
        }
        this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
    }

    boolean shouldAddPadding() {
        return true;
    }

    public void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }

    public void onDetachedFromWindow() {
        if (this.preDrawListener != null) {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
            this.preDrawListener = null;
        }
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void onPreDraw() {
        float rotation = this.view.getRotation();
        if (this.rotation != rotation) {
            this.rotation = rotation;
            updateFromViewRotation();
        }
    }

    private void ensurePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.4
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }

    MaterialShapeDrawable createShapeDrawable() {
        if (this.usingDefaultCorner) {
            this.shapeAppearance.setCornerRadius(this.view.getSizeDimension() / 2);
        }
        return new MaterialShapeDrawable(this.shapeAppearance);
    }

    public boolean isOrWillBeShown() {
        return this.view.getVisibility() != 0 ? this.animState == 2 : this.animState != 1;
    }

    public boolean isOrWillBeHidden() {
        return this.view.getVisibility() == 0 ? this.animState == 1 : this.animState != 2;
    }

    private ValueAnimator createElevationAnimator(@NonNull ShadowAnimatorImpl impl) {
        ValueAnimator animator = new ValueAnimator();
        animator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        animator.setDuration(100L);
        animator.addListener(impl);
        animator.addUpdateListener(impl);
        animator.setFloatValues(0.0f, 1.0f);
        return animator;
    }

    /* loaded from: classes2.dex */
    public abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        private boolean validValues;

        protected abstract float getTargetShadowSize();

        private ShadowAnimatorImpl() {
            FloatingActionButtonImpl.this = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animator) {
            if (!this.validValues) {
                this.shadowSizeStart = FloatingActionButtonImpl.this.shapeDrawable.getElevation();
                this.shadowSizeEnd = getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            float f = this.shadowSizeStart;
            floatingActionButtonImpl.updateShadow((int) (f + ((this.shadowSizeEnd - f) * animator.getAnimatedFraction())));
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.updateShadow((int) this.shadowSizeEnd);
            this.validValues = false;
        }
    }

    /* loaded from: classes2.dex */
    public class ResetElevationAnimation extends ShadowAnimatorImpl {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ResetElevationAnimation() {
            super();
            FloatingActionButtonImpl.this = r2;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation;
        }
    }

    /* loaded from: classes2.dex */
    public class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ElevateToHoveredFocusedTranslationZAnimation() {
            super();
            FloatingActionButtonImpl.this = r2;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
        }
    }

    /* loaded from: classes2.dex */
    public class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ElevateToPressedTranslationZAnimation() {
            super();
            FloatingActionButtonImpl.this = r2;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
        }
    }

    /* loaded from: classes2.dex */
    public class DisabledElevationAnimation extends ShadowAnimatorImpl {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        DisabledElevationAnimation() {
            super();
            FloatingActionButtonImpl.this = r2;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode();
    }

    void updateFromViewRotation() {
        if (Build.VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, null);
            }
        }
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShadowCompatRotation((int) this.rotation);
        }
    }
}
