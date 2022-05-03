package com.google.android.play.core.splitinstall;

import com.google.android.play.core.internal.cl;
import com.google.android.play.core.internal.cn;
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager;
import java.io.File;

/* loaded from: classes2.dex */
public final class j implements cn<i> {
    private final cn<w> a;
    private final cn<FakeSplitInstallManager> b;
    private final cn<File> c;

    public j(cn<w> cnVar, cn<FakeSplitInstallManager> cnVar2, cn<File> cnVar3) {
        this.a = cnVar;
        this.b = cnVar2;
        this.c = cnVar3;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ i a() {
        return new i(cl.b(this.a), cl.b(this.b), cl.b(this.c));
    }
}
