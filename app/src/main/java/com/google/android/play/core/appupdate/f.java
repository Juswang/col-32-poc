package com.google.android.play.core.appupdate;

import android.content.Context;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class f implements cn<e> {
    private final cn<p> a;
    private final cn<a> b;
    private final cn<Context> c;

    public f(cn<p> cnVar, cn<a> cnVar2, cn<Context> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ e a() {
        return new e(this.a.a(), this.b.a(), ((i) this.c).a());
    }
}
