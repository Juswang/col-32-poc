package com.google.android.material.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class MaterialButtonHelper {
    private static final boolean IS_LOLLIPOP;
    @Nullable
    private ColorStateList backgroundTint;
    @Nullable
    private PorterDuff.Mode backgroundTintMode;
    private boolean checkable;
    private int cornerRadius;
    private int insetBottom;
    private int insetLeft;
    private int insetRight;
    private int insetTop;
    @Nullable
    private MaterialShapeDrawable maskDrawable;
    private final MaterialButton materialButton;
    @Nullable
    private ColorStateList rippleColor;
    private LayerDrawable rippleDrawable;
    private final ShapeAppearanceModel shapeAppearanceModel;
    @Nullable
    private ColorStateList strokeColor;
    private int strokeWidth;
    private boolean shouldDrawSurfaceColorStroke = false;
    private boolean backgroundOverwritten = false;
    private boolean cornerRadiusSet = false;

    static {
        IS_LOLLIPOP = Build.VERSION.SDK_INT >= 21;
    }

    public MaterialButtonHelper(MaterialButton button, ShapeAppearanceModel shapeAppearanceModel) {
        this.materialButton = button;
        this.shapeAppearanceModel = shapeAppearanceModel;
    }

    public void loadFromAttributes(TypedArray attributes) {
        this.insetLeft = attributes.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetLeft, 0);
        this.insetRight = attributes.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
        this.insetTop = attributes.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
        this.insetBottom = attributes.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
        if (attributes.hasValue(R.styleable.MaterialButton_cornerRadius)) {
            this.cornerRadius = attributes.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, -1);
            this.shapeAppearanceModel.setCornerRadius(this.cornerRadius);
            this.cornerRadiusSet = true;
        }
        this.strokeWidth = attributes.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
        this.backgroundTintMode = ViewUtils.parseTintMode(attributes.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), attributes, R.styleable.MaterialButton_backgroundTint);
        this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), attributes, R.styleable.MaterialButton_strokeColor);
        this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), attributes, R.styleable.MaterialButton_rippleColor);
        this.checkable = attributes.getBoolean(R.styleable.MaterialButton_android_checkable, false);
        int elevation = attributes.getDimensionPixelSize(R.styleable.MaterialButton_elevation, 0);
        int paddingStart = ViewCompat.getPaddingStart(this.materialButton);
        int paddingTop = this.materialButton.getPaddingTop();
        int paddingEnd = ViewCompat.getPaddingEnd(this.materialButton);
        int paddingBottom = this.materialButton.getPaddingBottom();
        this.materialButton.setInternalBackground(createBackground());
        MaterialShapeDrawable materialShapeDrawable = getMaterialShapeDrawable();
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setElevation(elevation);
        }
        ViewCompat.setPaddingRelative(this.materialButton, this.insetLeft + paddingStart, this.insetTop + paddingTop, this.insetRight + paddingEnd, this.insetBottom + paddingBottom);
    }

    public void setBackgroundOverwritten() {
        this.backgroundOverwritten = true;
        this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
        this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
    }

    public boolean isBackgroundOverwritten() {
        return this.backgroundOverwritten;
    }

    private InsetDrawable wrapDrawableWithInset(Drawable drawable) {
        return new InsetDrawable(drawable, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
    }

    public void setSupportBackgroundTintList(@Nullable ColorStateList tintList) {
        if (this.backgroundTint != tintList) {
            this.backgroundTint = tintList;
            if (getMaterialShapeDrawable() != null) {
                DrawableCompat.setTintList(getMaterialShapeDrawable(), this.backgroundTint);
            }
        }
    }

    public ColorStateList getSupportBackgroundTintList() {
        return this.backgroundTint;
    }

    public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            if (getMaterialShapeDrawable() != null && this.backgroundTintMode != null) {
                DrawableCompat.setTintMode(getMaterialShapeDrawable(), this.backgroundTintMode);
            }
        }
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    public void setShouldDrawSurfaceColorStroke(boolean shouldDrawSurfaceColorStroke) {
        this.shouldDrawSurfaceColorStroke = shouldDrawSurfaceColorStroke;
        updateStroke();
    }

    private Drawable createBackground() {
        MaterialShapeDrawable backgroundDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
        DrawableCompat.setTintList(backgroundDrawable, this.backgroundTint);
        PorterDuff.Mode mode = this.backgroundTintMode;
        if (mode != null) {
            DrawableCompat.setTintMode(backgroundDrawable, mode);
        }
        backgroundDrawable.setStroke(this.strokeWidth, this.strokeColor);
        MaterialShapeDrawable surfaceColorStrokeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
        surfaceColorStrokeDrawable.setTint(0);
        surfaceColorStrokeDrawable.setStroke(this.strokeWidth, this.shouldDrawSurfaceColorStroke ? MaterialColors.getColor(this.materialButton, R.attr.colorSurface) : 0);
        this.maskDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
        if (IS_LOLLIPOP) {
            if (this.strokeWidth > 0) {
                ShapeAppearanceModel temporaryAdjustedShapeAppearanceModel = new ShapeAppearanceModel(this.shapeAppearanceModel);
                adjustShapeAppearanceModelCornerRadius(temporaryAdjustedShapeAppearanceModel, this.strokeWidth / 2.0f);
                backgroundDrawable.setShapeAppearanceModel(temporaryAdjustedShapeAppearanceModel);
                surfaceColorStrokeDrawable.setShapeAppearanceModel(temporaryAdjustedShapeAppearanceModel);
                this.maskDrawable.setShapeAppearanceModel(temporaryAdjustedShapeAppearanceModel);
            }
            DrawableCompat.setTint(this.maskDrawable, -1);
            this.rippleDrawable = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(this.rippleColor), wrapDrawableWithInset(new LayerDrawable(new Drawable[]{surfaceColorStrokeDrawable, backgroundDrawable})), this.maskDrawable);
            return this.rippleDrawable;
        }
        DrawableCompat.setTintList(this.maskDrawable, RippleUtils.convertToRippleDrawableColor(this.rippleColor));
        this.rippleDrawable = new LayerDrawable(new Drawable[]{surfaceColorStrokeDrawable, backgroundDrawable, this.maskDrawable});
        return wrapDrawableWithInset(this.rippleDrawable);
    }

    public void updateMaskBounds(int height, int width) {
        MaterialShapeDrawable materialShapeDrawable = this.maskDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setBounds(this.insetLeft, this.insetTop, width - this.insetRight, height - this.insetBottom);
        }
    }

    public void setBackgroundColor(int color) {
        if (getMaterialShapeDrawable() != null) {
            getMaterialShapeDrawable().setTint(color);
        }
    }

    public void setRippleColor(@Nullable ColorStateList rippleColor) {
        if (this.rippleColor != rippleColor) {
            this.rippleColor = rippleColor;
            if (IS_LOLLIPOP && (this.materialButton.getBackground() instanceof RippleDrawable)) {
                ((RippleDrawable) this.materialButton.getBackground()).setColor(RippleUtils.convertToRippleDrawableColor(rippleColor));
            } else if (!IS_LOLLIPOP && getMaskDrawable() != null) {
                DrawableCompat.setTintList(getMaskDrawable(), RippleUtils.convertToRippleDrawableColor(rippleColor));
            }
        }
    }

    @Nullable
    public ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    public void setStrokeColor(@Nullable ColorStateList strokeColor) {
        if (this.strokeColor != strokeColor) {
            this.strokeColor = strokeColor;
            updateStroke();
        }
    }

    @Nullable
    public ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeWidth(int strokeWidth) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth;
            updateStroke();
        }
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    private void updateStroke() {
        MaterialShapeDrawable materialShapeDrawable = getMaterialShapeDrawable();
        MaterialShapeDrawable surfaceColorStrokeDrawable = getSurfaceColorStrokeDrawable();
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setStroke(this.strokeWidth, this.strokeColor);
            if (surfaceColorStrokeDrawable != null) {
                surfaceColorStrokeDrawable.setStroke(this.strokeWidth, this.shouldDrawSurfaceColorStroke ? MaterialColors.getColor(this.materialButton, R.attr.colorSurface) : 0);
            }
            if (IS_LOLLIPOP) {
                ShapeAppearanceModel temporaryShapeAppearance = new ShapeAppearanceModel(this.shapeAppearanceModel);
                adjustShapeAppearanceModelCornerRadius(temporaryShapeAppearance, this.strokeWidth / 2.0f);
                materialShapeDrawable.setShapeAppearanceModel(temporaryShapeAppearance);
                if (surfaceColorStrokeDrawable != null) {
                    surfaceColorStrokeDrawable.setShapeAppearanceModel(temporaryShapeAppearance);
                }
                if (getMaskDrawable() != null) {
                    getMaskDrawable().setShapeAppearanceModel(temporaryShapeAppearance);
                }
                MaterialShapeDrawable materialShapeDrawable2 = this.maskDrawable;
                if (materialShapeDrawable2 != null) {
                    materialShapeDrawable2.setShapeAppearanceModel(temporaryShapeAppearance);
                }
            }
        }
    }

    public void setCornerRadius(int cornerRadius) {
        if (!this.cornerRadiusSet || this.cornerRadius != cornerRadius) {
            this.cornerRadius = cornerRadius;
            this.cornerRadiusSet = true;
            this.shapeAppearanceModel.setCornerRadius(cornerRadius + (this.strokeWidth / 2.0f));
            if (getMaterialShapeDrawable() != null) {
                getMaterialShapeDrawable().setShapeAppearanceModel(this.shapeAppearanceModel);
            }
            if (getSurfaceColorStrokeDrawable() != null) {
                getSurfaceColorStrokeDrawable().setShapeAppearanceModel(this.shapeAppearanceModel);
            }
            if (getMaskDrawable() != null) {
                getMaskDrawable().setShapeAppearanceModel(this.shapeAppearanceModel);
            }
        }
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    private void adjustShapeAppearanceModelCornerRadius(ShapeAppearanceModel shapeAppearanceModel, float cornerRadiusAdjustment) {
        shapeAppearanceModel.getTopLeftCorner().setCornerSize(shapeAppearanceModel.getTopLeftCorner().getCornerSize() + cornerRadiusAdjustment);
        shapeAppearanceModel.getTopRightCorner().setCornerSize(shapeAppearanceModel.getTopRightCorner().getCornerSize() + cornerRadiusAdjustment);
        shapeAppearanceModel.getBottomRightCorner().setCornerSize(shapeAppearanceModel.getBottomRightCorner().getCornerSize() + cornerRadiusAdjustment);
        shapeAppearanceModel.getBottomLeftCorner().setCornerSize(shapeAppearanceModel.getBottomLeftCorner().getCornerSize() + cornerRadiusAdjustment);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Nullable
    private MaterialShapeDrawable getMaterialShapeDrawable(boolean getSurfaceColorStrokeDrawable) {
        LayerDrawable layerDrawable = this.rippleDrawable;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 0) {
            return null;
        }
        if (!IS_LOLLIPOP) {
            return (MaterialShapeDrawable) this.rippleDrawable.getDrawable(!getSurfaceColorStrokeDrawable ? 1 : 0);
        }
        InsetDrawable insetDrawable = (InsetDrawable) this.rippleDrawable.getDrawable(0);
        LayerDrawable layerDrawable2 = (LayerDrawable) insetDrawable.getDrawable();
        return (MaterialShapeDrawable) layerDrawable2.getDrawable(!getSurfaceColorStrokeDrawable);
    }

    @Nullable
    MaterialShapeDrawable getMaterialShapeDrawable() {
        return getMaterialShapeDrawable(false);
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isCheckable() {
        return this.checkable;
    }

    @Nullable
    private MaterialShapeDrawable getSurfaceColorStrokeDrawable() {
        return getMaterialShapeDrawable(true);
    }

    @Nullable
    public MaterialShapeDrawable getMaskDrawable() {
        LayerDrawable layerDrawable = this.rippleDrawable;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 1) {
            return null;
        }
        if (this.rippleDrawable.getNumberOfLayers() > 2) {
            return (MaterialShapeDrawable) this.rippleDrawable.getDrawable(2);
        }
        return (MaterialShapeDrawable) this.rippleDrawable.getDrawable(1);
    }
}
