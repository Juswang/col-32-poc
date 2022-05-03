package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.common.a;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class ax implements cn<aw> {
    private final cn<Context> a;
    private final cn<cp> b;
    private final cn<bw> c;
    private final cn<w> d;
    private final cn<bz> e;
    private final cn<bn> f;
    private final cn<a> g;
    private final cn<Executor> h;
    private final cn<Executor> i;

    public ax(cn<Context> cnVar, cn<cp> cnVar2, cn<bw> cnVar3, cn<w> cnVar4, cn<bz> cnVar5, cn<bn> cnVar6, cn<a> cnVar7, cn<Executor> cnVar8, cn<Executor> cnVar9) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
        this.e = cnVar5;
        this.f = cnVar6;
        this.g = cnVar7;
        this.h = cnVar8;
        this.i = cnVar9;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ aw a() {
        return new aw(((s) this.a).a(), this.b.a(), this.c.a(), cl.b(this.d), this.e.a(), this.f.a(), this.g.a(), cl.b(this.h), cl.b(this.i));
    }
}
