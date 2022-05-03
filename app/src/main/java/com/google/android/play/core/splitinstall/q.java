package com.google.android.play.core.splitinstall;

import android.content.Context;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class q implements cn<p> {
    private final cn<Context> a;

    public q(cn<Context> cnVar) {
        this.a = cnVar;
    }

    public static q a(cn<Context> cnVar) {
        return new q(cnVar);
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ p a() {
        return new p(this.a.a());
    }
}
