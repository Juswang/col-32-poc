package com.google.android.material.card;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class MaterialCardViewHelper {
    private static final float CARD_VIEW_SHADOW_MULTIPLIER = 1.5f;
    private static final int CHECKED_ICON_LAYER_INDEX = 2;
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    private static final int DEFAULT_STROKE_VALUE = -1;
    private static final float SHADOW_OFFSET_MULTIPLIER = 0.25f;
    private static final float SHADOW_RADIUS_MULTIPLIER = 0.75f;
    private final MaterialShapeDrawable bgDrawable;
    private boolean checkable;
    private Drawable checkedIcon;
    private ColorStateList checkedIconTint;
    @Nullable
    private LayerDrawable clickableForegroundDrawable;
    @Nullable
    private MaterialShapeDrawable compatRippleDrawable;
    private final MaterialShapeDrawable drawableInsetByStroke;
    private Drawable fgDrawable;
    private final MaterialShapeDrawable foregroundContentDrawable;
    private final MaterialCardView materialCardView;
    private ColorStateList rippleColor;
    @Nullable
    private Drawable rippleDrawable;
    private final ShapeAppearanceModel shapeAppearanceModel;
    private final ShapeAppearanceModel shapeAppearanceModelInsetByStroke;
    @ColorInt
    private int strokeColor;
    @Dimension
    private int strokeWidth;
    private final Rect userContentPadding = new Rect();
    private final Rect temporaryBounds = new Rect();
    private boolean isBackgroundOverwritten = false;

    public MaterialCardViewHelper(MaterialCardView card, AttributeSet attrs, int defStyleAttr, @StyleRes int defStyleRes) {
        this.materialCardView = card;
        this.bgDrawable = new MaterialShapeDrawable(card.getContext(), attrs, defStyleAttr, defStyleRes);
        this.shapeAppearanceModel = this.bgDrawable.getShapeAppearanceModel();
        this.bgDrawable.setShadowColor(-12303292);
        this.foregroundContentDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
        TypedArray cardViewAttributes = card.getContext().obtainStyledAttributes(attrs, R.styleable.CardView, defStyleAttr, R.style.CardView);
        if (cardViewAttributes.hasValue(R.styleable.CardView_cardCornerRadius)) {
            this.shapeAppearanceModel.setCornerRadius(cardViewAttributes.getDimension(R.styleable.CardView_cardCornerRadius, 0.0f));
        }
        this.shapeAppearanceModelInsetByStroke = new ShapeAppearanceModel(this.shapeAppearanceModel);
        this.drawableInsetByStroke = new MaterialShapeDrawable(this.shapeAppearanceModelInsetByStroke);
    }

    public void loadFromAttributes(TypedArray attributes) {
        this.strokeColor = attributes.getColor(R.styleable.MaterialCardView_strokeColor, -1);
        this.strokeWidth = attributes.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
        this.checkable = attributes.getBoolean(R.styleable.MaterialCardView_android_checkable, false);
        this.materialCardView.setLongClickable(this.checkable);
        this.checkedIconTint = MaterialResources.getColorStateList(this.materialCardView.getContext(), attributes, R.styleable.MaterialCardView_checkedIconTint);
        setCheckedIcon(MaterialResources.getDrawable(this.materialCardView.getContext(), attributes, R.styleable.MaterialCardView_checkedIcon));
        this.rippleColor = MaterialResources.getColorStateList(this.materialCardView.getContext(), attributes, R.styleable.MaterialCardView_rippleColor);
        if (this.rippleColor == null) {
            this.rippleColor = ColorStateList.valueOf(MaterialColors.getColor(this.materialCardView, R.attr.colorControlHighlight));
        }
        adjustShapeAppearanceModelInsetByStroke();
        ColorStateList foregroundColor = MaterialResources.getColorStateList(this.materialCardView.getContext(), attributes, R.styleable.MaterialCardView_cardForegroundColor);
        this.foregroundContentDrawable.setFillColor(foregroundColor == null ? ColorStateList.valueOf(0) : foregroundColor);
        updateRippleColor();
        updateElevation();
        updateStroke();
        this.materialCardView.setBackgroundInternal(insetDrawable(this.bgDrawable));
        this.fgDrawable = this.materialCardView.isClickable() ? getClickableForeground() : this.foregroundContentDrawable;
        this.materialCardView.setForeground(insetDrawable(this.fgDrawable));
    }

    public boolean isBackgroundOverwritten() {
        return this.isBackgroundOverwritten;
    }

    public void setBackgroundOverwritten(boolean isBackgroundOverwritten) {
        this.isBackgroundOverwritten = isBackgroundOverwritten;
    }

    public void setStrokeColor(@ColorInt int strokeColor) {
        if (this.strokeColor != strokeColor) {
            this.strokeColor = strokeColor;
            updateStroke();
        }
    }

    @ColorInt
    public int getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeWidth(@Dimension int strokeWidth) {
        if (strokeWidth != this.strokeWidth) {
            this.strokeWidth = strokeWidth;
            adjustShapeAppearanceModelInsetByStroke();
            updateStroke();
        }
    }

    @Dimension
    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setCardBackgroundColor(ColorStateList color) {
        this.bgDrawable.setFillColor(color);
    }

    public ColorStateList getCardBackgroundColor() {
        return this.bgDrawable.getFillColor();
    }

    public void setUserContentPadding(int left, int top, int right, int bottom) {
        this.userContentPadding.set(left, top, right, bottom);
        updateContentPadding();
    }

    public Rect getUserContentPadding() {
        return this.userContentPadding;
    }

    public void updateClickable() {
        Drawable previousFgDrawable = this.fgDrawable;
        this.fgDrawable = this.materialCardView.isClickable() ? getClickableForeground() : this.foregroundContentDrawable;
        Drawable drawable = this.fgDrawable;
        if (previousFgDrawable != drawable) {
            updateInsetForeground(drawable);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.shapeAppearanceModel.setCornerRadius(cornerRadius);
        this.shapeAppearanceModelInsetByStroke.setCornerRadius(cornerRadius - this.strokeWidth);
        this.bgDrawable.invalidateSelf();
        this.fgDrawable.invalidateSelf();
        if (shouldAddCornerPaddingOutsideCardBackground() || shouldAddCornerPaddingInsideCardBackground()) {
            updateContentPadding();
        }
        if (shouldAddCornerPaddingOutsideCardBackground()) {
            updateInsets();
        }
    }

    public float getCornerRadius() {
        return this.shapeAppearanceModel.getTopLeftCorner().getCornerSize();
    }

    public void updateElevation() {
        if (Build.VERSION.SDK_INT < 21) {
            this.bgDrawable.setElevation(this.materialCardView.getCardElevation());
            this.bgDrawable.setShadowRadius((int) Math.ceil(this.materialCardView.getCardElevation() * SHADOW_RADIUS_MULTIPLIER));
            this.bgDrawable.setShadowVerticalOffset((int) Math.ceil(this.materialCardView.getCardElevation() * SHADOW_OFFSET_MULTIPLIER));
        }
    }

    public void updateInsets() {
        if (!isBackgroundOverwritten()) {
            this.materialCardView.setBackgroundInternal(insetDrawable(this.bgDrawable));
        }
        this.materialCardView.setForeground(insetDrawable(this.fgDrawable));
    }

    void updateStroke() {
        this.foregroundContentDrawable.setStroke(this.strokeWidth, this.strokeColor);
    }

    @TargetApi(21)
    public void createOutlineProvider(@Nullable View contentView) {
        if (contentView != null) {
            this.materialCardView.setClipToOutline(false);
            if (canClipToOutline()) {
                contentView.setClipToOutline(true);
                contentView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.google.android.material.card.MaterialCardViewHelper.1
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view, Outline outline) {
                        MaterialCardViewHelper.this.temporaryBounds.set(0, 0, view.getWidth(), view.getHeight());
                        MaterialCardViewHelper.this.drawableInsetByStroke.setBounds(MaterialCardViewHelper.this.temporaryBounds);
                        MaterialCardViewHelper.this.drawableInsetByStroke.getOutline(outline);
                    }
                });
                return;
            }
            contentView.setClipToOutline(false);
            contentView.setOutlineProvider(null);
        }
    }

    public void updateContentPadding() {
        boolean includeCornerPadding = shouldAddCornerPaddingInsideCardBackground() || shouldAddCornerPaddingOutsideCardBackground();
        int contentPaddingOffset = (int) ((includeCornerPadding ? calculateActualCornerPadding() : 0.0f) - getParentCardViewCalculatedCornerPadding());
        this.materialCardView.setContentPaddingInternal(this.userContentPadding.left + contentPaddingOffset, this.userContentPadding.top + contentPaddingOffset, this.userContentPadding.right + contentPaddingOffset, this.userContentPadding.bottom + contentPaddingOffset);
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isCheckable() {
        return this.checkable;
    }

    public void setRippleColor(@Nullable ColorStateList rippleColor) {
        this.rippleColor = rippleColor;
    }

    public void setCheckedIconTint(@Nullable ColorStateList checkedIconTint) {
        this.checkedIconTint = checkedIconTint;
        Drawable drawable = this.checkedIcon;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, checkedIconTint);
        }
    }

    @Nullable
    public ColorStateList getCheckedIconTint() {
        return this.checkedIconTint;
    }

    @Nullable
    public ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    @Nullable
    public Drawable getCheckedIcon() {
        return this.checkedIcon;
    }

    public void setCheckedIcon(@Nullable Drawable checkedIcon) {
        this.checkedIcon = checkedIcon;
        if (checkedIcon != null) {
            this.checkedIcon = DrawableCompat.wrap(checkedIcon.mutate());
            DrawableCompat.setTintList(this.checkedIcon, this.checkedIconTint);
        }
        if (this.clickableForegroundDrawable != null) {
            Drawable checkedLayer = createCheckedIconLayer();
            this.clickableForegroundDrawable.setDrawableByLayerId(R.id.mtrl_card_checked_layer_id, checkedLayer);
        }
    }

    public void onMeasure(int measuredWidth, int measuredHeight) {
        int right;
        int left;
        if (this.materialCardView.isCheckable() && this.clickableForegroundDrawable != null) {
            Resources resources = this.materialCardView.getResources();
            int margin = resources.getDimensionPixelSize(R.dimen.mtrl_card_checked_icon_margin);
            int size = resources.getDimensionPixelSize(R.dimen.mtrl_card_checked_icon_size);
            int left2 = (measuredWidth - margin) - size;
            int bottom = (measuredHeight - margin) - size;
            if (ViewCompat.getLayoutDirection(this.materialCardView) == 1) {
                left = margin;
                right = left2;
            } else {
                left = left2;
                right = margin;
            }
            this.clickableForegroundDrawable.setLayerInset(2, left, margin, right, bottom);
        }
    }

    @RequiresApi(api = 23)
    public void forceRippleRedraw() {
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int bottom = bounds.bottom;
            this.rippleDrawable.setBounds(bounds.left, bounds.top, bounds.right, bottom - 1);
            this.rippleDrawable.setBounds(bounds.left, bounds.top, bounds.right, bottom);
        }
    }

    private void adjustShapeAppearanceModelInsetByStroke() {
        this.shapeAppearanceModelInsetByStroke.getTopLeftCorner().setCornerSize(this.shapeAppearanceModel.getTopLeftCorner().getCornerSize() - this.strokeWidth);
        this.shapeAppearanceModelInsetByStroke.getTopRightCorner().setCornerSize(this.shapeAppearanceModel.getTopRightCorner().getCornerSize() - this.strokeWidth);
        this.shapeAppearanceModelInsetByStroke.getBottomRightCorner().setCornerSize(this.shapeAppearanceModel.getBottomRightCorner().getCornerSize() - this.strokeWidth);
        this.shapeAppearanceModelInsetByStroke.getBottomLeftCorner().setCornerSize(this.shapeAppearanceModel.getBottomLeftCorner().getCornerSize() - this.strokeWidth);
    }

    private void updateInsetForeground(Drawable insetForeground) {
        if (Build.VERSION.SDK_INT < 23 || !(this.materialCardView.getForeground() instanceof InsetDrawable)) {
            this.materialCardView.setForeground(insetDrawable(insetForeground));
        } else {
            ((InsetDrawable) this.materialCardView.getForeground()).setDrawable(insetForeground);
        }
    }

    private Drawable insetDrawable(Drawable originalDrawable) {
        int insetVertical = 0;
        int insetHorizontal = 0;
        boolean isPreLollipop = Build.VERSION.SDK_INT < 21;
        if (isPreLollipop || this.materialCardView.getUseCompatPadding()) {
            insetVertical = (int) Math.ceil(calculateVerticalBackgroundPadding());
            insetHorizontal = (int) Math.ceil(calculateHorizontalBackgroundPadding());
        }
        return new InsetDrawable(originalDrawable, insetHorizontal, insetVertical, insetHorizontal, insetVertical) { // from class: com.google.android.material.card.MaterialCardViewHelper.2
            @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
            public boolean getPadding(Rect padding) {
                return false;
            }
        };
    }

    private float calculateVerticalBackgroundPadding() {
        return (this.materialCardView.getMaxCardElevation() * CARD_VIEW_SHADOW_MULTIPLIER) + (shouldAddCornerPaddingOutsideCardBackground() ? calculateActualCornerPadding() : 0.0f);
    }

    private float calculateHorizontalBackgroundPadding() {
        return this.materialCardView.getMaxCardElevation() + (shouldAddCornerPaddingOutsideCardBackground() ? calculateActualCornerPadding() : 0.0f);
    }

    private boolean canClipToOutline() {
        return Build.VERSION.SDK_INT >= 21 && this.shapeAppearanceModel.isRoundRect();
    }

    private float getParentCardViewCalculatedCornerPadding() {
        if (!this.materialCardView.getPreventCornerOverlap()) {
            return 0.0f;
        }
        if (Build.VERSION.SDK_INT < 21 || this.materialCardView.getUseCompatPadding()) {
            return (float) ((1.0d - COS_45) * this.materialCardView.getCardViewRadius());
        }
        return 0.0f;
    }

    private boolean shouldAddCornerPaddingInsideCardBackground() {
        return this.materialCardView.getPreventCornerOverlap() && !canClipToOutline();
    }

    private boolean shouldAddCornerPaddingOutsideCardBackground() {
        return this.materialCardView.getPreventCornerOverlap() && canClipToOutline() && this.materialCardView.getUseCompatPadding();
    }

    private float calculateActualCornerPadding() {
        return Math.max(Math.max(calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopLeftCorner()), calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopRightCorner())), Math.max(calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomRightCorner()), calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomLeftCorner())));
    }

    private float calculateCornerPaddingForCornerTreatment(CornerTreatment treatment) {
        if (treatment instanceof RoundedCornerTreatment) {
            return (float) ((1.0d - COS_45) * treatment.getCornerSize());
        }
        if (treatment instanceof CutCornerTreatment) {
            return treatment.getCornerSize() / 2.0f;
        }
        return 0.0f;
    }

    @NonNull
    private Drawable getClickableForeground() {
        if (this.rippleDrawable == null) {
            this.rippleDrawable = createForegroundRippleDrawable();
        }
        if (this.clickableForegroundDrawable == null) {
            Drawable checkedLayer = createCheckedIconLayer();
            this.clickableForegroundDrawable = new LayerDrawable(new Drawable[]{this.rippleDrawable, this.foregroundContentDrawable, checkedLayer});
            this.clickableForegroundDrawable.setId(2, R.id.mtrl_card_checked_layer_id);
        }
        Drawable checkedLayer2 = this.clickableForegroundDrawable;
        return checkedLayer2;
    }

    private Drawable createForegroundRippleDrawable() {
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            return new RippleDrawable(this.rippleColor, null, createForegroundShapeDrawable());
        }
        return createCompatRippleDrawable();
    }

    private Drawable createCompatRippleDrawable() {
        StateListDrawable rippleDrawable = new StateListDrawable();
        this.compatRippleDrawable = createForegroundShapeDrawable();
        this.compatRippleDrawable.setFillColor(this.rippleColor);
        rippleDrawable.addState(new int[]{16842919}, this.compatRippleDrawable);
        return rippleDrawable;
    }

    private void updateRippleColor() {
        Drawable drawable;
        if (!RippleUtils.USE_FRAMEWORK_RIPPLE || (drawable = this.rippleDrawable) == null) {
            MaterialShapeDrawable materialShapeDrawable = this.compatRippleDrawable;
            if (materialShapeDrawable != null) {
                materialShapeDrawable.setFillColor(this.rippleColor);
                return;
            }
            return;
        }
        ((RippleDrawable) drawable).setColor(this.rippleColor);
    }

    @NonNull
    private Drawable createCheckedIconLayer() {
        StateListDrawable checkedLayer = new StateListDrawable();
        Drawable drawable = this.checkedIcon;
        if (drawable != null) {
            checkedLayer.addState(CHECKED_STATE_SET, drawable);
        }
        return checkedLayer;
    }

    private MaterialShapeDrawable createForegroundShapeDrawable() {
        return new MaterialShapeDrawable(this.shapeAppearanceModel);
    }
}
