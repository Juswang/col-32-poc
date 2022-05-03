package com.google.android.play.core.appupdate;

import android.content.Context;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class q implements cn<p> {
    private final cn<Context> a;
    private final cn<r> b;

    public q(cn<Context> cnVar, cn<r> cnVar2) {
        this.a = cnVar;
        this.b = cnVar2;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ p a() {
        return new p(((i) this.a).a(), this.b.a());
    }
}
