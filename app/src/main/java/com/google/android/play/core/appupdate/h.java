package com.google.android.play.core.appupdate;

import com.google.android.play.core.internal.bq;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class h implements cn<AppUpdateManager> {
    private final cn<e> a;

    public h(cn<e> cnVar) {
        this.a = cnVar;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ AppUpdateManager a() {
        e a = this.a.a();
        bq.b(a);
        return a;
    }
}
