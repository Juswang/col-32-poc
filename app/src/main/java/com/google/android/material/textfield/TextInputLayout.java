package com.google.android.material.textfield;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

/* loaded from: classes2.dex */
public class TextInputLayout extends LinearLayout {
    public static final int BOX_BACKGROUND_FILLED = 1;
    public static final int BOX_BACKGROUND_NONE = 0;
    public static final int BOX_BACKGROUND_OUTLINE = 2;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_TextInputLayout;
    public static final int END_ICON_CLEAR_TEXT = 2;
    public static final int END_ICON_CUSTOM = -1;
    public static final int END_ICON_NONE = 0;
    public static final int END_ICON_PASSWORD_TOGGLE = 1;
    private static final int INVALID_MAX_LENGTH = -1;
    private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
    private static final String LOG_TAG = "TextInputLayout";
    private ValueAnimator animator;
    private MaterialShapeDrawable boxBackground;
    @ColorInt
    private int boxBackgroundColor;
    private int boxBackgroundMode;
    private final int boxCollapsedPaddingTopPx;
    private final int boxLabelCutoutPaddingPx;
    @ColorInt
    private int boxStrokeColor;
    private final int boxStrokeWidthDefaultPx;
    private final int boxStrokeWidthFocusedPx;
    private int boxStrokeWidthPx;
    private MaterialShapeDrawable boxUnderline;
    private final TextWatcher clearTextEndIconTextWatcher;
    private final OnEditTextAttachedListener clearTextOnEditTextAttachedListener;
    final CollapsingTextHelper collapsingTextHelper;
    private final ShapeAppearanceModel cornerAdjustedShapeAppearanceModel;
    boolean counterEnabled;
    private int counterMaxLength;
    private int counterOverflowTextAppearance;
    @Nullable
    private ColorStateList counterOverflowTextColor;
    private boolean counterOverflowed;
    private int counterTextAppearance;
    @Nullable
    private ColorStateList counterTextColor;
    private TextView counterView;
    @ColorInt
    private int defaultFilledBackgroundColor;
    private ColorStateList defaultHintTextColor;
    @ColorInt
    private final int defaultStrokeColor;
    @ColorInt
    private final int disabledColor;
    @ColorInt
    private final int disabledFilledBackgroundColor;
    EditText editText;
    private final LinkedHashSet<OnEditTextAttachedListener> editTextAttachedListeners;
    private final LinkedHashSet<OnEndIconChangedListener> endIconChangedListeners;
    private Drawable endIconDummyDrawable;
    private int endIconMode;
    private ColorStateList endIconTintList;
    private PorterDuff.Mode endIconTintMode;
    private CheckableImageButton endIconView;
    @ColorInt
    private int focusedStrokeColor;
    private ColorStateList focusedTextColor;
    private boolean hasEndIconTintList;
    private boolean hasEndIconTintMode;
    private CharSequence hint;
    private boolean hintAnimationEnabled;
    private boolean hintEnabled;
    private boolean hintExpanded;
    @ColorInt
    private final int hoveredFilledBackgroundColor;
    @ColorInt
    private final int hoveredStrokeColor;
    private boolean inDrawableStateChanged;
    private final IndicatorViewController indicatorViewController;
    private final FrameLayout inputFrame;
    private boolean isProvidingHint;
    private Drawable originalEditTextEndDrawable;
    private CharSequence originalHint;
    private final OnEndIconChangedListener passwordToggleEndIconChangedListener;
    private final OnEditTextAttachedListener passwordToggleOnEditTextAttachedListener;
    private boolean restoringSavedState;
    private final ShapeAppearanceModel shapeAppearanceModel;
    private final Rect tmpBoundsRect;
    private final Rect tmpRect;
    private final RectF tmpRectF;
    private Typeface typeface;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface BoxBackgroundMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface EndIconMode {
    }

    /* loaded from: classes2.dex */
    public interface OnEditTextAttachedListener {
        void onEditTextAttached();
    }

    /* loaded from: classes2.dex */
    public interface OnEndIconChangedListener {
        void onEndIconChanged(int i);
    }

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.textInputStyle);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(ThemeEnforcement.createThemedContext(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        this.indicatorViewController = new IndicatorViewController(this);
        this.tmpRect = new Rect();
        this.tmpBoundsRect = new Rect();
        this.tmpRectF = new RectF();
        this.editTextAttachedListeners = new LinkedHashSet<>();
        this.endIconMode = 0;
        this.endIconChangedListeners = new LinkedHashSet<>();
        this.passwordToggleOnEditTextAttachedListener = new OnEditTextAttachedListener() { // from class: com.google.android.material.textfield.TextInputLayout.1
            @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
            public void onEditTextAttached() {
                TextInputLayout textInputLayout = TextInputLayout.this;
                textInputLayout.setEndIconVisible(textInputLayout.hasPasswordTransformation());
            }
        };
        this.passwordToggleEndIconChangedListener = new OnEndIconChangedListener() { // from class: com.google.android.material.textfield.TextInputLayout.2
            @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
            public void onEndIconChanged(int previousIcon) {
                if (TextInputLayout.this.editText != null && previousIcon == 1) {
                    TextInputLayout.this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        };
        this.clearTextEndIconTextWatcher = new TextWatcher() { // from class: com.google.android.material.textfield.TextInputLayout.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                TextInputLayout.this.setEndIconVisible(s.length() > 0);
            }
        };
        this.clearTextOnEditTextAttachedListener = new OnEditTextAttachedListener() { // from class: com.google.android.material.textfield.TextInputLayout.4
            @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
            public void onEditTextAttached() {
                TextInputLayout textInputLayout = TextInputLayout.this;
                textInputLayout.setEndIconVisible(!TextUtils.isEmpty(textInputLayout.editText.getText()));
                TextInputLayout.this.editText.removeTextChangedListener(TextInputLayout.this.clearTextEndIconTextWatcher);
                TextInputLayout.this.editText.addTextChangedListener(TextInputLayout.this.clearTextEndIconTextWatcher);
            }
        };
        this.collapsingTextHelper = new CollapsingTextHelper(this);
        Context context2 = getContext();
        setOrientation(1);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        this.inputFrame = new FrameLayout(context2);
        this.inputFrame.setAddStatesFromChildren(true);
        addView(this.inputFrame);
        this.collapsingTextHelper.setTextSizeInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.collapsingTextHelper.setPositionInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.collapsingTextHelper.setCollapsedTextGravity(8388659);
        TintTypedArray a = ThemeEnforcement.obtainTintedStyledAttributes(context2, attrs, R.styleable.TextInputLayout, defStyleAttr, DEF_STYLE_RES, R.styleable.TextInputLayout_counterTextAppearance, R.styleable.TextInputLayout_counterOverflowTextAppearance, R.styleable.TextInputLayout_errorTextAppearance, R.styleable.TextInputLayout_helperTextTextAppearance, R.styleable.TextInputLayout_hintTextAppearance);
        this.hintEnabled = a.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        setHint(a.getText(R.styleable.TextInputLayout_android_hint));
        this.hintAnimationEnabled = a.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        this.shapeAppearanceModel = new ShapeAppearanceModel(context2, attrs, defStyleAttr, DEF_STYLE_RES);
        this.cornerAdjustedShapeAppearanceModel = new ShapeAppearanceModel(this.shapeAppearanceModel);
        this.boxLabelCutoutPaddingPx = context2.getResources().getDimensionPixelOffset(R.dimen.mtrl_textinput_box_label_cutout_padding);
        this.boxCollapsedPaddingTopPx = a.getDimensionPixelOffset(R.styleable.TextInputLayout_boxCollapsedPaddingTop, 0);
        this.boxStrokeWidthDefaultPx = context2.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_default);
        this.boxStrokeWidthFocusedPx = context2.getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_box_stroke_width_focused);
        this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
        float boxCornerRadiusTopStart = a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopStart, -1.0f);
        float boxCornerRadiusTopEnd = a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusTopEnd, -1.0f);
        float boxCornerRadiusBottomEnd = a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomEnd, -1.0f);
        float boxCornerRadiusBottomStart = a.getDimension(R.styleable.TextInputLayout_boxCornerRadiusBottomStart, -1.0f);
        if (boxCornerRadiusTopStart >= 0.0f) {
            this.shapeAppearanceModel.getTopLeftCorner().setCornerSize(boxCornerRadiusTopStart);
        }
        if (boxCornerRadiusTopEnd >= 0.0f) {
            this.shapeAppearanceModel.getTopRightCorner().setCornerSize(boxCornerRadiusTopEnd);
        }
        if (boxCornerRadiusBottomEnd >= 0.0f) {
            this.shapeAppearanceModel.getBottomRightCorner().setCornerSize(boxCornerRadiusBottomEnd);
        }
        if (boxCornerRadiusBottomStart >= 0.0f) {
            this.shapeAppearanceModel.getBottomLeftCorner().setCornerSize(boxCornerRadiusBottomStart);
        }
        adjustCornerSizeForStrokeWidth();
        ColorStateList filledBackgroundColorStateList = MaterialResources.getColorStateList(context2, a, R.styleable.TextInputLayout_boxBackgroundColor);
        if (filledBackgroundColorStateList != null) {
            this.defaultFilledBackgroundColor = filledBackgroundColorStateList.getDefaultColor();
            this.boxBackgroundColor = this.defaultFilledBackgroundColor;
            if (filledBackgroundColorStateList.isStateful()) {
                this.disabledFilledBackgroundColor = filledBackgroundColorStateList.getColorForState(new int[]{-16842910}, -1);
                this.hoveredFilledBackgroundColor = filledBackgroundColorStateList.getColorForState(new int[]{16843623}, -1);
            } else {
                ColorStateList mtrlFilledBackgroundColorStateList = AppCompatResources.getColorStateList(context2, R.color.mtrl_filled_background_color);
                this.disabledFilledBackgroundColor = mtrlFilledBackgroundColorStateList.getColorForState(new int[]{-16842910}, -1);
                this.hoveredFilledBackgroundColor = mtrlFilledBackgroundColorStateList.getColorForState(new int[]{16843623}, -1);
            }
        } else {
            this.boxBackgroundColor = 0;
            this.defaultFilledBackgroundColor = 0;
            this.disabledFilledBackgroundColor = 0;
            this.hoveredFilledBackgroundColor = 0;
        }
        if (a.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            ColorStateList colorStateList = a.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.focusedTextColor = colorStateList;
            this.defaultHintTextColor = colorStateList;
        }
        ColorStateList boxStrokeColorStateList = MaterialResources.getColorStateList(context2, a, R.styleable.TextInputLayout_boxStrokeColor);
        if (boxStrokeColorStateList == null || !boxStrokeColorStateList.isStateful()) {
            this.focusedStrokeColor = a.getColor(R.styleable.TextInputLayout_boxStrokeColor, 0);
            this.defaultStrokeColor = ContextCompat.getColor(context2, R.color.mtrl_textinput_default_box_stroke_color);
            this.disabledColor = ContextCompat.getColor(context2, R.color.mtrl_textinput_disabled_color);
            this.hoveredStrokeColor = ContextCompat.getColor(context2, R.color.mtrl_textinput_hovered_box_stroke_color);
        } else {
            this.defaultStrokeColor = boxStrokeColorStateList.getDefaultColor();
            this.disabledColor = boxStrokeColorStateList.getColorForState(new int[]{-16842910}, -1);
            this.hoveredStrokeColor = boxStrokeColorStateList.getColorForState(new int[]{16843623}, -1);
            this.focusedStrokeColor = boxStrokeColorStateList.getColorForState(new int[]{16842908}, -1);
        }
        int hintAppearance = a.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1);
        if (hintAppearance != -1) {
            setHintTextAppearance(a.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        int errorTextAppearance = a.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        boolean errorEnabled = a.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        int helperTextTextAppearance = a.getResourceId(R.styleable.TextInputLayout_helperTextTextAppearance, 0);
        boolean helperTextEnabled = a.getBoolean(R.styleable.TextInputLayout_helperTextEnabled, false);
        CharSequence helperText = a.getText(R.styleable.TextInputLayout_helperText);
        boolean counterEnabled = a.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        setCounterMaxLength(a.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.counterTextAppearance = a.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.counterOverflowTextAppearance = a.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        this.endIconView = (CheckableImageButton) LayoutInflater.from(getContext()).inflate(R.layout.design_text_input_end_icon, (ViewGroup) this.inputFrame, false);
        this.inputFrame.addView(this.endIconView);
        this.endIconView.setVisibility(8);
        if (a.hasValue(R.styleable.TextInputLayout_endIconMode)) {
            setEndIconMode(a.getInt(R.styleable.TextInputLayout_endIconMode, 0));
            if (a.hasValue(R.styleable.TextInputLayout_endIconDrawable)) {
                setEndIconDrawable(a.getDrawable(R.styleable.TextInputLayout_endIconDrawable));
            }
            if (a.hasValue(R.styleable.TextInputLayout_endIconContentDescription)) {
                setEndIconContentDescription(a.getText(R.styleable.TextInputLayout_endIconContentDescription));
            }
        } else if (a.hasValue(R.styleable.TextInputLayout_passwordToggleEnabled)) {
            setEndIconMode(1);
            setEndIconDrawable(a.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable));
            setEndIconContentDescription(a.getText(R.styleable.TextInputLayout_passwordToggleContentDescription));
        }
        if (a.hasValue(R.styleable.TextInputLayout_endIconTint)) {
            setEndIconTintList(AppCompatResources.getColorStateList(context2, a.getResourceId(R.styleable.TextInputLayout_endIconTint, -1)));
        }
        if (a.hasValue(R.styleable.TextInputLayout_endIconTintMode)) {
            setEndIconTintMode(ViewUtils.parseTintMode(a.getInt(R.styleable.TextInputLayout_endIconTintMode, -1), null));
        }
        if (a.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            setEndIconTintList(AppCompatResources.getColorStateList(context2, a.getResourceId(R.styleable.TextInputLayout_passwordToggleTint, -1)));
        }
        if (a.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            setEndIconTintMode(ViewUtils.parseTintMode(a.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null));
        }
        setHelperTextEnabled(helperTextEnabled);
        setHelperText(helperText);
        setHelperTextTextAppearance(helperTextTextAppearance);
        setErrorEnabled(errorEnabled);
        setErrorTextAppearance(errorTextAppearance);
        setCounterTextAppearance(this.counterTextAppearance);
        setCounterOverflowTextAppearance(this.counterOverflowTextAppearance);
        if (a.hasValue(R.styleable.TextInputLayout_errorTextColor)) {
            setErrorTextColor(a.getColorStateList(R.styleable.TextInputLayout_errorTextColor));
        }
        if (a.hasValue(R.styleable.TextInputLayout_helperTextTextColor)) {
            setHelperTextColor(a.getColorStateList(R.styleable.TextInputLayout_helperTextTextColor));
        }
        if (a.hasValue(R.styleable.TextInputLayout_hintTextColor)) {
            setHintTextColor(a.getColorStateList(R.styleable.TextInputLayout_hintTextColor));
        }
        if (a.hasValue(R.styleable.TextInputLayout_counterTextColor)) {
            setCounterTextColor(a.getColorStateList(R.styleable.TextInputLayout_counterTextColor));
        }
        if (a.hasValue(R.styleable.TextInputLayout_counterOverflowTextColor)) {
            setCounterOverflowTextColor(a.getColorStateList(R.styleable.TextInputLayout_counterOverflowTextColor));
        }
        setCounterEnabled(counterEnabled);
        setBoxBackgroundMode(a.getInt(R.styleable.TextInputLayout_boxBackgroundMode, 0));
        a.recycle();
        ViewCompat.setImportantForAccessibility(this, 2);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(params);
            flp.gravity = (flp.gravity & (-113)) | 16;
            this.inputFrame.addView(child, flp);
            this.inputFrame.setLayoutParams(params);
            updateInputLayoutMargins();
            setEditText((EditText) child);
            return;
        }
        super.addView(child, index, params);
    }

    @NonNull
    private Drawable getBoxBackground() {
        int i = this.boxBackgroundMode;
        if (i == 1 || i == 2) {
            return this.boxBackground;
        }
        throw new IllegalStateException();
    }

    public void setBoxBackgroundMode(int boxBackgroundMode) {
        if (boxBackgroundMode != this.boxBackgroundMode) {
            this.boxBackgroundMode = boxBackgroundMode;
            if (this.editText != null) {
                onApplyBoxBackgroundMode();
            }
        }
    }

    public int getBoxBackgroundMode() {
        return this.boxBackgroundMode;
    }

    private void onApplyBoxBackgroundMode() {
        assignBoxBackgroundByMode();
        setEditTextBoxBackground();
        updateTextInputBoxState();
        if (this.boxBackgroundMode != 0) {
            updateInputLayoutMargins();
        }
    }

    private void assignBoxBackgroundByMode() {
        int i = this.boxBackgroundMode;
        if (i == 0) {
            this.boxBackground = null;
            this.boxUnderline = null;
        } else if (i == 1) {
            this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
            this.boxUnderline = new MaterialShapeDrawable();
        } else if (i == 2) {
            if (!this.hintEnabled || (this.boxBackground instanceof CutoutDrawable)) {
                this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
            } else {
                this.boxBackground = new CutoutDrawable(this.shapeAppearanceModel);
            }
            this.boxUnderline = null;
        } else {
            throw new IllegalArgumentException(this.boxBackgroundMode + " is illegal; only @BoxBackgroundMode constants are supported.");
        }
    }

    private void setEditTextBoxBackground() {
        if (shouldUseEditTextBackgroundForBoxBackground()) {
            ViewCompat.setBackground(this.editText, this.boxBackground);
        }
    }

    private boolean shouldUseEditTextBackgroundForBoxBackground() {
        EditText editText = this.editText;
        return (editText == null || this.boxBackground == null || editText.getBackground() != null || this.boxBackgroundMode == 0) ? false : true;
    }

    public void setBoxStrokeColor(@ColorInt int boxStrokeColor) {
        if (this.focusedStrokeColor != boxStrokeColor) {
            this.focusedStrokeColor = boxStrokeColor;
            updateTextInputBoxState();
        }
    }

    public int getBoxStrokeColor() {
        return this.focusedStrokeColor;
    }

    public void setBoxBackgroundColorResource(@ColorRes int boxBackgroundColorId) {
        setBoxBackgroundColor(ContextCompat.getColor(getContext(), boxBackgroundColorId));
    }

    public void setBoxBackgroundColor(@ColorInt int boxBackgroundColor) {
        if (this.boxBackgroundColor != boxBackgroundColor) {
            this.boxBackgroundColor = boxBackgroundColor;
            this.defaultFilledBackgroundColor = boxBackgroundColor;
            applyBoxAttributes();
        }
    }

    public int getBoxBackgroundColor() {
        return this.boxBackgroundColor;
    }

    public void setBoxCornerRadiiResources(@DimenRes int boxCornerRadiusTopStartId, @DimenRes int boxCornerRadiusTopEndId, @DimenRes int boxCornerRadiusBottomEndId, @DimenRes int boxCornerRadiusBottomStartId) {
        setBoxCornerRadii(getContext().getResources().getDimension(boxCornerRadiusTopStartId), getContext().getResources().getDimension(boxCornerRadiusTopEndId), getContext().getResources().getDimension(boxCornerRadiusBottomStartId), getContext().getResources().getDimension(boxCornerRadiusBottomEndId));
    }

    public void setBoxCornerRadii(float boxCornerRadiusTopStart, float boxCornerRadiusTopEnd, float boxCornerRadiusBottomStart, float boxCornerRadiusBottomEnd) {
        if (this.shapeAppearanceModel.getTopLeftCorner().getCornerSize() != boxCornerRadiusTopStart || this.shapeAppearanceModel.getTopRightCorner().getCornerSize() != boxCornerRadiusTopEnd || this.shapeAppearanceModel.getBottomRightCorner().getCornerSize() != boxCornerRadiusBottomEnd || this.shapeAppearanceModel.getBottomLeftCorner().getCornerSize() != boxCornerRadiusBottomStart) {
            this.shapeAppearanceModel.getTopLeftCorner().setCornerSize(boxCornerRadiusTopStart);
            this.shapeAppearanceModel.getTopRightCorner().setCornerSize(boxCornerRadiusTopEnd);
            this.shapeAppearanceModel.getBottomRightCorner().setCornerSize(boxCornerRadiusBottomEnd);
            this.shapeAppearanceModel.getBottomLeftCorner().setCornerSize(boxCornerRadiusBottomStart);
            applyBoxAttributes();
        }
    }

    public float getBoxCornerRadiusTopStart() {
        return this.shapeAppearanceModel.getTopLeftCorner().getCornerSize();
    }

    public float getBoxCornerRadiusTopEnd() {
        return this.shapeAppearanceModel.getTopRightCorner().getCornerSize();
    }

    public float getBoxCornerRadiusBottomEnd() {
        return this.shapeAppearanceModel.getBottomLeftCorner().getCornerSize();
    }

    public float getBoxCornerRadiusBottomStart() {
        return this.shapeAppearanceModel.getBottomRightCorner().getCornerSize();
    }

    private void adjustCornerSizeForStrokeWidth() {
        float strokeInset = this.boxBackgroundMode == 2 ? this.boxStrokeWidthPx / 2.0f : 0.0f;
        if (strokeInset > 0.0f) {
            float cornerRadiusTopLeft = this.shapeAppearanceModel.getTopLeftCorner().getCornerSize();
            this.cornerAdjustedShapeAppearanceModel.getTopLeftCorner().setCornerSize(cornerRadiusTopLeft + strokeInset);
            float cornerRadiusTopRight = this.shapeAppearanceModel.getTopRightCorner().getCornerSize();
            this.cornerAdjustedShapeAppearanceModel.getTopRightCorner().setCornerSize(cornerRadiusTopRight + strokeInset);
            float cornerRadiusBottomRight = this.shapeAppearanceModel.getBottomRightCorner().getCornerSize();
            this.cornerAdjustedShapeAppearanceModel.getBottomRightCorner().setCornerSize(cornerRadiusBottomRight + strokeInset);
            float cornerRadiusBottomLeft = this.shapeAppearanceModel.getBottomLeftCorner().getCornerSize();
            this.cornerAdjustedShapeAppearanceModel.getBottomLeftCorner().setCornerSize(cornerRadiusBottomLeft + strokeInset);
            ensureCornerAdjustedShapeAppearanceModel();
        }
    }

    private void ensureCornerAdjustedShapeAppearanceModel() {
        if (this.boxBackgroundMode != 0 && (getBoxBackground() instanceof MaterialShapeDrawable)) {
            ((MaterialShapeDrawable) getBoxBackground()).setShapeAppearanceModel(this.cornerAdjustedShapeAppearanceModel);
        }
    }

    public void setTypeface(@Nullable Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.collapsingTextHelper.setTypefaces(typeface);
            this.indicatorViewController.setTypefaces(typeface);
            TextView textView = this.counterView;
            if (textView != null) {
                textView.setTypeface(typeface);
            }
        }
    }

    @Nullable
    public Typeface getTypeface() {
        return this.typeface;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        EditText editText;
        if (this.originalHint == null || (editText = this.editText) == null) {
            super.dispatchProvideAutofillStructure(structure, flags);
            return;
        }
        boolean wasProvidingHint = this.isProvidingHint;
        this.isProvidingHint = false;
        CharSequence hint = editText.getHint();
        this.editText.setHint(this.originalHint);
        try {
            super.dispatchProvideAutofillStructure(structure, flags);
        } finally {
            this.editText.setHint(hint);
            this.isProvidingHint = wasProvidingHint;
        }
    }

    private void setEditText(EditText editText) {
        if (this.editText == null) {
            if (!(editText instanceof TextInputEditText)) {
                Log.i(LOG_TAG, "EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.editText = editText;
            onApplyBoxBackgroundMode();
            setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
            this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
            this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
            int editTextGravity = this.editText.getGravity();
            this.collapsingTextHelper.setCollapsedTextGravity((editTextGravity & (-113)) | 48);
            this.collapsingTextHelper.setExpandedTextGravity(editTextGravity);
            this.editText.addTextChangedListener(new TextWatcher() { // from class: com.google.android.material.textfield.TextInputLayout.5
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                    TextInputLayout textInputLayout = TextInputLayout.this;
                    textInputLayout.updateLabelState(!textInputLayout.restoringSavedState);
                    if (TextInputLayout.this.counterEnabled) {
                        TextInputLayout.this.updateCounter(s.length());
                    }
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            if (this.defaultHintTextColor == null) {
                this.defaultHintTextColor = this.editText.getHintTextColors();
            }
            if (this.hintEnabled) {
                if (TextUtils.isEmpty(this.hint)) {
                    this.originalHint = this.editText.getHint();
                    setHint(this.originalHint);
                    this.editText.setHint((CharSequence) null);
                }
                this.isProvidingHint = true;
            }
            if (this.counterView != null) {
                updateCounter(this.editText.getText().length());
            }
            updateEditTextBackground();
            this.indicatorViewController.adjustIndicatorPadding();
            ViewCompat.setPaddingRelative(this.endIconView, getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_end_icon_padding_start), this.editText.getPaddingTop(), getResources().getDimensionPixelSize(R.dimen.mtrl_textinput_end_icon_padding_end), this.editText.getPaddingBottom());
            this.endIconView.bringToFront();
            dispatchOnEditTextAttached();
            updateLabelState(false, true);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private void updateInputLayoutMargins() {
        if (this.boxBackgroundMode != 1) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.inputFrame.getLayoutParams();
            int newTopMargin = calculateLabelMarginTop();
            if (newTopMargin != lp.topMargin) {
                lp.topMargin = newTopMargin;
                this.inputFrame.requestLayout();
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public int getBaseline() {
        EditText editText = this.editText;
        if (editText != null) {
            return editText.getBaseline() + getPaddingTop() + calculateLabelMarginTop();
        }
        return super.getBaseline();
    }

    public void updateLabelState(boolean animate) {
        updateLabelState(animate, false);
    }

    private void updateLabelState(boolean animate, boolean force) {
        ColorStateList colorStateList;
        TextView textView;
        boolean isEnabled = isEnabled();
        EditText editText = this.editText;
        boolean hasFocus = true;
        boolean hasText = editText != null && !TextUtils.isEmpty(editText.getText());
        EditText editText2 = this.editText;
        if (editText2 == null || !editText2.hasFocus()) {
            hasFocus = false;
        }
        boolean errorShouldBeShown = this.indicatorViewController.errorShouldBeShown();
        ColorStateList colorStateList2 = this.defaultHintTextColor;
        if (colorStateList2 != null) {
            this.collapsingTextHelper.setCollapsedTextColor(colorStateList2);
            this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
        }
        if (!isEnabled) {
            this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf(this.disabledColor));
            this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf(this.disabledColor));
        } else if (errorShouldBeShown) {
            this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
        } else if (this.counterOverflowed && (textView = this.counterView) != null) {
            this.collapsingTextHelper.setCollapsedTextColor(textView.getTextColors());
        } else if (hasFocus && (colorStateList = this.focusedTextColor) != null) {
            this.collapsingTextHelper.setCollapsedTextColor(colorStateList);
        }
        if (hasText || (isEnabled() && (hasFocus || errorShouldBeShown))) {
            if (force || this.hintExpanded) {
                collapseHint(animate);
            }
        } else if (force || !this.hintExpanded) {
            expandHint(animate);
        }
    }

    @Nullable
    public EditText getEditText() {
        return this.editText;
    }

    public void setHint(@Nullable CharSequence hint) {
        if (this.hintEnabled) {
            setHintInternal(hint);
            sendAccessibilityEvent(2048);
        }
    }

    private void setHintInternal(CharSequence hint) {
        if (!TextUtils.equals(hint, this.hint)) {
            this.hint = hint;
            this.collapsingTextHelper.setText(hint);
            if (!this.hintExpanded) {
                openCutout();
            }
        }
    }

    @Nullable
    public CharSequence getHint() {
        if (this.hintEnabled) {
            return this.hint;
        }
        return null;
    }

    public void setHintEnabled(boolean enabled) {
        if (enabled != this.hintEnabled) {
            this.hintEnabled = enabled;
            if (!this.hintEnabled) {
                this.isProvidingHint = false;
                if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint())) {
                    this.editText.setHint(this.hint);
                }
                setHintInternal(null);
            } else {
                CharSequence editTextHint = this.editText.getHint();
                if (!TextUtils.isEmpty(editTextHint)) {
                    if (TextUtils.isEmpty(this.hint)) {
                        setHint(editTextHint);
                    }
                    this.editText.setHint((CharSequence) null);
                }
                this.isProvidingHint = true;
            }
            if (this.editText != null) {
                updateInputLayoutMargins();
            }
        }
    }

    public boolean isHintEnabled() {
        return this.hintEnabled;
    }

    public boolean isProvidingHint() {
        return this.isProvidingHint;
    }

    public void setHintTextAppearance(@StyleRes int resId) {
        this.collapsingTextHelper.setCollapsedTextAppearance(resId);
        this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
        if (this.editText != null) {
            updateLabelState(false);
            updateInputLayoutMargins();
        }
    }

    public void setHintTextColor(@Nullable ColorStateList hintTextColor) {
        if (this.collapsingTextHelper.getCollapsedTextColor() != hintTextColor) {
            this.collapsingTextHelper.setCollapsedTextColor(hintTextColor);
            this.focusedTextColor = hintTextColor;
            if (this.editText != null) {
                updateLabelState(false);
            }
        }
    }

    @Nullable
    public ColorStateList getHintTextColor() {
        return this.collapsingTextHelper.getCollapsedTextColor();
    }

    public void setDefaultHintTextColor(@Nullable ColorStateList textColor) {
        this.defaultHintTextColor = textColor;
        this.focusedTextColor = textColor;
        if (this.editText != null) {
            updateLabelState(false);
        }
    }

    @Nullable
    public ColorStateList getDefaultHintTextColor() {
        return this.defaultHintTextColor;
    }

    public void setErrorEnabled(boolean enabled) {
        this.indicatorViewController.setErrorEnabled(enabled);
    }

    public void setErrorTextAppearance(@StyleRes int errorTextAppearance) {
        this.indicatorViewController.setErrorTextAppearance(errorTextAppearance);
    }

    public void setErrorTextColor(@Nullable ColorStateList errorTextColor) {
        this.indicatorViewController.setErrorViewTextColor(errorTextColor);
    }

    @ColorInt
    public int getErrorCurrentTextColors() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    public void setHelperTextTextAppearance(@StyleRes int helperTextTextAppearance) {
        this.indicatorViewController.setHelperTextAppearance(helperTextTextAppearance);
    }

    public void setHelperTextColor(@Nullable ColorStateList helperTextColor) {
        this.indicatorViewController.setHelperTextViewTextColor(helperTextColor);
    }

    public boolean isErrorEnabled() {
        return this.indicatorViewController.isErrorEnabled();
    }

    public void setHelperTextEnabled(boolean enabled) {
        this.indicatorViewController.setHelperTextEnabled(enabled);
    }

    public void setHelperText(@Nullable CharSequence helperText) {
        if (!TextUtils.isEmpty(helperText)) {
            if (!isHelperTextEnabled()) {
                setHelperTextEnabled(true);
            }
            this.indicatorViewController.showHelper(helperText);
        } else if (isHelperTextEnabled()) {
            setHelperTextEnabled(false);
        }
    }

    public boolean isHelperTextEnabled() {
        return this.indicatorViewController.isHelperTextEnabled();
    }

    @ColorInt
    public int getHelperTextCurrentTextColor() {
        return this.indicatorViewController.getHelperTextViewCurrentTextColor();
    }

    public void setError(@Nullable CharSequence errorText) {
        if (!this.indicatorViewController.isErrorEnabled()) {
            if (!TextUtils.isEmpty(errorText)) {
                setErrorEnabled(true);
            } else {
                return;
            }
        }
        if (!TextUtils.isEmpty(errorText)) {
            this.indicatorViewController.showError(errorText);
        } else {
            this.indicatorViewController.hideError();
        }
    }

    public void setCounterEnabled(boolean enabled) {
        if (this.counterEnabled != enabled) {
            if (enabled) {
                this.counterView = new AppCompatTextView(getContext());
                this.counterView.setId(R.id.textinput_counter);
                Typeface typeface = this.typeface;
                if (typeface != null) {
                    this.counterView.setTypeface(typeface);
                }
                this.counterView.setMaxLines(1);
                this.indicatorViewController.addIndicator(this.counterView, 2);
                updateCounterTextAppearanceAndColor();
                updateCounter();
            } else {
                this.indicatorViewController.removeIndicator(this.counterView, 2);
                this.counterView = null;
            }
            this.counterEnabled = enabled;
        }
    }

    public void setCounterTextAppearance(int counterTextAppearance) {
        if (this.counterTextAppearance != counterTextAppearance) {
            this.counterTextAppearance = counterTextAppearance;
            updateCounterTextAppearanceAndColor();
        }
    }

    public void setCounterTextColor(@Nullable ColorStateList counterTextColor) {
        if (this.counterTextColor != counterTextColor) {
            this.counterTextColor = counterTextColor;
            updateCounterTextAppearanceAndColor();
        }
    }

    @Nullable
    public ColorStateList getCounterTextColor() {
        return this.counterTextColor;
    }

    public void setCounterOverflowTextAppearance(int counterOverflowTextAppearance) {
        if (this.counterOverflowTextAppearance != counterOverflowTextAppearance) {
            this.counterOverflowTextAppearance = counterOverflowTextAppearance;
            updateCounterTextAppearanceAndColor();
        }
    }

    public void setCounterOverflowTextColor(@Nullable ColorStateList counterOverflowTextColor) {
        if (this.counterOverflowTextColor != counterOverflowTextColor) {
            this.counterOverflowTextColor = counterOverflowTextColor;
            updateCounterTextAppearanceAndColor();
        }
    }

    @Nullable
    public ColorStateList getCounterOverflowTextColor() {
        return this.counterTextColor;
    }

    public boolean isCounterEnabled() {
        return this.counterEnabled;
    }

    public void setCounterMaxLength(int maxLength) {
        if (this.counterMaxLength != maxLength) {
            if (maxLength > 0) {
                this.counterMaxLength = maxLength;
            } else {
                this.counterMaxLength = -1;
            }
            if (this.counterEnabled) {
                updateCounter();
            }
        }
    }

    private void updateCounter() {
        if (this.counterView != null) {
            EditText editText = this.editText;
            updateCounter(editText == null ? 0 : editText.getText().length());
        }
    }

    void updateCounter(int length) {
        boolean wasCounterOverflowed = this.counterOverflowed;
        if (this.counterMaxLength == -1) {
            this.counterView.setText(String.valueOf(length));
            this.counterView.setContentDescription(null);
            this.counterOverflowed = false;
        } else {
            if (ViewCompat.getAccessibilityLiveRegion(this.counterView) == 1) {
                ViewCompat.setAccessibilityLiveRegion(this.counterView, 0);
            }
            this.counterOverflowed = length > this.counterMaxLength;
            updateCounterContentDescription(getContext(), this.counterView, length, this.counterMaxLength, this.counterOverflowed);
            if (wasCounterOverflowed != this.counterOverflowed) {
                updateCounterTextAppearanceAndColor();
                if (this.counterOverflowed) {
                    ViewCompat.setAccessibilityLiveRegion(this.counterView, 1);
                }
            }
            this.counterView.setText(getContext().getString(R.string.character_counter_pattern, Integer.valueOf(length), Integer.valueOf(this.counterMaxLength)));
        }
        if (this.editText != null && wasCounterOverflowed != this.counterOverflowed) {
            updateLabelState(false);
            updateTextInputBoxState();
            updateEditTextBackground();
        }
    }

    private static void updateCounterContentDescription(Context context, TextView counterView, int length, int counterMaxLength, boolean counterOverflowed) {
        counterView.setContentDescription(context.getString(counterOverflowed ? R.string.character_counter_overflowed_content_description : R.string.character_counter_content_description, Integer.valueOf(length), Integer.valueOf(counterMaxLength)));
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        recursiveSetEnabled(this, enabled);
        super.setEnabled(enabled);
    }

    private static void recursiveSetEnabled(ViewGroup vg, boolean enabled) {
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup) child, enabled);
            }
        }
    }

    public int getCounterMaxLength() {
        return this.counterMaxLength;
    }

    @Nullable
    CharSequence getCounterOverflowDescription() {
        TextView textView;
        if (!this.counterEnabled || !this.counterOverflowed || (textView = this.counterView) == null) {
            return null;
        }
        return textView.getContentDescription();
    }

    private void updateCounterTextAppearanceAndColor() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        TextView textView = this.counterView;
        if (textView != null) {
            setTextAppearanceCompatWithErrorFallback(textView, this.counterOverflowed ? this.counterOverflowTextAppearance : this.counterTextAppearance);
            if (!this.counterOverflowed && (colorStateList2 = this.counterTextColor) != null) {
                this.counterView.setTextColor(colorStateList2);
            }
            if (this.counterOverflowed && (colorStateList = this.counterOverflowTextColor) != null) {
                this.counterView.setTextColor(colorStateList);
            }
        }
    }

    public void setTextAppearanceCompatWithErrorFallback(TextView textView, @StyleRes int textAppearance) {
        boolean useDefaultColor = false;
        try {
            TextViewCompat.setTextAppearance(textView, textAppearance);
            if (Build.VERSION.SDK_INT >= 23) {
                if (textView.getTextColors().getDefaultColor() == -65281) {
                    useDefaultColor = true;
                }
            }
        } catch (Exception e) {
            useDefaultColor = true;
        }
        if (useDefaultColor) {
            TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Caption);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.design_error));
        }
    }

    private int calculateLabelMarginTop() {
        if (!this.hintEnabled) {
            return 0;
        }
        int i = this.boxBackgroundMode;
        if (i == 0 || i == 1) {
            return (int) this.collapsingTextHelper.getCollapsedTextHeight();
        }
        if (i != 2) {
            return 0;
        }
        return (int) (this.collapsingTextHelper.getCollapsedTextHeight() / 2.0f);
    }

    private Rect calculateCollapsedTextBounds(Rect rect) {
        if (this.editText != null) {
            Rect bounds = this.tmpBoundsRect;
            bounds.bottom = rect.bottom;
            int i = this.boxBackgroundMode;
            if (i == 1) {
                bounds.left = rect.left + this.editText.getCompoundPaddingLeft();
                bounds.top = rect.top + this.boxCollapsedPaddingTopPx;
                bounds.right = rect.right - this.editText.getCompoundPaddingRight();
                return bounds;
            } else if (i != 2) {
                bounds.left = rect.left + this.editText.getCompoundPaddingLeft();
                bounds.top = getPaddingTop();
                bounds.right = rect.right - this.editText.getCompoundPaddingRight();
                return bounds;
            } else {
                bounds.left = rect.left + this.editText.getPaddingLeft();
                bounds.top = rect.top - calculateLabelMarginTop();
                bounds.right = rect.right - this.editText.getPaddingRight();
                return bounds;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private Rect calculateExpandedTextBounds(Rect rect) {
        if (this.editText != null) {
            Rect bounds = this.tmpBoundsRect;
            bounds.left = rect.left + this.editText.getCompoundPaddingLeft();
            bounds.top = rect.top + this.editText.getCompoundPaddingTop();
            bounds.right = rect.right - this.editText.getCompoundPaddingRight();
            bounds.bottom = rect.bottom - this.editText.getCompoundPaddingBottom();
            return bounds;
        }
        throw new IllegalStateException();
    }

    private int calculateBoxBackgroundColor() {
        int backgroundColor = this.boxBackgroundColor;
        if (this.boxBackgroundMode != 1) {
            return backgroundColor;
        }
        int surfaceLayerColor = MaterialColors.getColor(this, R.attr.colorSurface, 0);
        int backgroundColor2 = MaterialColors.layer(surfaceLayerColor, this.boxBackgroundColor);
        return backgroundColor2;
    }

    private void applyBoxAttributes() {
        if (this.boxBackground != null) {
            if (canDrawOutlineStroke()) {
                this.boxBackground.setStroke(this.boxStrokeWidthPx, this.boxStrokeColor);
            }
            this.boxBackground.setFillColor(ColorStateList.valueOf(calculateBoxBackgroundColor()));
            applyBoxUnderlineAttributes();
            invalidate();
        }
    }

    private void applyBoxUnderlineAttributes() {
        if (this.boxUnderline != null) {
            if (canDrawStroke()) {
                this.boxUnderline.setFillColor(ColorStateList.valueOf(this.boxStrokeColor));
            }
            invalidate();
        }
    }

    private boolean canDrawOutlineStroke() {
        return this.boxBackgroundMode == 2 && canDrawStroke();
    }

    private boolean canDrawStroke() {
        return this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0;
    }

    public void updateEditTextBackground() {
        Drawable editTextBackground;
        TextView textView;
        EditText editText = this.editText;
        if (editText != null && this.boxBackgroundMode == 0 && (editTextBackground = editText.getBackground()) != null) {
            if (DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
                editTextBackground = editTextBackground.mutate();
            }
            if (this.indicatorViewController.errorShouldBeShown()) {
                editTextBackground.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff.Mode.SRC_IN));
            } else if (!this.counterOverflowed || (textView = this.counterView) == null) {
                DrawableCompat.clearColorFilter(editTextBackground);
                this.editText.refreshDrawableState();
            } else {
                editTextBackground.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.textfield.TextInputLayout.SavedState.1
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        CharSequence error;
        boolean isEndIconChecked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.error = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
            this.isEndIconChecked = source.readInt() != 1 ? false : true;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            TextUtils.writeToParcel(this.error, dest, flags);
            dest.writeInt(this.isEndIconChecked ? 1 : 0);
        }

        public String toString() {
            return "TextInputLayout.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " error=" + ((Object) this.error) + "}";
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        if (this.indicatorViewController.errorShouldBeShown()) {
            ss.error = getError();
        }
        ss.isEndIconChecked = hasEndIcon() && this.endIconView.isChecked();
        return ss;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setError(ss.error);
        if (ss.isEndIconChecked) {
            this.endIconView.performClick();
            this.endIconView.jumpDrawablesToCurrentState();
        }
        requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        this.restoringSavedState = true;
        super.dispatchRestoreInstanceState(container);
        this.restoringSavedState = false;
    }

    @Nullable
    public CharSequence getError() {
        if (this.indicatorViewController.isErrorEnabled()) {
            return this.indicatorViewController.getErrorText();
        }
        return null;
    }

    @Nullable
    public CharSequence getHelperText() {
        if (this.indicatorViewController.isHelperTextEnabled()) {
            return this.indicatorViewController.getHelperText();
        }
        return null;
    }

    public boolean isHintAnimationEnabled() {
        return this.hintAnimationEnabled;
    }

    public void setHintAnimationEnabled(boolean enabled) {
        this.hintAnimationEnabled = enabled;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setEditTextHeightAndDummyDrawable();
    }

    private void setEditTextHeightAndDummyDrawable() {
        if (this.editText != null && hasEndIcon() && this.editText.getMeasuredHeight() < this.endIconView.getMeasuredHeight()) {
            this.editText.setMinimumHeight(this.endIconView.getMeasuredHeight());
            this.editText.post(new Runnable() { // from class: com.google.android.material.textfield.TextInputLayout.6
                @Override // java.lang.Runnable
                public void run() {
                    TextInputLayout.this.editText.requestLayout();
                }
            });
        }
        if (hasEndIcon() && isEndIconVisible()) {
            addEndIconDummyDrawable();
        } else if (this.endIconDummyDrawable != null) {
            removeEndIconDummyDrawable();
        }
    }

    public void setEndIconMode(int endIconMode) {
        int previousEndIconMode = this.endIconMode;
        this.endIconMode = endIconMode;
        setEndIconVisible(endIconMode != 0);
        if (endIconMode == -1) {
            setEndIconOnClickListener(null);
        } else if (endIconMode == 1) {
            setEndIconPasswordToggleDefaults();
        } else if (endIconMode != 2) {
            setEndIconOnClickListener(null);
            setEndIconDrawable((Drawable) null);
            setEndIconContentDescription((CharSequence) null);
        } else {
            setEndIconClearTextDefaults();
        }
        applyEndIconTint();
        dispatchOnEndIconChanged(previousEndIconMode);
    }

    public int getEndIconMode() {
        return this.endIconMode;
    }

    public void setEndIconOnClickListener(@Nullable View.OnClickListener onClickListener) {
        this.endIconView.setOnClickListener(onClickListener);
        boolean z = true;
        this.endIconView.setFocusable(onClickListener != null);
        CheckableImageButton checkableImageButton = this.endIconView;
        if (onClickListener == null) {
            z = false;
        }
        checkableImageButton.setClickable(z);
    }

    public void setEndIconVisible(boolean visible) {
        if (isEndIconVisible() == visible) {
            return;
        }
        if (visible) {
            this.endIconView.setVisibility(0);
            addEndIconDummyDrawable();
            return;
        }
        this.endIconView.setVisibility(4);
        removeEndIconDummyDrawable();
    }

    public boolean isEndIconVisible() {
        return this.endIconView.getVisibility() == 0;
    }

    public void setEndIconDrawable(@DrawableRes int resId) {
        setEndIconDrawable(resId != 0 ? AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    public void setEndIconDrawable(@Nullable Drawable icon) {
        this.endIconView.setImageDrawable(icon);
    }

    @Nullable
    public Drawable getEndIconDrawable() {
        return this.endIconView.getDrawable();
    }

    public void setEndIconContentDescription(@StringRes int resId) {
        setEndIconContentDescription(resId != 0 ? getResources().getText(resId) : null);
    }

    public void setEndIconContentDescription(@Nullable CharSequence description) {
        this.endIconView.setContentDescription(description);
    }

    @Nullable
    public CharSequence getEndIconContentDescription() {
        return this.endIconView.getContentDescription();
    }

    public void setEndIconTintList(@Nullable ColorStateList tintList) {
        this.endIconTintList = tintList;
        this.hasEndIconTintList = true;
        applyEndIconTint();
    }

    public void setEndIconTintMode(@Nullable PorterDuff.Mode mode) {
        this.endIconTintMode = mode;
        this.hasEndIconTintMode = true;
        applyEndIconTint();
    }

    public void addOnEndIconChangedListener(OnEndIconChangedListener listener) {
        this.endIconChangedListeners.add(listener);
    }

    public void removeOnEndIconChangedListener(OnEndIconChangedListener listener) {
        this.endIconChangedListeners.remove(listener);
    }

    public void clearOnEndIconChangedListeners() {
        this.endIconChangedListeners.clear();
    }

    public void addOnEditTextAttachedListener(OnEditTextAttachedListener listener) {
        this.editTextAttachedListeners.add(listener);
        if (this.editText != null) {
            listener.onEditTextAttached();
        }
    }

    public void removeOnEditTextAttachedListener(OnEditTextAttachedListener listener) {
        this.editTextAttachedListeners.remove(listener);
    }

    public void clearOnEditTextAttachedListeners() {
        this.editTextAttachedListeners.clear();
    }

    private void setEndIconPasswordToggleDefaults() {
        setEndIconDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.design_password_eye));
        setEndIconContentDescription(getResources().getText(R.string.password_toggle_content_description));
        setEndIconOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.textfield.TextInputLayout.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (TextInputLayout.this.editText != null) {
                    int selection = TextInputLayout.this.editText.getSelectionEnd();
                    if (TextInputLayout.this.hasPasswordTransformation()) {
                        TextInputLayout.this.editText.setTransformationMethod(null);
                        TextInputLayout.this.endIconView.setChecked(true);
                    } else {
                        TextInputLayout.this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        TextInputLayout.this.endIconView.setChecked(false);
                    }
                    TextInputLayout.this.editText.setSelection(selection);
                }
            }
        });
        addOnEditTextAttachedListener(this.passwordToggleOnEditTextAttachedListener);
        addOnEndIconChangedListener(this.passwordToggleEndIconChangedListener);
    }

    private void setEndIconClearTextDefaults() {
        setEndIconDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.mtrl_clear_text_button));
        setEndIconContentDescription(getResources().getText(R.string.clear_text_end_icon_content_description));
        setEndIconOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.textfield.TextInputLayout.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TextInputLayout.this.editText.setText((CharSequence) null);
            }
        });
        addOnEditTextAttachedListener(this.clearTextOnEditTextAttachedListener);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(@DrawableRes int resId) {
        setPasswordVisibilityToggleDrawable(resId != 0 ? AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(@Nullable Drawable icon) {
        this.endIconView.setImageDrawable(icon);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(@StringRes int resId) {
        setPasswordVisibilityToggleContentDescription(resId != 0 ? getResources().getText(resId) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(@Nullable CharSequence description) {
        this.endIconView.setContentDescription(description);
    }

    @Nullable
    @Deprecated
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.endIconView.getDrawable();
    }

    @Nullable
    @Deprecated
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.endIconView.getContentDescription();
    }

    @Deprecated
    public boolean isPasswordVisibilityToggleEnabled() {
        return this.endIconMode == 1;
    }

    @Deprecated
    public void setPasswordVisibilityToggleEnabled(boolean enabled) {
        if (enabled && this.endIconMode != 1) {
            setEndIconMode(1);
        } else if (!enabled) {
            setEndIconMode(0);
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintList(@Nullable ColorStateList tintList) {
        this.endIconTintList = tintList;
        this.hasEndIconTintList = true;
        applyEndIconTint();
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintMode(@Nullable PorterDuff.Mode mode) {
        this.endIconTintMode = mode;
        this.hasEndIconTintMode = true;
        applyEndIconTint();
    }

    @Deprecated
    public void passwordVisibilityToggleRequested(boolean shouldSkipAnimations) {
        if (this.endIconMode == 1) {
            this.endIconView.performClick();
            if (shouldSkipAnimations) {
                this.endIconView.jumpDrawablesToCurrentState();
            }
        }
    }

    public void setTextInputAccessibilityDelegate(AccessibilityDelegate delegate) {
        EditText editText = this.editText;
        if (editText != null) {
            ViewCompat.setAccessibilityDelegate(editText, delegate);
        }
    }

    private boolean hasEndIcon() {
        return this.endIconMode != 0;
    }

    private void dispatchOnEndIconChanged(int previousIcon) {
        Iterator<OnEndIconChangedListener> it = this.endIconChangedListeners.iterator();
        while (it.hasNext()) {
            OnEndIconChangedListener listener = it.next();
            listener.onEndIconChanged(previousIcon);
        }
    }

    private void dispatchOnEditTextAttached() {
        Iterator<OnEditTextAttachedListener> it = this.editTextAttachedListeners.iterator();
        while (it.hasNext()) {
            OnEditTextAttachedListener listener = it.next();
            listener.onEditTextAttached();
        }
    }

    private void addEndIconDummyDrawable() {
        if (this.editText != null) {
            this.endIconDummyDrawable = new ColorDrawable();
            this.endIconDummyDrawable.setBounds(0, 0, this.endIconView.getMeasuredWidth() - this.endIconView.getPaddingLeft(), 1);
            Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            if (compounds[2] != this.endIconDummyDrawable) {
                this.originalEditTextEndDrawable = compounds[2];
            }
            TextViewCompat.setCompoundDrawablesRelative(this.editText, compounds[0], compounds[1], this.endIconDummyDrawable, compounds[3]);
        }
    }

    private void removeEndIconDummyDrawable() {
        if (this.endIconDummyDrawable != null) {
            Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            if (compounds[2] == this.endIconDummyDrawable) {
                TextViewCompat.setCompoundDrawablesRelative(this.editText, compounds[0], compounds[1], this.originalEditTextEndDrawable, compounds[3]);
            }
            this.endIconDummyDrawable = null;
        }
    }

    private void applyEndIconTint() {
        Drawable endIconDrawable = this.endIconView.getDrawable();
        if (endIconDrawable == null) {
            return;
        }
        if (this.hasEndIconTintList || this.hasEndIconTintMode) {
            Drawable endIconDrawable2 = DrawableCompat.wrap(endIconDrawable).mutate();
            if (this.hasEndIconTintList) {
                DrawableCompat.setTintList(endIconDrawable2, this.endIconTintList);
            }
            if (this.hasEndIconTintMode) {
                DrawableCompat.setTintMode(endIconDrawable2, this.endIconTintMode);
            }
            if (this.endIconView.getDrawable() != endIconDrawable2) {
                this.endIconView.setImageDrawable(endIconDrawable2);
            }
        }
    }

    public boolean hasPasswordTransformation() {
        EditText editText = this.editText;
        return editText != null && (editText.getTransformationMethod() instanceof PasswordTransformationMethod);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        EditText editText = this.editText;
        if (editText != null) {
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(this, editText, rect);
            updateBoxUnderlineBounds(rect);
            if (this.hintEnabled) {
                this.collapsingTextHelper.setCollapsedBounds(calculateCollapsedTextBounds(rect));
                this.collapsingTextHelper.setExpandedBounds(calculateExpandedTextBounds(rect));
                this.collapsingTextHelper.recalculate();
                if (cutoutEnabled() && !this.hintExpanded) {
                    openCutout();
                }
            }
        }
    }

    private void updateBoxUnderlineBounds(Rect bounds) {
        if (this.boxUnderline != null) {
            int top = bounds.bottom - this.boxStrokeWidthFocusedPx;
            this.boxUnderline.setBounds(bounds.left, top, bounds.right, bounds.bottom);
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawHint(canvas);
        drawBoxUnderline(canvas);
    }

    private void drawHint(Canvas canvas) {
        if (this.hintEnabled) {
            this.collapsingTextHelper.draw(canvas);
        }
    }

    private void drawBoxUnderline(Canvas canvas) {
        MaterialShapeDrawable materialShapeDrawable = this.boxUnderline;
        if (materialShapeDrawable != null) {
            Rect underlineBounds = materialShapeDrawable.getBounds();
            underlineBounds.top = underlineBounds.bottom - this.boxStrokeWidthPx;
            this.boxUnderline.draw(canvas);
        }
    }

    private void collapseHint(boolean animate) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (!animate || !this.hintAnimationEnabled) {
            this.collapsingTextHelper.setExpansionFraction(1.0f);
        } else {
            animateToExpansionFraction(1.0f);
        }
        this.hintExpanded = false;
        if (cutoutEnabled()) {
            openCutout();
        }
    }

    private boolean cutoutEnabled() {
        return this.hintEnabled && !TextUtils.isEmpty(this.hint) && (this.boxBackground instanceof CutoutDrawable);
    }

    private void openCutout() {
        if (cutoutEnabled()) {
            RectF cutoutBounds = this.tmpRectF;
            this.collapsingTextHelper.getCollapsedTextActualBounds(cutoutBounds);
            applyCutoutPadding(cutoutBounds);
            cutoutBounds.offset(-getPaddingLeft(), 0.0f);
            ((CutoutDrawable) this.boxBackground).setCutout(cutoutBounds);
        }
    }

    private void closeCutout() {
        if (cutoutEnabled()) {
            ((CutoutDrawable) this.boxBackground).removeCutout();
        }
    }

    private void applyCutoutPadding(RectF cutoutBounds) {
        cutoutBounds.left -= this.boxLabelCutoutPaddingPx;
        cutoutBounds.top -= this.boxLabelCutoutPaddingPx;
        cutoutBounds.right += this.boxLabelCutoutPaddingPx;
        cutoutBounds.bottom += this.boxLabelCutoutPaddingPx;
    }

    @VisibleForTesting
    boolean cutoutIsOpen() {
        return cutoutEnabled() && ((CutoutDrawable) this.boxBackground).hasCutout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (!this.inDrawableStateChanged) {
            boolean z = true;
            this.inDrawableStateChanged = true;
            super.drawableStateChanged();
            int[] state = getDrawableState();
            boolean changed = false;
            CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
            if (collapsingTextHelper != null) {
                changed = false | collapsingTextHelper.setState(state);
            }
            if (!ViewCompat.isLaidOut(this) || !isEnabled()) {
                z = false;
            }
            updateLabelState(z);
            updateEditTextBackground();
            updateTextInputBoxState();
            if (changed) {
                invalidate();
            }
            this.inDrawableStateChanged = false;
        }
    }

    public void updateTextInputBoxState() {
        TextView textView;
        EditText editText;
        EditText editText2;
        if (this.boxBackground != null && this.boxBackgroundMode != 0) {
            boolean isHovered = false;
            boolean hasFocus = isFocused() || ((editText2 = this.editText) != null && editText2.hasFocus());
            if (isHovered() || ((editText = this.editText) != null && editText.isHovered())) {
                isHovered = true;
            }
            if (!isEnabled()) {
                this.boxStrokeColor = this.disabledColor;
            } else if (this.indicatorViewController.errorShouldBeShown()) {
                this.boxStrokeColor = this.indicatorViewController.getErrorViewCurrentTextColor();
            } else if (this.counterOverflowed && (textView = this.counterView) != null) {
                this.boxStrokeColor = textView.getCurrentTextColor();
            } else if (hasFocus) {
                this.boxStrokeColor = this.focusedStrokeColor;
            } else if (isHovered) {
                this.boxStrokeColor = this.hoveredStrokeColor;
            } else {
                this.boxStrokeColor = this.defaultStrokeColor;
            }
            if ((isHovered || hasFocus) && isEnabled()) {
                this.boxStrokeWidthPx = this.boxStrokeWidthFocusedPx;
                adjustCornerSizeForStrokeWidth();
            } else {
                this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
                adjustCornerSizeForStrokeWidth();
            }
            if (this.boxBackgroundMode == 1) {
                if (!isEnabled()) {
                    this.boxBackgroundColor = this.disabledFilledBackgroundColor;
                } else if (isHovered) {
                    this.boxBackgroundColor = this.hoveredFilledBackgroundColor;
                } else {
                    this.boxBackgroundColor = this.defaultFilledBackgroundColor;
                }
            }
            applyBoxAttributes();
        }
    }

    private void expandHint(boolean animate) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (!animate || !this.hintAnimationEnabled) {
            this.collapsingTextHelper.setExpansionFraction(0.0f);
        } else {
            animateToExpansionFraction(0.0f);
        }
        if (cutoutEnabled() && ((CutoutDrawable) this.boxBackground).hasCutout()) {
            closeCutout();
        }
        this.hintExpanded = true;
    }

    @VisibleForTesting
    void animateToExpansionFraction(float target) {
        if (this.collapsingTextHelper.getExpansionFraction() != target) {
            if (this.animator == null) {
                this.animator = new ValueAnimator();
                this.animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                this.animator.setDuration(167L);
                this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.TextInputLayout.9
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextInputLayout.this.collapsingTextHelper.setExpansionFraction(((Float) animator.getAnimatedValue()).floatValue());
                    }
                });
            }
            this.animator.setFloatValues(this.collapsingTextHelper.getExpansionFraction(), target);
            this.animator.start();
        }
    }

    @VisibleForTesting
    final boolean isHintExpanded() {
        return this.hintExpanded;
    }

    @VisibleForTesting
    final boolean isHelperTextDisplayed() {
        return this.indicatorViewController.helperTextIsDisplayed();
    }

    @VisibleForTesting
    final int getHintCurrentCollapsedTextColor() {
        return this.collapsingTextHelper.getCurrentCollapsedTextColor();
    }

    @VisibleForTesting
    final float getHintCollapsedTextHeight() {
        return this.collapsingTextHelper.getCollapsedTextHeight();
    }

    @VisibleForTesting
    final int getErrorTextCurrentColor() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    /* loaded from: classes2.dex */
    public static class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final TextInputLayout layout;

        public AccessibilityDelegate(TextInputLayout layout) {
            this.layout = layout;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            EditText editText = this.layout.getEditText();
            CharSequence text = editText != null ? editText.getText() : null;
            CharSequence hintText = this.layout.getHint();
            CharSequence errorText = this.layout.getError();
            CharSequence counterDesc = this.layout.getCounterOverflowDescription();
            boolean showingText = !TextUtils.isEmpty(text);
            boolean hasHint = !TextUtils.isEmpty(hintText);
            boolean showingError = !TextUtils.isEmpty(errorText);
            boolean z = false;
            boolean contentInvalid = showingError || !TextUtils.isEmpty(counterDesc);
            if (showingText) {
                info.setText(text);
            } else if (hasHint) {
                info.setText(hintText);
            }
            if (hasHint) {
                info.setHintText(hintText);
                if (!showingText && hasHint) {
                    z = true;
                }
                info.setShowingHintText(z);
            }
            if (contentInvalid) {
                info.setError(showingError ? errorText : counterDesc);
                info.setContentInvalid(true);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            EditText editText = this.layout.getEditText();
            CharSequence text = editText != null ? editText.getText() : null;
            CharSequence eventText = TextUtils.isEmpty(text) ? this.layout.getHint() : text;
            if (!TextUtils.isEmpty(eventText)) {
                event.getText().add(eventText);
            }
        }
    }
}
