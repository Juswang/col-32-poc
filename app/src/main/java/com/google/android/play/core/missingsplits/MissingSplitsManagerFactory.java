package com.google.android.play.core.missingsplits;

import android.content.Context;
import androidx.annotation.NonNull;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public class MissingSplitsManagerFactory {
    private static final AtomicReference<Boolean> a = new AtomicReference<>(null);

    @NonNull
    public static MissingSplitsManager create(@NonNull Context context) {
        return new b(context, Runtime.getRuntime(), new a(context, context.getPackageManager()), a);
    }
}
