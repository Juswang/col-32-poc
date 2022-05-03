package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class bx implements cn<bw> {
    private final cn<cp> a;
    private final cn<w> b;
    private final cn<bt> c;
    private final cn<dv> d;
    private final cn<df> e;
    private final cn<dj> f;
    private final cn<Cdo> g;
    private final cn<cs> h;

    public bx(cn<cp> cnVar, cn<w> cnVar2, cn<bt> cnVar3, cn<dv> cnVar4, cn<df> cnVar5, cn<dj> cnVar6, cn<Cdo> cnVar7, cn<cs> cnVar8) {
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
    public final /* bridge */ /* synthetic */ bw a() {
        return new bw(this.a.a(), cl.b(this.b), this.c.a(), this.d.a(), this.e.a(), this.f.a(), this.g.a(), this.h.a());
    }
}
