package com.google.android.play.core.appupdate;

import android.content.Context;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class y {
    private cn<Context> a;
    private cn<r> b;
    private cn<p> c;
    private cn<a> d;
    private cn<e> e;
    private cn<AppUpdateManager> f;

    public /* synthetic */ y(g gVar) {
        this.a = new i(gVar);
        this.b = cl.a(new s(this.a));
        this.c = cl.a(new q(this.a, this.b));
        this.d = cl.a(new b(this.a));
        this.e = cl.a(new f(this.c, this.d, this.a));
        this.f = cl.a(new h(this.e));
    }

    public final AppUpdateManager a() {
        return this.f.a();
    }
}
