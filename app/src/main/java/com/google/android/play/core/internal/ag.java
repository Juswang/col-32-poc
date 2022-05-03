package com.google.android.play.core.internal;

import androidx.annotation.Nullable;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public abstract class ag implements Runnable {
    @Nullable
    private final i<?> a;

    public ag() {
        this.a = null;
    }

    public ag(@Nullable i<?> iVar) {
        this.a = iVar;
    }

    protected abstract void a();

    @Nullable
    public final i<?> b() {
        return this.a;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            a();
        } catch (Exception e) {
            i<?> iVar = this.a;
            if (iVar != null) {
                iVar.b(e);
            }
        }
    }
}
