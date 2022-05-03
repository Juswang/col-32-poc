package com.google.android.material.radiobutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;

/* loaded from: classes2.dex */
public class MaterialRadioButton extends AppCompatRadioButton {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CompoundButton_RadioButton;
    private static final int[][] ENABLED_CHECKED_STATES = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};
    @Nullable
    private ColorStateList materialThemeColorsTintList;

    public MaterialRadioButton(Context context) {
        this(context, null);
    }

    public MaterialRadioButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public MaterialRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(ThemeEnforcement.createThemedContext(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        Context context2 = getContext();
        TypedArray attributes = ThemeEnforcement.obtainStyledAttributes(context2, attrs, R.styleable.MaterialRadioButton, defStyleAttr, DEF_STYLE_RES, new int[0]);
        boolean useMaterialThemeColors = attributes.getBoolean(R.styleable.MaterialRadioButton_useMaterialThemeColors, false);
        attributes.recycle();
        if (useMaterialThemeColors && CompoundButtonCompat.getButtonTintList(this) == null) {
            setUseMaterialThemeColors(true);
        }
    }

    public void setUseMaterialThemeColors(boolean useMaterialThemeColors) {
        if (useMaterialThemeColors) {
            CompoundButtonCompat.setButtonTintList(this, getMaterialThemeColorsTintList());
        } else {
            CompoundButtonCompat.setButtonTintList(this, null);
        }
    }

    public boolean isUseMaterialThemeColors() {
        ColorStateList colorStateList = this.materialThemeColorsTintList;
        return colorStateList != null && colorStateList.equals(CompoundButtonCompat.getButtonTintList(this));
    }

    private ColorStateList getMaterialThemeColorsTintList() {
        if (this.materialThemeColorsTintList == null) {
            int colorControlActivated = MaterialColors.getColor(this, R.attr.colorControlActivated);
            int colorOnSurface = MaterialColors.getColor(this, R.attr.colorOnSurface);
            int colorSurface = MaterialColors.getColor(this, R.attr.colorSurface);
            int[] radioButtonColorList = new int[ENABLED_CHECKED_STATES.length];
            radioButtonColorList[0] = MaterialColors.layer(colorSurface, colorControlActivated, 1.0f);
            radioButtonColorList[1] = MaterialColors.layer(colorSurface, colorOnSurface, 0.54f);
            radioButtonColorList[2] = MaterialColors.layer(colorSurface, colorOnSurface, 0.38f);
            radioButtonColorList[3] = MaterialColors.layer(colorSurface, colorOnSurface, 0.38f);
            this.materialThemeColorsTintList = new ColorStateList(ENABLED_CHECKED_STATES, radioButtonColorList);
        }
        return this.materialThemeColorsTintList;
    }
}
