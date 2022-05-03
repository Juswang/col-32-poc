package com.google.android.play.core.review;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.play.core.common.PlayCoreDialogWrapperActivity;
import com.google.android.play.core.splitcompat.p;

/* loaded from: classes2.dex */
public class ReviewManagerFactory {
    private ReviewManagerFactory() {
    }

    @NonNull
    public static ReviewManager create(@NonNull Context context) {
        PlayCoreDialogWrapperActivity.a(context);
        return new c(new h(p.a(context)));
    }
}
