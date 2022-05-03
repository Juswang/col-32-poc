package com.google.android.play.core.assetpacks;

import com.google.android.play.core.common.a;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class dp implements cn<Cdo> {
    private final cn<bb> a;
    private final cn<w> b;
    private final cn<a> c;

    public dp(cn<bb> cnVar, cn<w> cnVar2, cn<a> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ Cdo a() {
        return new Cdo(this.a.a(), cl.b(this.b), this.c.a());
    }
}
