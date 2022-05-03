package com.google.android.material.resources;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes2.dex */
public class MaterialAttributes {
    public static TypedValue resolveAttributeOrThrow(View componentView, @AttrRes int attributeResId) {
        return resolveAttributeOrThrow(componentView.getContext(), attributeResId, componentView.getClass().getCanonicalName());
    }

    public static TypedValue resolveAttributeOrThrow(Context context, @AttrRes int attributeResId, String errorMessageComponent) {
        TypedValue typedValue = resolveAttribute(context, attributeResId);
        if (typedValue != null) {
            return typedValue;
        }
        throw new IllegalArgumentException(String.format("%1$s requires a value for the %2$s attribute to be set in your app theme. You can either set the attribute in your theme or update your theme to inherit from Theme.MaterialComponents (or a descendant).", errorMessageComponent, context.getResources().getResourceName(attributeResId)));
    }

    @Nullable
    public static TypedValue resolveAttribute(Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue;
        }
        return null;
    }
}
