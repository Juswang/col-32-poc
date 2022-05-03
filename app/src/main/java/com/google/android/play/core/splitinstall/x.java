package com.google.android.play.core.splitinstall;

import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class x implements cn<w> {
    private final cn<av> a;
    private final cn<t> b;
    private final cn<p> c;
    private final cn<ax> d;

    public x(cn<av> cnVar, cn<t> cnVar2, cn<p> cnVar3, cn<ax> cnVar4) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ w a() {
        return new w(this.a.a(), this.b.a(), this.c.a(), this.d.a());
    }
}
