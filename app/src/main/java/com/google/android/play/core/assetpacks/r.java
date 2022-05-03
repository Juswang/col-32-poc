package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.internal.bq;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class r implements cn<w> {
    private final cn<Context> a;
    private final cn<ar> b;
    private final cn<cz> c;

    public r(cn<Context> cnVar, cn<ar> cnVar2, cn<cz> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ w a() {
        w wVar = (w) (n.a(((s) this.a).a()) == null ? cl.b(this.b).a() : cl.b(this.c).a());
        bq.b(wVar);
        return wVar;
    }
}
