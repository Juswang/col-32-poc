package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class bc implements cn<bb> {
    private final cn<Context> a;
    private final cn<dl> b;

    public bc(cn<Context> cnVar, cn<dl> cnVar2) {
        this.a = cnVar;
        this.b = cnVar2;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ bb a() {
        return new bb(((s) this.a).a(), this.b.a());
    }
}
