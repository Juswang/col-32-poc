package androidx.transition;

import android.animation.TypeEvaluator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class FloatArrayEvaluator implements TypeEvaluator<float[]> {
    private float[] mArray;

    public FloatArrayEvaluator(float[] reuseArray) {
        this.mArray = reuseArray;
    }

    public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
        float[] array = this.mArray;
        if (array == null) {
            array = new float[startValue.length];
        }
        for (int i = 0; i < array.length; i++) {
            float start = startValue[i];
            float end = endValue[i];
            array[i] = ((end - start) * fraction) + start;
        }
        return array;
    }
}
