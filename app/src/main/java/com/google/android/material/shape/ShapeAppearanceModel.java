package com.google.android.material.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import androidx.annotation.AttrRes;
import androidx.annotation.Dimension;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import com.google.android.material.R;

/* loaded from: classes2.dex */
public class ShapeAppearanceModel {
    private EdgeTreatment bottomEdge;
    private CornerTreatment bottomLeftCorner;
    private CornerTreatment bottomRightCorner;
    private EdgeTreatment leftEdge;
    private EdgeTreatment rightEdge;
    private EdgeTreatment topEdge;
    private CornerTreatment topLeftCorner;
    private CornerTreatment topRightCorner;

    public ShapeAppearanceModel() {
        setTopLeftCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setTopRightCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setBottomRightCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setBottomLeftCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setTopEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setRightEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setBottomEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setLeftEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
    }

    public ShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.topLeftCorner = shapeAppearanceModel.getTopLeftCorner().clone();
        this.topRightCorner = shapeAppearanceModel.getTopRightCorner().clone();
        this.bottomRightCorner = shapeAppearanceModel.getBottomRightCorner().clone();
        this.bottomLeftCorner = shapeAppearanceModel.getBottomLeftCorner().clone();
        this.topEdge = shapeAppearanceModel.getTopEdge().clone();
        this.rightEdge = shapeAppearanceModel.getRightEdge().clone();
        this.leftEdge = shapeAppearanceModel.getLeftEdge().clone();
        this.bottomEdge = shapeAppearanceModel.getBottomEdge().clone();
    }

    public ShapeAppearanceModel(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        this(context, attrs, defStyleAttr, defStyleRes, 0);
    }

    public ShapeAppearanceModel(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes, int defaultCornerSize) {
        Context context2 = context;
        TypedArray a = context2.obtainStyledAttributes(attrs, R.styleable.MaterialShape, defStyleAttr, defStyleRes);
        int shapeAppearanceResId = a.getResourceId(R.styleable.MaterialShape_shapeAppearance, 0);
        int shapeAppearanceOverlayResId = a.getResourceId(R.styleable.MaterialShape_shapeAppearanceOverlay, 0);
        a.recycle();
        if (shapeAppearanceOverlayResId != 0) {
            context2 = new ContextThemeWrapper(context2, shapeAppearanceResId);
            shapeAppearanceResId = shapeAppearanceOverlayResId;
        }
        TypedArray a2 = context2.obtainStyledAttributes(shapeAppearanceResId, R.styleable.ShapeAppearance);
        int cornerFamily = a2.getInt(R.styleable.ShapeAppearance_cornerFamily, 0);
        int cornerFamilyTopLeft = a2.getInt(R.styleable.ShapeAppearance_cornerFamilyTopLeft, cornerFamily);
        int cornerFamilyTopRight = a2.getInt(R.styleable.ShapeAppearance_cornerFamilyTopRight, cornerFamily);
        int cornerFamilyBottomRight = a2.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomRight, cornerFamily);
        int cornerFamilyBottomLeft = a2.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomLeft, cornerFamily);
        int cornerSize = a2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSize, defaultCornerSize);
        int cornerSizeTopLeft = a2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeTopLeft, cornerSize);
        int cornerSizeTopRight = a2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeTopRight, cornerSize);
        int cornerSizeBottomRight = a2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeBottomRight, cornerSize);
        int cornerSizeBottomLeft = a2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeBottomLeft, cornerSize);
        setTopLeftCorner(cornerFamilyTopLeft, cornerSizeTopLeft);
        setTopRightCorner(cornerFamilyTopRight, cornerSizeTopRight);
        setBottomRightCorner(cornerFamilyBottomRight, cornerSizeBottomRight);
        setBottomLeftCorner(cornerFamilyBottomLeft, cornerSizeBottomLeft);
        setTopEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setRightEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setBottomEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setLeftEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        a2.recycle();
    }

    public void setAllCorners(CornerTreatment cornerTreatment) {
        this.topLeftCorner = cornerTreatment.clone();
        this.topRightCorner = cornerTreatment.clone();
        this.bottomRightCorner = cornerTreatment.clone();
        this.bottomLeftCorner = cornerTreatment.clone();
    }

    public void setAllCorners(int cornerFamily, @Dimension int cornerSize) {
        setAllCorners(MaterialShapeUtils.createCornerTreatment(cornerFamily, cornerSize));
    }

    public void setCornerRadius(float cornerRadius) {
        this.topLeftCorner.setCornerSize(cornerRadius);
        this.topRightCorner.setCornerSize(cornerRadius);
        this.bottomRightCorner.setCornerSize(cornerRadius);
        this.bottomLeftCorner.setCornerSize(cornerRadius);
    }

    public void setCornerRadii(float topLeftCornerRadius, float topRightCornerRadius, float bottomRightCornerRadius, float bottomLeftCornerRadius) {
        this.topLeftCorner.setCornerSize(topLeftCornerRadius);
        this.topRightCorner.setCornerSize(topRightCornerRadius);
        this.bottomRightCorner.setCornerSize(bottomRightCornerRadius);
        this.bottomLeftCorner.setCornerSize(bottomLeftCornerRadius);
    }

    public void setAllEdges(EdgeTreatment edgeTreatment) {
        this.leftEdge = edgeTreatment.clone();
        this.topEdge = edgeTreatment.clone();
        this.rightEdge = edgeTreatment.clone();
        this.bottomEdge = edgeTreatment.clone();
    }

    public void setCornerTreatments(CornerTreatment topLeftCorner, CornerTreatment topRightCorner, CornerTreatment bottomRightCorner, CornerTreatment bottomLeftCorner) {
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomRightCorner = bottomRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public void setEdgeTreatments(EdgeTreatment leftEdge, EdgeTreatment topEdge, EdgeTreatment rightEdge, EdgeTreatment bottomEdge) {
        this.leftEdge = leftEdge;
        this.topEdge = topEdge;
        this.rightEdge = rightEdge;
        this.bottomEdge = bottomEdge;
    }

    public void setTopLeftCorner(int cornerFamily, @Dimension int cornerSize) {
        setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(cornerFamily, cornerSize));
    }

    public void setTopLeftCorner(CornerTreatment topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
    }

    public CornerTreatment getTopLeftCorner() {
        return this.topLeftCorner;
    }

    public void setTopRightCorner(int cornerFamily, @Dimension int cornerSize) {
        setTopRightCorner(MaterialShapeUtils.createCornerTreatment(cornerFamily, cornerSize));
    }

    public void setTopRightCorner(CornerTreatment topRightCorner) {
        this.topRightCorner = topRightCorner;
    }

    public CornerTreatment getTopRightCorner() {
        return this.topRightCorner;
    }

    public void setBottomRightCorner(int cornerFamily, @Dimension int cornerSize) {
        setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(cornerFamily, cornerSize));
    }

    public void setBottomRightCorner(CornerTreatment bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }

    public CornerTreatment getBottomRightCorner() {
        return this.bottomRightCorner;
    }

    public void setBottomLeftCorner(int cornerFamily, @Dimension int cornerSize) {
        setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(cornerFamily, cornerSize));
    }

    public void setBottomLeftCorner(CornerTreatment bottomLeftCorner) {
        this.bottomLeftCorner = bottomLeftCorner;
    }

    public CornerTreatment getBottomLeftCorner() {
        return this.bottomLeftCorner;
    }

    public void setTopEdge(EdgeTreatment topEdge) {
        this.topEdge = topEdge;
    }

    public EdgeTreatment getTopEdge() {
        return this.topEdge;
    }

    public void setRightEdge(EdgeTreatment rightEdge) {
        this.rightEdge = rightEdge;
    }

    public EdgeTreatment getRightEdge() {
        return this.rightEdge;
    }

    public void setBottomEdge(EdgeTreatment bottomEdge) {
        this.bottomEdge = bottomEdge;
    }

    public EdgeTreatment getBottomEdge() {
        return this.bottomEdge;
    }

    public void setLeftEdge(EdgeTreatment leftEdge) {
        this.leftEdge = leftEdge;
    }

    public EdgeTreatment getLeftEdge() {
        return this.leftEdge;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isRoundRect() {
        boolean hasDefaultEdges = this.leftEdge.getClass().equals(EdgeTreatment.class) && this.rightEdge.getClass().equals(EdgeTreatment.class) && this.topEdge.getClass().equals(EdgeTreatment.class) && this.bottomEdge.getClass().equals(EdgeTreatment.class);
        float cornerSize = this.topLeftCorner.getCornerSize();
        boolean cornersHaveSameSize = this.topRightCorner.getCornerSize() == cornerSize && this.bottomLeftCorner.getCornerSize() == cornerSize && this.bottomRightCorner.getCornerSize() == cornerSize;
        boolean hasRoundedCorners = (this.topRightCorner instanceof RoundedCornerTreatment) && (this.topLeftCorner instanceof RoundedCornerTreatment) && (this.bottomRightCorner instanceof RoundedCornerTreatment) && (this.bottomLeftCorner instanceof RoundedCornerTreatment);
        return hasDefaultEdges && cornersHaveSameSize && hasRoundedCorners;
    }
}
