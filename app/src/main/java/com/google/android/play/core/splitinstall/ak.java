package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class ak extends ag {
    final /* synthetic */ i a;
    final /* synthetic */ av b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ak(av avVar, i iVar, i iVar2) {
        super(iVar);
        this.b = avVar;
        this.a = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        try {
            str = this.b.d;
            this.b.a.b().a(str, new as(this.b, this.a));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "getSessionStates", new Object[0]);
            this.a.b((Exception) new RuntimeException(e));
        }
    }
}
