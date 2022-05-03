package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class ao extends ak<Void> {
    final int c;
    final String d;
    final int e;
    final /* synthetic */ ar f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ao(ar arVar, i<Void> iVar, int i, String str, int i2) {
        super(arVar, iVar);
        this.f = arVar;
        this.c = i;
        this.d = str;
        this.e = i2;
    }

    @Override // com.google.android.play.core.assetpacks.ak, com.google.android.play.core.internal.u
    public final void a(Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.f.e;
        apVar.a();
        int i = bundle.getInt("error_code");
        afVar = ar.a;
        afVar.b("onError(%d), retrying notifyModuleCompleted...", Integer.valueOf(i));
        int i2 = this.e;
        if (i2 > 0) {
            this.f.a(this.c, this.d, i2 - 1);
        }
    }
}
