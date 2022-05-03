package com.google.android.play.core.splitinstall;

import androidx.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public final class o {
    private static final AtomicReference<n> a = new AtomicReference<>(null);

    @Nullable
    public static n a() {
        return a.get();
    }

    public static void a(n nVar) {
        a.compareAndSet(null, nVar);
    }
}
