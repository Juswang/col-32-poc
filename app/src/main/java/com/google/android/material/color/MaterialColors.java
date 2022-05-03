package com.google.android.material.color;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.RestrictTo;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class MaterialColors {
    public static final float ALPHA_DISABLED = 0.38f;
    public static final float ALPHA_DISABLED_LOW = 0.12f;
    public static final float ALPHA_FULL = 1.0f;
    public static final float ALPHA_LOW = 0.32f;
    public static final float ALPHA_MEDIUM = 0.54f;

    @ColorInt
    public static int getColor(View view, @AttrRes int colorAttributeResId) {
        return MaterialAttributes.resolveAttributeOrThrow(view, colorAttributeResId).data;
    }

    @ColorInt
    public static int getColor(View view, @AttrRes int colorAttributeResId, @ColorInt int defaultValue) {
        TypedValue typedValue = MaterialAttributes.resolveAttribute(view.getContext(), colorAttributeResId);
        if (typedValue != null) {
            return typedValue.data;
        }
        return defaultValue;
    }

    @ColorInt
    public static int layer(View view, @AttrRes int backgroundColorAttributeResId, @AttrRes int overlayColorAttributeResId) {
        return layer(view, backgroundColorAttributeResId, overlayColorAttributeResId, 1.0f);
    }

    @ColorInt
    public static int layer(View view, @AttrRes int backgroundColorAttributeResId, @AttrRes int overlayColorAttributeResId, @FloatRange(from = 0.0d, to = 1.0d) float overlayAlpha) {
        int backgroundColor = getColor(view, backgroundColorAttributeResId);
        int overlayColor = getColor(view, overlayColorAttributeResId);
        return layer(backgroundColor, overlayColor, overlayAlpha);
    }

    @ColorInt
    public static int layer(@ColorInt int backgroundColor, @ColorInt int overlayColor, @FloatRange(from = 0.0d, to = 1.0d) float overlayAlpha) {
        int computedAlpha = Math.round(Color.alpha(overlayColor) * overlayAlpha);
        int computedOverlayColor = ColorUtils.setAlphaComponent(overlayColor, computedAlpha);
        return layer(backgroundColor, computedOverlayColor);
    }

    @ColorInt
    public static int layer(@ColorInt int backgroundColor, @ColorInt int overlayColor) {
        return ColorUtils.compositeColors(overlayColor, backgroundColor);
    }
}
