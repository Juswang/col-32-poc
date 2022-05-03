package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.common.a;
import com.google.android.play.core.common.c;
import com.google.android.play.core.internal.ck;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import com.google.android.play.core.splitinstall.p;
import com.google.android.play.core.splitinstall.q;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class br implements a {
    private final n a;
    private cn<Context> b;
    private cn<dl> c;
    private cn<bb> d;
    private cn<bz> e;
    private cn<ar> f;
    private cn<String> g;
    private cn<Executor> i;
    private cn<cp> j;
    private cn<bt> l;
    private cn<dv> m;
    private cn<df> n;
    private cn<dj> o;
    private cn<Cdo> q;
    private cn<cs> s;
    private cn<bw> t;
    private cn<bn> u;
    private cn<Executor> v;
    private cn<cz> w;
    private cn<p> x;
    private cn<j> y;
    private cn<AssetPackManager> z;
    private cn<w> h = new ck();
    private cn<aw> k = new ck();
    private cn<a> p = cl.a(c.b());
    private cn<bk> r = cl.a(new bl(this.h));

    public /* synthetic */ br(n nVar) {
        cb cbVar;
        p pVar;
        bp bpVar;
        v vVar;
        this.a = nVar;
        this.b = new s(nVar);
        this.c = cl.a(new dm(this.b));
        this.d = cl.a(new bc(this.b, this.c));
        cbVar = ca.a;
        this.e = cl.a(cbVar);
        this.f = cl.a(new as(this.b, this.e));
        this.g = cl.a(new t(this.b));
        pVar = o.a;
        this.i = cl.a(pVar);
        this.j = cl.a(new cq(this.d, this.h, this.e, this.i));
        this.l = cl.a(new bu(this.d, this.h, this.k, this.e));
        this.m = cl.a(new dw(this.d));
        this.n = cl.a(new dg(this.d));
        this.o = cl.a(new dk(this.d, this.h, this.j, this.i, this.e));
        this.q = cl.a(new dp(this.d, this.h, this.p));
        this.s = cl.a(new ct(this.j, this.d, this.r));
        this.t = cl.a(new bx(this.j, this.h, this.l, this.m, this.n, this.o, this.q, this.s));
        bpVar = bo.a;
        this.u = cl.a(bpVar);
        vVar = u.a;
        this.v = cl.a(vVar);
        ck.a(this.k, cl.a(new ax(this.b, this.j, this.t, this.h, this.e, this.u, this.p, this.i, this.v)));
        this.w = cl.a(new da(this.g, this.k, this.e, this.b, this.c, this.i));
        ck.a(this.h, cl.a(new r(this.b, this.f, this.w)));
        this.x = cl.a(q.a(this.b));
        this.y = cl.a(new k(this.d, this.h, this.k, this.x, this.j, this.e, this.u, this.i));
        this.z = cl.a(new q(this.y, this.b));
    }

    @Override // com.google.android.play.core.assetpacks.a
    public final AssetPackManager a() {
        return this.z.a();
    }

    @Override // com.google.android.play.core.assetpacks.a
    public final void a(AssetPackExtractionService assetPackExtractionService) {
        assetPackExtractionService.a = s.a(this.a);
        assetPackExtractionService.b = this.y.a();
        assetPackExtractionService.c = this.d.a();
    }
}
