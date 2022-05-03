package com.google.android.play.core.assetpacks;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;
import java.util.Map;

/* loaded from: classes2.dex */
final class ad extends ag {
    final /* synthetic */ Map a;
    final /* synthetic */ i b;
    final /* synthetic */ ar c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ad(ar arVar, i iVar, Map map, i iVar2) {
        super(iVar);
        this.c = arVar;
        this.a = map;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        try {
            apVar = this.c.e;
            str = this.c.c;
            ((s) apVar.b()).a(str, ar.b(this.a), new am(this.c, this.b));
        } catch (RemoteException e) {
            afVar = ar.a;
            afVar.a(e, "syncPacks", new Object[0]);
            this.b.b((Exception) new RuntimeException(e));
        }
    }
}
