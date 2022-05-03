package com.google.android.material.shape;

/* loaded from: classes2.dex */
public class CornerTreatment implements Cloneable {
    protected float cornerSize;

    public CornerTreatment() {
        this.cornerSize = 0.0f;
    }

    public CornerTreatment(float cornerSize) {
        this.cornerSize = cornerSize;
    }

    public void getCornerPath(float angle, float interpolation, ShapePath shapePath) {
    }

    public float getCornerSize() {
        return this.cornerSize;
    }

    public void setCornerSize(float cornerSize) {
        this.cornerSize = cornerSize;
    }

    public CornerTreatment clone() {
        try {
            return (CornerTreatment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
