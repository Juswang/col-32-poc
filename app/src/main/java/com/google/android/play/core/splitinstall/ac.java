package com.google.android.play.core.splitinstall;

import com.google.android.play.core.internal.bq;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class ac implements cn<SplitInstallManager> {
    private final y a;
    private final cn<i> b;

    public ac(y yVar, cn<i> cnVar) {
        this.a = yVar;
        this.b = cnVar;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ SplitInstallManager a() {
        i a = this.b.a();
        bq.b(a);
        return a;
    }
}
