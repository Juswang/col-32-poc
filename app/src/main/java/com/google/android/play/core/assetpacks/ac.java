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

/* loaded from: classes2.dex */
final class ac extends ag {
    final /* synthetic */ List a;
    final /* synthetic */ i b;
    final /* synthetic */ ar c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ac(ar arVar, i iVar, List list, i iVar2) {
        super(iVar);
        this.c = arVar;
        this.a = list;
        this.b = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        ap apVar;
        String str;
        Bundle e;
        ArrayList a = ar.a((Collection) this.a);
        try {
            apVar = this.c.e;
            str = this.c.c;
            e = ar.e();
            ((s) apVar.b()).b(str, a, e, new ak(this.c, this.b, (byte[]) null));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "cancelDownloads(%s)", this.a);
        }
    }
}
