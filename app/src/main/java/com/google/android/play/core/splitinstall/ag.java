package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.tasks.i;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public final class ag extends com.google.android.play.core.internal.ag {
    final /* synthetic */ List a;
    final /* synthetic */ i b;
    final /* synthetic */ av c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ag(av avVar, i iVar, List list, i iVar2) {
        super(iVar);
        this.c = avVar;
        this.a = list;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        try {
            str = this.c.d;
            this.c.a.b().c(str, av.a((Collection) this.a), av.b(), new an(this.c, this.b));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "deferredInstall(%s)", this.a);
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
