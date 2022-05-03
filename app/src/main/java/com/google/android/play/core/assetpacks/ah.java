package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class ah extends ag {
    final /* synthetic */ int a;
    final /* synthetic */ i b;
    final /* synthetic */ ar c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ah(ar arVar, i iVar, int i, i iVar2) {
        super(iVar);
        this.c = arVar;
        this.a = i;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        Bundle c;
        Bundle e;
        try {
            apVar = this.c.e;
            str = this.c.c;
            c = ar.c(this.a);
            e = ar.e();
            ((s) apVar.b()).c(str, c, e, new ak(this.c, this.b, (int[]) null));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "notifySessionFailed", new Object[0]);
        }
    }
}
