package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class as implements cn<ar> {
    private final cn<Context> a;
    private final cn<bz> b;

    public as(cn<Context> cnVar, cn<bz> cnVar2) {
        this.a = cnVar;
        this.b = cnVar2;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ ar a() {
        return new ar(((s) this.a).a(), this.b.a());
    }
}
