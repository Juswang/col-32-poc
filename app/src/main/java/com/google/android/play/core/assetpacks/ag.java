package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class ag extends com.google.android.play.core.internal.ag {
    final /* synthetic */ int a;
    final /* synthetic */ String b;
    final /* synthetic */ i c;
    final /* synthetic */ int d;
    final /* synthetic */ ar e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ag(ar arVar, i iVar, int i, String str, i iVar2, int i2) {
        super(iVar);
        this.e = arVar;
        this.a = i;
        this.b = str;
        this.c = iVar2;
        this.d = i2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        Bundle c;
        Bundle e;
        try {
            apVar = this.e.e;
            str = this.e.c;
            c = ar.c(this.a, this.b);
            e = ar.e();
            ((s) apVar.b()).b(str, c, e, new ao(this.e, this.c, this.a, this.b, this.d));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "notifyModuleCompleted", new Object[0]);
        }
    }
}
