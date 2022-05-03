package com.google.android.play.core.appupdate;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class k extends ag {
    final /* synthetic */ String a;
    final /* synthetic */ i b;
    final /* synthetic */ p c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k(p pVar, i iVar, String str, i iVar2) {
        super(iVar);
        this.c = pVar;
        this.a = str;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        try {
            str = this.c.d;
            this.c.a.b().a(str, p.a(this.c, this.a), new o(this.c, this.b, this.a));
        } catch (RemoteException e) {
            afVar = p.b;
            afVar.a(e, "requestUpdateInfo(%s)", this.a);
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
