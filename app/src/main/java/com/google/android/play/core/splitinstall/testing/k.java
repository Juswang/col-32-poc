package com.google.android.play.core.splitinstall.testing;

import android.content.Context;
import com.google.android.play.core.internal.cn;
import com.google.android.play.core.splitinstall.p;
import com.google.android.play.core.splitinstall.z;
import java.io.File;

/* loaded from: classes2.dex */
public final class k implements cn<FakeSplitInstallManager> {
    private final cn<Context> a;
    private final cn<File> b;
    private final cn<p> c;

    public k(cn<Context> cnVar, cn<File> cnVar2, cn<p> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ FakeSplitInstallManager a() {
        return new FakeSplitInstallManager(((z) this.a).a(), this.b.a(), this.c.a());
    }
}
