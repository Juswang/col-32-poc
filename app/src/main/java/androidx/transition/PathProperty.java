package androidx.transition;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.Property;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class PathProperty<T> extends Property<T, Float> {
    private float mCurrentFraction;
    private final float mPathLength;
    private final PathMeasure mPathMeasure;
    private final Property<T, PointF> mProperty;
    private final float[] mPosition = new float[2];
    private final PointF mPointF = new PointF();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.util.Property
    public /* bridge */ /* synthetic */ void set(Object obj, Float f) {
        set2((PathProperty<T>) obj, f);
    }

    public PathProperty(Property<T, PointF> property, Path path) {
        super(Float.class, property.getName());
        this.mProperty = property;
        this.mPathMeasure = new PathMeasure(path, false);
        this.mPathLength = this.mPathMeasure.getLength();
    }

    @Override // android.util.Property
    public Float get(T object) {
        return Float.valueOf(this.mCurrentFraction);
    }

    /* renamed from: set */
    public void set2(T target, Float fraction) {
        this.mCurrentFraction = fraction.floatValue();
        this.mPathMeasure.getPosTan(this.mPathLength * fraction.floatValue(), this.mPosition, null);
        PointF pointF = this.mPointF;
        float[] fArr = this.mPosition;
        pointF.x = fArr[0];
        pointF.y = fArr[1];
        this.mProperty.set(target, pointF);
    }
}
