package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class ct implements cn<cs> {
    private final cn<cp> a;
    private final cn<bb> b;
    private final cn<bk> c;

    public ct(cn<cp> cnVar, cn<bb> cnVar2, cn<bk> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ cs a() {
        return new cs(this.a.a(), this.b.a(), this.c.a());
    }
}
