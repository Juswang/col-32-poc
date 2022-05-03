package com.google.android.play.core.appupdate;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class l extends ag {
    final /* synthetic */ i a;
    final /* synthetic */ String b;
    final /* synthetic */ p c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public l(p pVar, i iVar, i iVar2, String str) {
        super(iVar);
        this.c = pVar;
        this.a = iVar2;
        this.b = str;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        Bundle d;
        try {
            str = this.c.d;
            d = p.d();
            this.c.a.b().b(str, d, new n(this.c, this.a));
        } catch (RemoteException e) {
            afVar = p.b;
            afVar.a(e, "completeUpdate(%s)", this.b);
            this.a.b((Exception) new RuntimeException(e));
        }
    }
}
