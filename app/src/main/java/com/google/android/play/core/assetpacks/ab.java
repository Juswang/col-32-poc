package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
final class ab extends ag {
    final /* synthetic */ List a;
    final /* synthetic */ Map b;
    final /* synthetic */ i c;
    final /* synthetic */ List d;
    final /* synthetic */ ar e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ab(ar arVar, i iVar, List list, Map map, i iVar2, List list2) {
        super(iVar);
        this.e = arVar;
        this.a = list;
        this.b = map;
        this.c = iVar2;
        this.d = list2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        bz bzVar;
        ArrayList a = ar.a((Collection) this.a);
        try {
            apVar = this.e.e;
            str = this.e.c;
            Bundle b = ar.b(this.b);
            ar arVar = this.e;
            i iVar = this.c;
            bzVar = arVar.d;
            ((s) apVar.b()).a(str, a, b, new aq(arVar, iVar, bzVar, this.d));
        } catch (RemoteException e) {
            afVar = ar.a;
            afVar.a(e, "startDownload(%s)", this.a);
            this.c.b((Exception) new RuntimeException(e));
        }
    }
}
