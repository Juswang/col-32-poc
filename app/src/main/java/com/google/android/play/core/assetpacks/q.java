package com.google.android.play.core.assetpacks;

import android.content.ComponentName;
import android.content.Context;
import com.google.android.play.core.common.PlayCoreDialogWrapperActivity;
import com.google.android.play.core.internal.bq;
import com.google.android.play.core.internal.cn;

/* loaded from: classes2.dex */
public final class q implements cn<AssetPackManager> {
    private final cn<j> a;
    private final cn<Context> b;

    public q(cn<j> cnVar, cn<Context> cnVar2) {
        this.a = cnVar;
        this.b = cnVar2;
    }

    @Override // com.google.android.play.core.internal.cn
    public final /* bridge */ /* synthetic */ AssetPackManager a() {
        j a = this.a.a();
        Context b = ((s) this.b).a();
        j jVar = a;
        bq.a(b.getPackageManager(), new ComponentName(b.getPackageName(), "com.google.android.play.core.assetpacks.AssetPackExtractionService"), 4);
        PlayCoreDialogWrapperActivity.a(b);
        bq.b(jVar);
        return jVar;
    }
}
