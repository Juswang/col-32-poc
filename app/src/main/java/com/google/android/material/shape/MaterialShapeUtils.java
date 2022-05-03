package com.google.android.material.shape;

import androidx.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class MaterialShapeUtils {
    public static CornerTreatment createCornerTreatment(int cornerFamily, int cornerSize) {
        if (cornerFamily == 0) {
            return new RoundedCornerTreatment(cornerSize);
        }
        if (cornerFamily != 1) {
            return createDefaultCornerTreatment();
        }
        return new CutCornerTreatment(cornerSize);
    }

    public static CornerTreatment createDefaultCornerTreatment() {
        return new RoundedCornerTreatment(0.0f);
    }

    public static EdgeTreatment createDefaultEdgeTreatment() {
        return new EdgeTreatment();
    }
}
