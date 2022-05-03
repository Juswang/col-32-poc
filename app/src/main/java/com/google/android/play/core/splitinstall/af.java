package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public final class af extends ag {
    final /* synthetic */ List a;
    final /* synthetic */ i b;
    final /* synthetic */ av c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public af(av avVar, i iVar, List list, i iVar2) {
        super(iVar);
        this.c = avVar;
        this.a = list;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        com.google.android.play.core.internal.af afVar;
        String str;
        try {
            str = this.c.d;
            this.c.a.b().b(str, av.a((Collection) this.a), av.b(), new aq(this.c, this.b));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "deferredUninstall(%s)", this.a);
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
