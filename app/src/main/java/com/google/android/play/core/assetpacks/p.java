package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.bq;
import com.google.android.play.core.internal.cn;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public final class p implements cn<Executor> {
    public static Executor b() {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor(l.a);
        bq.b(newSingleThreadExecutor);
        return newSingleThreadExecutor;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ Executor a() {
        return b();
    }
}
