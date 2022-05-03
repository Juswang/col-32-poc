package com.google.android.material.shape;

/* loaded from: classes2.dex */
public class CutCornerTreatment extends CornerTreatment implements Cloneable {
    public CutCornerTreatment(float size) {
        super(size);
    }

    @Override // com.google.android.material.shape.CornerTreatment
    public void getCornerPath(float angle, float interpolation, ShapePath shapePath) {
        shapePath.reset(0.0f, this.cornerSize * interpolation, 180.0f, 180.0f - angle);
        shapePath.lineTo((float) (Math.sin(Math.toRadians(angle)) * this.cornerSize * interpolation), (float) (Math.sin(Math.toRadians(90.0f - angle)) * this.cornerSize * interpolation));
    }
}
