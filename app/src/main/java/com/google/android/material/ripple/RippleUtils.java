package com.google.android.material.ripple;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.StateSet;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.graphics.ColorUtils;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class RippleUtils {
    private static final int[] FOCUSED_STATE_SET;
    private static final int[] HOVERED_FOCUSED_STATE_SET;
    private static final int[] HOVERED_STATE_SET;
    private static final int[] PRESSED_STATE_SET;
    private static final int[] SELECTED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_STATE_SET;
    private static final int[] SELECTED_PRESSED_STATE_SET;
    private static final int[] SELECTED_STATE_SET;
    public static final boolean USE_FRAMEWORK_RIPPLE;

    static {
        USE_FRAMEWORK_RIPPLE = Build.VERSION.SDK_INT >= 21;
        PRESSED_STATE_SET = new int[]{16842919};
        HOVERED_FOCUSED_STATE_SET = new int[]{16843623, 16842908};
        FOCUSED_STATE_SET = new int[]{16842908};
        HOVERED_STATE_SET = new int[]{16843623};
        SELECTED_PRESSED_STATE_SET = new int[]{16842913, 16842919};
        SELECTED_HOVERED_FOCUSED_STATE_SET = new int[]{16842913, 16843623, 16842908};
        SELECTED_FOCUSED_STATE_SET = new int[]{16842913, 16842908};
        SELECTED_HOVERED_STATE_SET = new int[]{16842913, 16843623};
        SELECTED_STATE_SET = new int[]{16842913};
    }

    private RippleUtils() {
    }

    @NonNull
    public static ColorStateList convertToRippleDrawableColor(@Nullable ColorStateList rippleColor) {
        if (USE_FRAMEWORK_RIPPLE) {
            int[][] states = new int[2];
            int[] colors = new int[2];
            states[0] = SELECTED_STATE_SET;
            colors[0] = getColorForState(rippleColor, SELECTED_PRESSED_STATE_SET);
            int i = 0 + 1;
            states[i] = StateSet.NOTHING;
            colors[i] = getColorForState(rippleColor, PRESSED_STATE_SET);
            int i2 = i + 1;
            return new ColorStateList(states, colors);
        }
        int[][] states2 = new int[10];
        int[] colors2 = new int[10];
        int[] iArr = SELECTED_PRESSED_STATE_SET;
        states2[0] = iArr;
        colors2[0] = getColorForState(rippleColor, iArr);
        int i3 = 0 + 1;
        int[] iArr2 = SELECTED_HOVERED_FOCUSED_STATE_SET;
        states2[i3] = iArr2;
        colors2[i3] = getColorForState(rippleColor, iArr2);
        int i4 = i3 + 1;
        int[] iArr3 = SELECTED_FOCUSED_STATE_SET;
        states2[i4] = iArr3;
        colors2[i4] = getColorForState(rippleColor, iArr3);
        int i5 = i4 + 1;
        int[] iArr4 = SELECTED_HOVERED_STATE_SET;
        states2[i5] = iArr4;
        colors2[i5] = getColorForState(rippleColor, iArr4);
        int i6 = i5 + 1;
        states2[i6] = SELECTED_STATE_SET;
        colors2[i6] = 0;
        int i7 = i6 + 1;
        int[] iArr5 = PRESSED_STATE_SET;
        states2[i7] = iArr5;
        colors2[i7] = getColorForState(rippleColor, iArr5);
        int i8 = i7 + 1;
        int[] iArr6 = HOVERED_FOCUSED_STATE_SET;
        states2[i8] = iArr6;
        colors2[i8] = getColorForState(rippleColor, iArr6);
        int i9 = i8 + 1;
        int[] iArr7 = FOCUSED_STATE_SET;
        states2[i9] = iArr7;
        colors2[i9] = getColorForState(rippleColor, iArr7);
        int i10 = i9 + 1;
        int[] iArr8 = HOVERED_STATE_SET;
        states2[i10] = iArr8;
        colors2[i10] = getColorForState(rippleColor, iArr8);
        int i11 = i10 + 1;
        states2[i11] = StateSet.NOTHING;
        colors2[i11] = 0;
        int i12 = i11 + 1;
        return new ColorStateList(states2, colors2);
    }

    @ColorInt
    private static int getColorForState(@Nullable ColorStateList rippleColor, int[] state) {
        int color;
        if (rippleColor != null) {
            color = rippleColor.getColorForState(state, rippleColor.getDefaultColor());
        } else {
            color = 0;
        }
        return USE_FRAMEWORK_RIPPLE ? doubleAlpha(color) : color;
    }

    @ColorInt
    @TargetApi(21)
    private static int doubleAlpha(@ColorInt int color) {
        int alpha = Math.min(Color.alpha(color) * 2, 255);
        return ColorUtils.setAlphaComponent(color, alpha);
    }
}
