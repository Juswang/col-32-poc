package com.google.android.play.core.splitinstall;

import android.content.Context;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager;
import com.google.android.play.core.splitinstall.testing.k;
import java.io.File;

/* loaded from: classes2.dex */
public final class c implements m {
    private cn<Context> a;
    private cn<av> b;
    private cn<t> c;
    private cn<p> d;
    private cn<ax> e;
    private cn<w> f;
    private cn<File> g;
    private cn<FakeSplitInstallManager> h;
    private cn<i> i;
    private cn<SplitInstallManager> j;

    public /* synthetic */ c(y yVar) {
        this.a = new z(yVar);
        this.b = cl.a(new aw(this.a));
        this.c = cl.a(new ab(yVar));
        this.d = cl.a(q.a(this.a));
        this.e = cl.a(new ay(this.a));
        this.f = cl.a(new x(this.b, this.c, this.d, this.e));
        this.g = cl.a(new aa(this.a));
        this.h = cl.a(new k(this.a, this.g, this.d));
        this.i = cl.a(new j(this.f, this.h, this.g));
        this.j = cl.a(new ac(yVar, this.i));
    }

    @Override // com.google.android.play.core.splitinstall.m
    public final SplitInstallManager a() {
        return this.j.a();
    }

    @Override // com.google.android.play.core.splitinstall.m
    public final File b() {
        return this.g.a();
    }
}
