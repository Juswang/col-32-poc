package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/* loaded from: classes2.dex */
final class i extends ResultReceiver {
    final /* synthetic */ com.google.android.play.core.tasks.i a;
    final /* synthetic */ j b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(j jVar, Handler handler, com.google.android.play.core.tasks.i iVar) {
        super(handler);
        this.b = jVar;
        this.a = iVar;
    }

    @Override // android.os.ResultReceiver
    public final void onReceiveResult(int i, Bundle bundle) {
        bn bnVar;
        if (i == 1) {
            this.a.b((com.google.android.play.core.tasks.i) (-1));
            bnVar = this.b.h;
            bnVar.a(null);
        } else if (i != 2) {
            this.a.b((Exception) new AssetPackException(-100));
        } else {
            this.a.b((com.google.android.play.core.tasks.i) 0);
        }
    }
}
