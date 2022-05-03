package com.google.android.material.theme;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;

@Keep
/* loaded from: classes2.dex */
public class MaterialComponentsViewInflater extends AppCompatViewInflater {
    private static int floatingToolbarItemBackgroundResId = -1;

    @Override // androidx.appcompat.app.AppCompatViewInflater
    @NonNull
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        if (shouldInflateAppCompatButton(context, attrs)) {
            return new AppCompatButton(context, attrs);
        }
        return new MaterialButton(context, attrs);
    }

    protected boolean shouldInflateAppCompatButton(Context context, AttributeSet attrs) {
        if (!(Build.VERSION.SDK_INT == 23 || Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25)) {
            return false;
        }
        if (floatingToolbarItemBackgroundResId == -1) {
            floatingToolbarItemBackgroundResId = context.getResources().getIdentifier("floatingToolbarItemBackgroundDrawable", "^attr-private", "android");
        }
        int i = floatingToolbarItemBackgroundResId;
        if (!(i == 0 || i == -1)) {
            for (int i2 = 0; i2 < attrs.getAttributeCount(); i2++) {
                if (attrs.getAttributeNameResource(i2) == 16842964) {
                    int backgroundResourceId = attrs.getAttributeListValue(i2, null, 0);
                    if (floatingToolbarItemBackgroundResId == backgroundResourceId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatViewInflater
    @NonNull
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return new MaterialCheckBox(context, attrs);
    }

    @Override // androidx.appcompat.app.AppCompatViewInflater
    @NonNull
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return new MaterialRadioButton(context, attrs);
    }
}
