package com.google.android.material.shape;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.core.graphics.drawable.TintAwareDrawable;
import com.google.android.material.shadow.ShadowRenderer;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.ShapePath;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable {
    public static final int SHADOW_COMPAT_MODE_ALWAYS = 2;
    public static final int SHADOW_COMPAT_MODE_DEFAULT = 0;
    public static final int SHADOW_COMPAT_MODE_NEVER = 1;
    private static final Paint clearPaint = new Paint(1);
    private final ShapePath.ShadowCompatOperation[] cornerShadowOperation;
    private MaterialShapeDrawableState drawableState;
    private final ShapePath.ShadowCompatOperation[] edgeShadowOperation;
    private final Paint fillPaint;
    private final RectF insetRectF;
    private final Matrix matrix;
    private final Path path;
    private boolean pathDirty;
    private final Path pathInsetByStroke;
    private final ShapeAppearancePathProvider pathProvider;
    private final ShapeAppearancePathProvider.PathListener pathShadowListener;
    private final RectF rectF;
    private final Region scratchRegion;
    private final ShadowRenderer shadowRenderer;
    private final Paint strokePaint;
    private ShapeAppearanceModel strokeShapeAppearance;
    @Nullable
    private PorterDuffColorFilter strokeTintFilter;
    @Nullable
    private PorterDuffColorFilter tintFilter;
    private final Region transparentRegion;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface CompatibilityShadowMode {
    }

    public MaterialShapeDrawable() {
        this(new ShapeAppearanceModel());
    }

    public MaterialShapeDrawable(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        this(new ShapeAppearanceModel(context, attrs, defStyleAttr, defStyleRes));
    }

    public MaterialShapeDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this(new MaterialShapeDrawableState(shapeAppearanceModel));
    }

    private MaterialShapeDrawable(MaterialShapeDrawableState drawableState) {
        this.cornerShadowOperation = new ShapePath.ShadowCompatOperation[4];
        this.edgeShadowOperation = new ShapePath.ShadowCompatOperation[4];
        this.matrix = new Matrix();
        this.path = new Path();
        this.pathInsetByStroke = new Path();
        this.rectF = new RectF();
        this.insetRectF = new RectF();
        this.transparentRegion = new Region();
        this.scratchRegion = new Region();
        this.fillPaint = new Paint(1);
        this.strokePaint = new Paint(1);
        this.shadowRenderer = new ShadowRenderer();
        this.pathProvider = new ShapeAppearancePathProvider();
        this.drawableState = drawableState;
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.fillPaint.setStyle(Paint.Style.FILL);
        clearPaint.setColor(-1);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        updateTintFilter();
        updateColorsForState(getState(), false);
        this.pathShadowListener = new ShapeAppearancePathProvider.PathListener() { // from class: com.google.android.material.shape.MaterialShapeDrawable.1
            @Override // com.google.android.material.shape.ShapeAppearancePathProvider.PathListener
            public void onCornerPathCreated(ShapePath cornerPath, Matrix transform, int count) {
                MaterialShapeDrawable.this.cornerShadowOperation[count] = cornerPath.createShadowCompatOperation(transform);
            }

            @Override // com.google.android.material.shape.ShapeAppearancePathProvider.PathListener
            public void onEdgePathCreated(ShapePath edgePath, Matrix transform, int count) {
                MaterialShapeDrawable.this.edgeShadowOperation[count] = edgePath.createShadowCompatOperation(transform);
            }
        };
    }

    @Override // android.graphics.drawable.Drawable
    @Nullable
    public Drawable.ConstantState getConstantState() {
        return this.drawableState;
    }

    @Override // android.graphics.drawable.Drawable
    @NonNull
    public Drawable mutate() {
        MaterialShapeDrawableState newDrawableState = new MaterialShapeDrawableState(this.drawableState);
        this.drawableState = newDrawableState;
        return this;
    }

    private static int modulateAlpha(int paintAlpha, int alpha) {
        int scale = (alpha >>> 7) + alpha;
        return (paintAlpha * scale) >>> 8;
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.drawableState.shapeAppearanceModel = shapeAppearanceModel;
        invalidateSelf();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.drawableState.shapeAppearanceModel;
    }

    @Deprecated
    public void setShapedViewModel(ShapePathModel shapedViewModel) {
        setShapeAppearanceModel(shapedViewModel);
    }

    @Deprecated
    public ShapeAppearanceModel getShapedViewModel() {
        return getShapeAppearanceModel();
    }

    public void setFillColor(@Nullable ColorStateList fillColor) {
        if (this.drawableState.fillColor != fillColor) {
            this.drawableState.fillColor = fillColor;
            onStateChange(getState());
        }
    }

    @Nullable
    public ColorStateList getFillColor() {
        return this.drawableState.fillColor;
    }

    public void setStrokeColor(@Nullable ColorStateList strokeColor) {
        if (this.drawableState.strokeColor != strokeColor) {
            this.drawableState.strokeColor = strokeColor;
            onStateChange(getState());
        }
    }

    @Nullable
    public ColorStateList getStrokeColor() {
        return this.drawableState.strokeColor;
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        if (this.drawableState.tintMode != tintMode) {
            this.drawableState.tintMode = tintMode;
            updateTintFilter();
            invalidateSelfIgnoreShape();
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTintList(ColorStateList tintList) {
        this.drawableState.tintList = tintList;
        updateTintFilter();
        invalidateSelfIgnoreShape();
    }

    public ColorStateList getTintList() {
        return this.drawableState.tintList;
    }

    public ColorStateList getStrokeTintList() {
        return this.drawableState.strokeTintList;
    }

    @ColorInt
    @Deprecated
    public int getStrokeTint() {
        return this.drawableState.strokeTintList.getColorForState(getState(), 0);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.TintAwareDrawable
    public void setTint(@ColorInt int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    public void setStrokeTint(ColorStateList tintList) {
        this.drawableState.strokeTintList = tintList;
        updateTintFilter();
        invalidateSelfIgnoreShape();
    }

    public void setStrokeTint(@ColorInt int tintColor) {
        setStrokeTint(ColorStateList.valueOf(tintColor));
    }

    public void setStroke(float strokeWidth, @ColorInt int strokeColor) {
        setStrokeWidth(strokeWidth);
        setStrokeColor(ColorStateList.valueOf(strokeColor));
    }

    public void setStroke(float strokeWidth, @Nullable ColorStateList strokeColor) {
        setStrokeWidth(strokeWidth);
        setStrokeColor(strokeColor);
    }

    public float getStrokeWidth() {
        return this.drawableState.strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.drawableState.strokeWidth = strokeWidth;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (this.drawableState.alpha != alpha) {
            this.drawableState.alpha = alpha;
            invalidateSelfIgnoreShape();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.drawableState.colorFilter = colorFilter;
        invalidateSelfIgnoreShape();
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        Rect bounds = getBounds();
        this.transparentRegion.set(bounds);
        calculatePath(getBoundsAsRectF(), this.path);
        this.scratchRegion.setPath(this.path, this.transparentRegion);
        this.transparentRegion.op(this.scratchRegion, Region.Op.DIFFERENCE);
        return this.transparentRegion;
    }

    public RectF getBoundsAsRectF() {
        Rect bounds = getBounds();
        this.rectF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        return this.rectF;
    }

    public void setCornerRadius(float cornerRadius) {
        this.drawableState.shapeAppearanceModel.setCornerRadius(cornerRadius);
        invalidateSelf();
    }

    public boolean isPointInTransparentRegion(int x, int y) {
        return getTransparentRegion().contains(x, y);
    }

    public int getShadowCompatibilityMode() {
        return this.drawableState.shadowCompatMode;
    }

    public void setShadowCompatibilityMode(int mode) {
        if (this.drawableState.shadowCompatMode != mode) {
            this.drawableState.shadowCompatMode = mode;
            invalidateSelfIgnoreShape();
        }
    }

    @Deprecated
    public boolean isShadowEnabled() {
        return this.drawableState.shadowCompatMode == 0 || this.drawableState.shadowCompatMode == 2;
    }

    @Deprecated
    public void setShadowEnabled(boolean shadowEnabled) {
        setShadowCompatibilityMode(!shadowEnabled ? 1 : 0);
    }

    public float getInterpolation() {
        return this.drawableState.interpolation;
    }

    public void setInterpolation(float interpolation) {
        if (this.drawableState.interpolation != interpolation) {
            this.drawableState.interpolation = interpolation;
            invalidateSelf();
        }
    }

    public float getElevation() {
        return this.drawableState.elevation;
    }

    public void setElevation(float elevation) {
        if (this.drawableState.elevation != elevation) {
            this.drawableState.shadowCompatRadius = Math.round(elevation);
            this.drawableState.elevation = elevation;
            invalidateSelfIgnoreShape();
        }
    }

    @Deprecated
    public int getShadowElevation() {
        return (int) getElevation();
    }

    @Deprecated
    public void setShadowElevation(int shadowElevation) {
        setElevation(shadowElevation);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public int getShadowVerticalOffset() {
        return this.drawableState.shadowCompatOffset;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setShadowVerticalOffset(int shadowOffset) {
        if (this.drawableState.shadowCompatOffset != shadowOffset) {
            this.drawableState.shadowCompatOffset = shadowOffset;
            invalidateSelfIgnoreShape();
        }
    }

    public int getShadowCompatRotation() {
        return this.drawableState.shadowCompatRotation;
    }

    public void setShadowCompatRotation(int shadowRotation) {
        if (this.drawableState.shadowCompatRotation != shadowRotation) {
            this.drawableState.shadowCompatRotation = shadowRotation;
            invalidateSelfIgnoreShape();
        }
    }

    @Deprecated
    public int getShadowRadius() {
        return this.drawableState.shadowCompatRadius;
    }

    @Deprecated
    public void setShadowRadius(int shadowRadius) {
        this.drawableState.shadowCompatRadius = shadowRadius;
    }

    private boolean requiresCompatShadow() {
        return Build.VERSION.SDK_INT < 21 || (!this.drawableState.shapeAppearanceModel.isRoundRect() && !this.path.isConvex());
    }

    public float getScale() {
        return this.drawableState.scale;
    }

    public void setScale(float scale) {
        if (this.drawableState.scale != scale) {
            this.drawableState.scale = scale;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.pathDirty = true;
        invalidateSelfIgnoreShape();
    }

    private void invalidateSelfIgnoreShape() {
        super.invalidateSelf();
    }

    public void setUseTintColorForShadow(boolean useTintColorForShadow) {
        if (this.drawableState.useTintColorForShadow != useTintColorForShadow) {
            this.drawableState.useTintColorForShadow = useTintColorForShadow;
            invalidateSelf();
        }
    }

    public void setShadowColor(int shadowColor) {
        this.shadowRenderer.setShadowColor(shadowColor);
        this.drawableState.useTintColorForShadow = false;
        invalidateSelfIgnoreShape();
    }

    public Paint.Style getPaintStyle() {
        return this.drawableState.paintStyle;
    }

    public void setPaintStyle(Paint.Style paintStyle) {
        this.drawableState.paintStyle = paintStyle;
        invalidateSelfIgnoreShape();
    }

    private boolean hasCompatShadow() {
        return this.drawableState.shadowCompatMode != 1 && this.drawableState.shadowCompatRadius > 0 && (this.drawableState.shadowCompatMode == 2 || requiresCompatShadow());
    }

    private boolean hasFill() {
        return this.drawableState.paintStyle == Paint.Style.FILL_AND_STROKE || this.drawableState.paintStyle == Paint.Style.FILL;
    }

    private boolean hasStroke() {
        return (this.drawableState.paintStyle == Paint.Style.FILL_AND_STROKE || this.drawableState.paintStyle == Paint.Style.STROKE) && this.strokePaint.getStrokeWidth() > 0.0f;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        this.pathDirty = true;
        super.onBoundsChange(bounds);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.fillPaint.setColorFilter(this.tintFilter);
        int prevAlpha = this.fillPaint.getAlpha();
        this.fillPaint.setAlpha(modulateAlpha(prevAlpha, this.drawableState.alpha));
        this.strokePaint.setColorFilter(this.strokeTintFilter);
        this.strokePaint.setStrokeWidth(this.drawableState.strokeWidth);
        int prevStrokeAlpha = this.strokePaint.getAlpha();
        this.strokePaint.setAlpha(modulateAlpha(prevStrokeAlpha, this.drawableState.alpha));
        if (this.pathDirty) {
            calculateStrokePath();
            calculatePath(getBoundsAsRectF(), this.path);
            this.pathDirty = false;
        }
        if (hasCompatShadow()) {
            canvas.save();
            prepareCanvasForShadow(canvas);
            Bitmap shadowLayer = Bitmap.createBitmap(getBounds().width() + (this.drawableState.shadowCompatRadius * 2), getBounds().height() + (this.drawableState.shadowCompatRadius * 2), Bitmap.Config.ARGB_8888);
            Canvas shadowCanvas = new Canvas(shadowLayer);
            float shadowLeft = getBounds().left - this.drawableState.shadowCompatRadius;
            float shadowTop = getBounds().top - this.drawableState.shadowCompatRadius;
            shadowCanvas.translate(-shadowLeft, -shadowTop);
            drawCompatShadow(shadowCanvas);
            canvas.drawBitmap(shadowLayer, shadowLeft, shadowTop, (Paint) null);
            shadowLayer.recycle();
            canvas.restore();
        }
        if (hasFill()) {
            drawFillShape(canvas);
        }
        if (hasStroke()) {
            drawStrokeShape(canvas);
        }
        this.fillPaint.setAlpha(prevAlpha);
        this.strokePaint.setAlpha(prevStrokeAlpha);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void drawShape(Canvas canvas, Paint paint, Path path, RectF bounds) {
        drawShape(canvas, paint, path, this.drawableState.shapeAppearanceModel, bounds);
    }

    private void drawShape(Canvas canvas, Paint paint, Path path, ShapeAppearanceModel shapeAppearanceModel, RectF bounds) {
        if (shapeAppearanceModel.isRoundRect()) {
            float cornerSize = shapeAppearanceModel.getTopRightCorner().getCornerSize();
            canvas.drawRoundRect(bounds, cornerSize, cornerSize, paint);
            return;
        }
        canvas.drawPath(path, paint);
    }

    private void drawFillShape(Canvas canvas) {
        drawShape(canvas, this.fillPaint, this.path, this.drawableState.shapeAppearanceModel, getBoundsAsRectF());
    }

    private void drawStrokeShape(Canvas canvas) {
        drawShape(canvas, this.strokePaint, this.pathInsetByStroke, this.strokeShapeAppearance, getBoundsInsetByStroke());
    }

    private void prepareCanvasForShadow(Canvas canvas) {
        int shadowOffsetX = (int) (this.drawableState.shadowCompatOffset * Math.sin(Math.toRadians(this.drawableState.shadowCompatRotation)));
        int shadowOffsetY = (int) (this.drawableState.shadowCompatOffset * Math.cos(Math.toRadians(this.drawableState.shadowCompatRotation)));
        if (Build.VERSION.SDK_INT < 21) {
            Rect canvasClipBounds = canvas.getClipBounds();
            canvasClipBounds.inset(-this.drawableState.shadowCompatRadius, -this.drawableState.shadowCompatRadius);
            canvasClipBounds.offset(-Math.abs(shadowOffsetX), -Math.abs(shadowOffsetY));
            canvas.clipRect(canvasClipBounds, Region.Op.REPLACE);
        }
        canvas.translate(shadowOffsetX, shadowOffsetY);
    }

    private void drawCompatShadow(Canvas canvas) {
        if (this.drawableState.shadowCompatOffset != 0) {
            canvas.drawPath(this.path, this.shadowRenderer.getShadowPaint());
        }
        for (int index = 0; index < 4; index++) {
            this.cornerShadowOperation[index].draw(this.shadowRenderer, this.drawableState.shadowCompatRadius, canvas);
            this.edgeShadowOperation[index].draw(this.shadowRenderer, this.drawableState.shadowCompatRadius, canvas);
        }
        int shadowOffsetX = (int) (this.drawableState.shadowCompatOffset * Math.sin(Math.toRadians(this.drawableState.shadowCompatRotation)));
        int shadowOffsetY = (int) (this.drawableState.shadowCompatOffset * Math.cos(Math.toRadians(this.drawableState.shadowCompatRotation)));
        canvas.translate(-shadowOffsetX, -shadowOffsetY);
        canvas.drawPath(this.path, clearPaint);
        canvas.translate(shadowOffsetX, shadowOffsetY);
    }

    @Deprecated
    public void getPathForSize(int width, int height, Path path) {
        calculatePathForSize(new RectF(0.0f, 0.0f, width, height), path);
    }

    @Deprecated
    public void getPathForSize(Rect bounds, Path path) {
        calculatePathForSize(new RectF(bounds), path);
    }

    private void calculatePathForSize(RectF bounds, Path path) {
        this.pathProvider.calculatePath(this.drawableState.shapeAppearanceModel, this.drawableState.interpolation, bounds, this.pathShadowListener, path);
    }

    private void calculateStrokePath() {
        this.strokeShapeAppearance = new ShapeAppearanceModel(getShapeAppearanceModel());
        float cornerSizeTopLeft = this.strokeShapeAppearance.getTopLeftCorner().cornerSize;
        float cornerSizeTopRight = this.strokeShapeAppearance.getTopRightCorner().cornerSize;
        float cornerSizeBottomRight = this.strokeShapeAppearance.getBottomRightCorner().cornerSize;
        float cornerSizeBottomLeft = this.strokeShapeAppearance.getBottomLeftCorner().cornerSize;
        this.strokeShapeAppearance.setCornerRadii(adjustCornerSizeForStrokeSize(cornerSizeTopLeft), adjustCornerSizeForStrokeSize(cornerSizeTopRight), adjustCornerSizeForStrokeSize(cornerSizeBottomRight), adjustCornerSizeForStrokeSize(cornerSizeBottomLeft));
        this.pathProvider.calculatePath(this.strokeShapeAppearance, this.drawableState.interpolation, getBoundsInsetByStroke(), this.pathInsetByStroke);
    }

    private float adjustCornerSizeForStrokeSize(float cornerSize) {
        float adjustedCornerSize = cornerSize - getStrokeInsetLength();
        return Math.max(adjustedCornerSize, 0.0f);
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.drawableState.shadowCompatMode != 2) {
            boolean isRoundRect = this.drawableState.shapeAppearanceModel.isRoundRect();
            if (isRoundRect) {
                float radius = this.drawableState.shapeAppearanceModel.getTopLeftCorner().getCornerSize();
                outline.setRoundRect(getBounds(), radius);
                return;
            }
            calculatePath(getBoundsAsRectF(), this.path);
            if (this.path.isConvex()) {
                outline.setConvexPath(this.path);
            }
        }
    }

    private void calculatePath(RectF bounds, Path path) {
        calculatePathForSize(bounds, path);
        if (this.drawableState.scale != 1.0f) {
            this.matrix.reset();
            this.matrix.setScale(this.drawableState.scale, this.drawableState.scale, bounds.width() / 2.0f, bounds.height() / 2.0f);
            path.transform(this.matrix);
        }
    }

    private void updateTintFilter() {
        this.tintFilter = calculateTintFilter(this.drawableState.tintList, this.drawableState.tintMode);
        this.strokeTintFilter = calculateTintFilter(this.drawableState.strokeTintList, this.drawableState.tintMode);
        if (this.drawableState.useTintColorForShadow) {
            this.shadowRenderer.setShadowColor(this.drawableState.tintList.getColorForState(getState(), 0));
        }
    }

    @Nullable
    private PorterDuffColorFilter calculateTintFilter(ColorStateList tintList, PorterDuff.Mode tintMode) {
        if (tintList == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(tintList.getColorForState(getState(), 0), tintMode);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return super.isStateful() || (this.drawableState.tintList != null && this.drawableState.tintList.isStateful()) || ((this.drawableState.strokeTintList != null && this.drawableState.strokeTintList.isStateful()) || ((this.drawableState.strokeColor != null && this.drawableState.strokeColor.isStateful()) || (this.drawableState.fillColor != null && this.drawableState.fillColor.isStateful())));
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] state) {
        boolean invalidateSelf = super.onStateChange(state);
        updateColorsForState(state, invalidateSelf);
        updateTintFilter();
        return invalidateSelf;
    }

    private boolean updateColorsForState(int[] state, boolean invalidateSelf) {
        int previousStrokeColor;
        int newStrokeColor;
        int previousFillColor;
        int newFillColor;
        if (!(this.drawableState.fillColor == null || previousFillColor == (newFillColor = this.drawableState.fillColor.getColorForState(state, (previousFillColor = this.fillPaint.getColor()))))) {
            this.fillPaint.setColor(newFillColor);
            invalidateSelf = true;
        }
        if (this.drawableState.strokeColor == null || previousStrokeColor == (newStrokeColor = this.drawableState.strokeColor.getColorForState(state, (previousStrokeColor = this.strokePaint.getColor())))) {
            return invalidateSelf;
        }
        this.strokePaint.setColor(newStrokeColor);
        return true;
    }

    private float getStrokeInsetLength() {
        if (hasStroke()) {
            return this.strokePaint.getStrokeWidth() / 2.0f;
        }
        return 0.0f;
    }

    private RectF getBoundsInsetByStroke() {
        RectF rectF = getBoundsAsRectF();
        float inset = getStrokeInsetLength();
        this.insetRectF.set(rectF.left + inset, rectF.top + inset, rectF.right - inset, rectF.bottom - inset);
        return this.insetRectF;
    }

    /* loaded from: classes2.dex */
    public static final class MaterialShapeDrawableState extends Drawable.ConstantState {
        public int alpha;
        public ColorFilter colorFilter;
        public float elevation;
        public ColorStateList fillColor;
        public float interpolation;
        public Paint.Style paintStyle;
        public float scale;
        public int shadowCompatMode;
        public int shadowCompatOffset;
        public int shadowCompatRadius;
        public int shadowCompatRotation;
        @NonNull
        public ShapeAppearanceModel shapeAppearanceModel;
        public ColorStateList strokeColor;
        public ColorStateList strokeTintList;
        public float strokeWidth;
        public ColorStateList tintList;
        public PorterDuff.Mode tintMode;
        public boolean useTintColorForShadow;

        public MaterialShapeDrawableState(ShapeAppearanceModel shapeAppearanceModel) {
            this.fillColor = null;
            this.strokeColor = null;
            this.strokeTintList = null;
            this.tintList = null;
            this.tintMode = PorterDuff.Mode.SRC_IN;
            this.scale = 1.0f;
            this.interpolation = 1.0f;
            this.alpha = 255;
            this.elevation = 0.0f;
            this.shadowCompatMode = 0;
            this.shadowCompatRadius = 0;
            this.shadowCompatOffset = 0;
            this.shadowCompatRotation = 0;
            this.useTintColorForShadow = false;
            this.paintStyle = Paint.Style.FILL_AND_STROKE;
            this.shapeAppearanceModel = shapeAppearanceModel;
        }

        public MaterialShapeDrawableState(MaterialShapeDrawableState orig) {
            this.fillColor = null;
            this.strokeColor = null;
            this.strokeTintList = null;
            this.tintList = null;
            this.tintMode = PorterDuff.Mode.SRC_IN;
            this.scale = 1.0f;
            this.interpolation = 1.0f;
            this.alpha = 255;
            this.elevation = 0.0f;
            this.shadowCompatMode = 0;
            this.shadowCompatRadius = 0;
            this.shadowCompatOffset = 0;
            this.shadowCompatRotation = 0;
            this.useTintColorForShadow = false;
            this.paintStyle = Paint.Style.FILL_AND_STROKE;
            this.shapeAppearanceModel = new ShapeAppearanceModel(orig.shapeAppearanceModel);
            this.strokeWidth = orig.strokeWidth;
            this.colorFilter = orig.colorFilter;
            this.fillColor = orig.fillColor;
            this.strokeColor = orig.strokeColor;
            this.tintMode = orig.tintMode;
            this.tintList = orig.tintList;
            this.alpha = orig.alpha;
            this.scale = orig.scale;
            this.shadowCompatOffset = orig.shadowCompatOffset;
            this.shadowCompatMode = orig.shadowCompatMode;
            this.useTintColorForShadow = orig.useTintColorForShadow;
            this.interpolation = orig.interpolation;
            this.elevation = orig.elevation;
            this.shadowCompatRadius = orig.shadowCompatRadius;
            this.shadowCompatRotation = orig.shadowCompatRotation;
            this.strokeTintList = orig.strokeTintList;
            this.paintStyle = orig.paintStyle;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new MaterialShapeDrawable(this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }
    }
}
