package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public final class ai extends ag {
    final /* synthetic */ List a;
    final /* synthetic */ i b;
    final /* synthetic */ av c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ai(av avVar, i iVar, List list, i iVar2) {
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
            this.c.a.b().e(str, av.b((Collection) this.a), av.b(), new ap(this.c, this.b));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "deferredLanguageUninstall(%s)", this.a);
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
