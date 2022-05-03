package com.google.android.material.switchmaterial;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;

/* loaded from: classes2.dex */
public class SwitchMaterial extends SwitchCompat {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CompoundButton_Switch;
    private static final int[][] ENABLED_CHECKED_STATES = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};
    @Nullable
    private ColorStateList materialThemeColorsThumbTintList;
    @Nullable
    private ColorStateList materialThemeColorsTrackTintList;

    public SwitchMaterial(Context context) {
        this(context, null);
    }

    public SwitchMaterial(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
    }

    public SwitchMaterial(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(ThemeEnforcement.createThemedContext(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        Context context2 = getContext();
        TypedArray attributes = ThemeEnforcement.obtainStyledAttributes(context2, attrs, R.styleable.SwitchMaterial, defStyleAttr, DEF_STYLE_RES, new int[0]);
        boolean useMaterialThemeColors = attributes.getBoolean(R.styleable.SwitchMaterial_useMaterialThemeColors, false);
        attributes.recycle();
        if (useMaterialThemeColors && getThumbTintList() == null) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
        }
        if (useMaterialThemeColors && getTrackTintList() == null) {
            setTrackTintList(getMaterialThemeColorsTrackTintList());
        }
    }

    public void setUseMaterialThemeColors(boolean useMaterialThemeColors) {
        if (useMaterialThemeColors) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
            setTrackTintList(getMaterialThemeColorsTrackTintList());
            return;
        }
        setThumbTintList(null);
        setTrackTintList(null);
    }

    public boolean isUseMaterialThemeColors() {
        ColorStateList colorStateList = this.materialThemeColorsThumbTintList;
        return colorStateList != null && this.materialThemeColorsTrackTintList != null && colorStateList.equals(getThumbTintList()) && this.materialThemeColorsTrackTintList.equals(getTrackTintList());
    }

    private ColorStateList getMaterialThemeColorsThumbTintList() {
        if (this.materialThemeColorsThumbTintList == null) {
            int colorSurface = MaterialColors.getColor(this, R.attr.colorSurface);
            int colorControlActivated = MaterialColors.getColor(this, R.attr.colorControlActivated);
            int[] switchThumbColorsList = new int[ENABLED_CHECKED_STATES.length];
            switchThumbColorsList[0] = MaterialColors.layer(colorSurface, colorControlActivated, 1.0f);
            switchThumbColorsList[1] = MaterialColors.layer(colorSurface, colorSurface, 1.0f);
            switchThumbColorsList[2] = MaterialColors.layer(colorSurface, colorControlActivated, 0.38f);
            switchThumbColorsList[3] = MaterialColors.layer(colorSurface, colorSurface, 1.0f);
            this.materialThemeColorsThumbTintList = new ColorStateList(ENABLED_CHECKED_STATES, switchThumbColorsList);
        }
        return this.materialThemeColorsThumbTintList;
    }

    private ColorStateList getMaterialThemeColorsTrackTintList() {
        if (this.materialThemeColorsTrackTintList == null) {
            int[] switchTrackColorsList = new int[ENABLED_CHECKED_STATES.length];
            int colorSurface = MaterialColors.getColor(this, R.attr.colorSurface);
            int colorControlActivated = MaterialColors.getColor(this, R.attr.colorControlActivated);
            int colorOnSurface = MaterialColors.getColor(this, R.attr.colorOnSurface);
            switchTrackColorsList[0] = MaterialColors.layer(colorSurface, colorControlActivated, 0.54f);
            switchTrackColorsList[1] = MaterialColors.layer(colorSurface, colorOnSurface, 0.32f);
            switchTrackColorsList[2] = MaterialColors.layer(colorSurface, colorControlActivated, 0.12f);
            switchTrackColorsList[3] = MaterialColors.layer(colorSurface, colorOnSurface, 0.12f);
            this.materialThemeColorsTrackTintList = new ColorStateList(ENABLED_CHECKED_STATES, switchTrackColorsList);
        }
        return this.materialThemeColorsTrackTintList;
    }
}
