package com.google.android.play.core.tasks;

import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
final class k implements Executor {
    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
