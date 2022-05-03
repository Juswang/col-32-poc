package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import com.google.android.play.core.splitinstall.p;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class k implements cn<j> {
    private final cn<bb> a;
    private final cn<w> b;
    private final cn<aw> c;
    private final cn<p> d;
    private final cn<cp> e;
    private final cn<bz> f;
    private final cn<bn> g;
    private final cn<Executor> h;

    public k(cn<bb> cnVar, cn<w> cnVar2, cn<aw> cnVar3, cn<p> cnVar4, cn<cp> cnVar5, cn<bz> cnVar6, cn<bn> cnVar7, cn<Executor> cnVar8) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
        this.e = cnVar5;
        this.f = cnVar6;
        this.g = cnVar7;
        this.h = cnVar8;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ j a() {
        return new j(this.a.a(), cl.b(this.b), this.c.a(), this.d.a(), this.e.a(), this.f.a(), this.g.a(), cl.b(this.h));
    }
}
