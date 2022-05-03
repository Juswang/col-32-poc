package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class al extends ag {
    final /* synthetic */ int a;
    final /* synthetic */ i b;
    final /* synthetic */ av c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public al(av avVar, i iVar, int i, i iVar2) {
        super(iVar);
        this.c = avVar;
        this.a = i;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        try {
            str = this.c.d;
            this.c.a.b().a(str, this.a, av.b(), new am(this.c, this.b));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "cancelInstall(%d)", Integer.valueOf(this.a));
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
