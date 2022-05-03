package com.google.android.play.core.splitinstall;

import androidx.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public final class l extends Enum<l> implements e {
    public static final l a = new l();
    private static final AtomicReference<f> b = new AtomicReference<>(null);

    static {
        new l[1][0] = a;
    }

    private l() {
        super("INSTANCE", 0);
    }

    @Override // com.google.android.play.core.splitinstall.e
    @Nullable
    public final f a() {
        return b.get();
    }

    public final void a(f fVar) {
        b.set(fVar);
    }
}
