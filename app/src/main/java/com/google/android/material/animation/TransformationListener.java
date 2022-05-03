package com.google.android.material.animation;

import android.view.View;

/* loaded from: classes2.dex */
public interface TransformationListener<T extends View> {
    void onScaleChanged(T t);

    void onTranslationChanged(T t);
}
