package com.google.android.play.core.assetpacks;

import android.content.Context;
import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import java.io.File;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class da implements cn<cz> {
    private final cn<String> a;
    private final cn<aw> b;
    private final cn<bz> c;
    private final cn<Context> d;
    private final cn<dl> e;
    private final cn<Executor> f;

    public da(cn<String> cnVar, cn<aw> cnVar2, cn<bz> cnVar3, cn<Context> cnVar4, cn<dl> cnVar5, cn<Executor> cnVar6) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
        this.d = cnVar4;
        this.e = cnVar5;
        this.f = cnVar6;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ cz a() {
        String a = this.a.a();
        aw a2 = this.b.a();
        bz a3 = this.c.a();
        Context b = ((s) this.d).a();
        dl a4 = this.e.a();
        return new cz(a != null ? new File(b.getExternalFilesDir(null), a) : b.getExternalFilesDir(null), a2, a3, b, a4, cl.b(this.f));
    }
}
