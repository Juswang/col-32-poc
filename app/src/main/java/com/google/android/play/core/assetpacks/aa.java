package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class aa extends ag {
    final /* synthetic */ String a;
    final /* synthetic */ i b;
    final /* synthetic */ ar c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aa(ar arVar, i iVar, String str, i iVar2) {
        super(iVar);
        this.c = arVar;
        this.a = str;
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
            c = ar.c(0, this.a);
            e = ar.e();
            ((s) apVar.b()).e(str, c, e, new ak(this.c, this.b, (short[]) null));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "removePack(%s)", this.a);
        }
    }
}
