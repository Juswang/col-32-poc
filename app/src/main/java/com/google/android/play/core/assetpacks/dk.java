package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class dk implements cn<dj> {
    private final cn<bb> a;
    private final cn<w> b;
    private final cn<cp> c;
    private final cn<Executor> d;
    private final cn<bz> e;

    public dk(cn<bb> cnVar, cn<w> cnVar2, cn<cp> cnVar3, cn<Executor> cnVar4, cn<bz> cnVar5) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
        this.e = cnVar5;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ dj a() {
        bb a = this.a.a();
        return new dj(a, cl.b(this.b), this.c.a(), cl.b(this.d), this.e.a());
    }
}
