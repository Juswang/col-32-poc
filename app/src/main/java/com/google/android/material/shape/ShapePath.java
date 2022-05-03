package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ShapePath {
    protected static final float ANGLE_LEFT = 180.0f;
    private static final float ANGLE_UP = 270.0f;
    public float currentShadowAngle;
    public float endShadowAngle;
    public float endX;
    public float endY;
    private final List<PathOperation> operations = new ArrayList();
    private final List<ShadowCompatOperation> shadowCompatOperations = new ArrayList();
    public float startX;
    public float startY;

    /* loaded from: classes2.dex */
    public static abstract class PathOperation {
        protected final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix matrix, Path path);
    }

    public ShapePath() {
        reset(0.0f, 0.0f);
    }

    public ShapePath(float startX, float startY) {
        reset(startX, startY);
    }

    public void reset(float startX, float startY) {
        reset(startX, startY, ANGLE_UP, 0.0f);
    }

    public void reset(float startX, float startY, float shadowStartAngle, float shadowSweepAngle) {
        this.startX = startX;
        this.startY = startY;
        this.endX = startX;
        this.endY = startY;
        this.currentShadowAngle = shadowStartAngle;
        this.endShadowAngle = (shadowStartAngle + shadowSweepAngle) % 360.0f;
        this.operations.clear();
        this.shadowCompatOperations.clear();
    }

    public void lineTo(float x, float y) {
        PathLineOperation operation = new PathLineOperation();
        operation.x = x;
        operation.y = y;
        this.operations.add(operation);
        LineShadowOperation shadowOperation = new LineShadowOperation(operation, this.endX, this.endY);
        addShadowCompatOperation(shadowOperation, shadowOperation.getAngle() + ANGLE_UP, shadowOperation.getAngle() + ANGLE_UP);
        this.endX = x;
        this.endY = y;
    }

    public void quadToPoint(float controlX, float controlY, float toX, float toY) {
        PathQuadOperation operation = new PathQuadOperation();
        operation.controlX = controlX;
        operation.controlY = controlY;
        operation.endX = toX;
        operation.endY = toY;
        this.operations.add(operation);
        this.endX = toX;
        this.endY = toY;
    }

    public void addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        PathArcOperation operation = new PathArcOperation(left, top, right, bottom);
        operation.startAngle = startAngle;
        operation.sweepAngle = sweepAngle;
        this.operations.add(operation);
        ArcShadowOperation arcShadowOperation = new ArcShadowOperation(operation);
        float endAngle = startAngle + sweepAngle;
        boolean drawShadowInsideBounds = sweepAngle < 0.0f;
        addShadowCompatOperation(arcShadowOperation, drawShadowInsideBounds ? (startAngle + ANGLE_LEFT) % 360.0f : startAngle, drawShadowInsideBounds ? (ANGLE_LEFT + endAngle) % 360.0f : endAngle);
        this.endX = ((left + right) * 0.5f) + (((right - left) / 2.0f) * ((float) Math.cos(Math.toRadians(startAngle + sweepAngle))));
        this.endY = ((top + bottom) * 0.5f) + (((bottom - top) / 2.0f) * ((float) Math.sin(Math.toRadians(startAngle + sweepAngle))));
    }

    public void applyToPath(Matrix transform, Path path) {
        int size = this.operations.size();
        for (int i = 0; i < size; i++) {
            PathOperation operation = this.operations.get(i);
            operation.applyToPath(transform, path);
        }
    }

    public ShadowCompatOperation createShadowCompatOperation(final Matrix transform) {
        addConnectingShadowIfNecessary(this.endShadowAngle);
        final List<ShadowCompatOperation> operations = new ArrayList<>(this.shadowCompatOperations);
        return new ShadowCompatOperation() { // from class: com.google.android.material.shape.ShapePath.1
            @Override // com.google.android.material.shape.ShapePath.ShadowCompatOperation
            public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
                for (ShadowCompatOperation op : operations) {
                    op.draw(transform, shadowRenderer, shadowElevation, canvas);
                }
            }
        };
    }

    private void addShadowCompatOperation(ShadowCompatOperation shadowOperation, float startShadowAngle, float endShadowAngle) {
        addConnectingShadowIfNecessary(startShadowAngle);
        this.shadowCompatOperations.add(shadowOperation);
        this.currentShadowAngle = endShadowAngle;
    }

    private void addConnectingShadowIfNecessary(float nextShadowAngle) {
        float f = this.currentShadowAngle;
        if (f != nextShadowAngle) {
            float shadowSweep = ((nextShadowAngle - f) + 360.0f) % 360.0f;
            if (shadowSweep <= ANGLE_LEFT) {
                float f2 = this.endX;
                float f3 = this.endY;
                PathArcOperation pathArcOperation = new PathArcOperation(f2, f3, f2, f3);
                pathArcOperation.startAngle = this.currentShadowAngle;
                pathArcOperation.sweepAngle = shadowSweep;
                this.shadowCompatOperations.add(new ArcShadowOperation(pathArcOperation));
                this.currentShadowAngle = nextShadowAngle;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class ShadowCompatOperation {
        static final Matrix IDENTITY_MATRIX = new Matrix();

        public abstract void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas);

        ShadowCompatOperation() {
        }

        public final void draw(ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            draw(IDENTITY_MATRIX, shadowRenderer, shadowElevation, canvas);
        }
    }

    /* loaded from: classes2.dex */
    public static class LineShadowOperation extends ShadowCompatOperation {
        private final PathLineOperation operation;
        private final float startX;
        private final float startY;

        public LineShadowOperation(PathLineOperation operation, float startX, float startY) {
            this.operation = operation;
            this.startX = startX;
            this.startY = startY;
        }

        @Override // com.google.android.material.shape.ShapePath.ShadowCompatOperation
        public void draw(Matrix transform, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            float height = this.operation.y - this.startY;
            float width = this.operation.x - this.startX;
            RectF rect = new RectF(0.0f, 0.0f, (float) Math.hypot(height, width), 0.0f);
            Matrix edgeTransform = new Matrix(transform);
            edgeTransform.preTranslate(this.startX, this.startY);
            edgeTransform.preRotate(getAngle());
            shadowRenderer.drawEdgeShadow(canvas, edgeTransform, rect, shadowElevation);
        }

        float getAngle() {
            return (float) Math.toDegrees(Math.atan((this.operation.y - this.startY) / (this.operation.x - this.startX)));
        }
    }

    /* loaded from: classes2.dex */
    public static class ArcShadowOperation extends ShadowCompatOperation {
        private final PathArcOperation operation;

        public ArcShadowOperation(PathArcOperation operation) {
            this.operation = operation;
        }

        @Override // com.google.android.material.shape.ShapePath.ShadowCompatOperation
        public void draw(Matrix transform, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            float startAngle = this.operation.startAngle;
            float sweepAngle = this.operation.sweepAngle;
            RectF rect = new RectF(this.operation.left, this.operation.top, this.operation.right, this.operation.bottom);
            shadowRenderer.drawCornerShadow(canvas, transform, rect, shadowElevation, startAngle, sweepAngle);
        }
    }

    /* loaded from: classes2.dex */
    public static class PathLineOperation extends PathOperation {
        private float x;
        private float y;

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            path.lineTo(this.x, this.y);
            path.transform(transform);
        }
    }

    /* loaded from: classes2.dex */
    public static class PathQuadOperation extends PathOperation {
        public float controlX;
        public float controlY;
        public float endX;
        public float endY;

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            path.quadTo(this.controlX, this.controlY, this.endX, this.endY);
            path.transform(transform);
        }
    }

    /* loaded from: classes2.dex */
    public static class PathArcOperation extends PathOperation {
        private static final RectF rectF = new RectF();
        public float bottom;
        public float left;
        public float right;
        public float startAngle;
        public float sweepAngle;
        public float top;

        public PathArcOperation(float left, float top, float right, float bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override // com.google.android.material.shape.ShapePath.PathOperation
        public void applyToPath(Matrix transform, Path path) {
            Matrix inverse = this.matrix;
            transform.invert(inverse);
            path.transform(inverse);
            rectF.set(this.left, this.top, this.right, this.bottom);
            path.arcTo(rectF, this.startAngle, this.sweepAngle, false);
            path.transform(transform);
        }
    }
}
