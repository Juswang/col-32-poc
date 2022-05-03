package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class bu implements cn<bt> {
    private final cn<bb> a;
    private final cn<w> b;
    private final cn<aw> c;
    private final cn<bz> d;

    public bu(cn<bb> cnVar, cn<w> cnVar2, cn<aw> cnVar3, cn<bz> cnVar4) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ bt a() {
        return new bt(this.a.a(), cl.b(this.b), cl.b(this.c), this.d.a());
    }
}
