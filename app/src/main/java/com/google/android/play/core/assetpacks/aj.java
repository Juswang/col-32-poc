package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class aj extends ag {
    final /* synthetic */ i a;
    final /* synthetic */ ar b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aj(ar arVar, i iVar, i iVar2) {
        super(iVar);
        this.b = arVar;
        this.a = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        Bundle e;
        try {
            apVar = this.b.f;
            str = this.b.c;
            e = ar.e();
            ((s) apVar.b()).b(str, e, new an(this.b, this.a));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "keepAlive", new Object[0]);
        }
    }
}
