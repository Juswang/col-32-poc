package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class cq implements cn<cp> {
    private final cn<bb> a;
    private final cn<w> b;
    private final cn<bz> c;
    private final cn<Executor> d;

    public cq(cn<bb> cnVar, cn<w> cnVar2, cn<bz> cnVar3, cn<Executor> cnVar4) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ cp a() {
        bb a = this.a.a();
        return new cp(a, cl.b(this.b), this.c.a(), cl.b(this.d));
    }
}
