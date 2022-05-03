package com.google.android.material.shape;

/* loaded from: classes2.dex */
public class RoundedCornerTreatment extends CornerTreatment implements Cloneable {
    public RoundedCornerTreatment(float radius) {
        super(radius);
    }

    @Override // com.google.android.material.shape.CornerTreatment
    public void getCornerPath(float angle, float interpolation, ShapePath shapePath) {
        float radius = this.cornerSize;
        shapePath.reset(0.0f, radius * interpolation, 180.0f, 180.0f - angle);
        shapePath.addArc(0.0f, 0.0f, radius * 2.0f * interpolation, 2.0f * radius * interpolation, 180.0f, angle);
    }
}
