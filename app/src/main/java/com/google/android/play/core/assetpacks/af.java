package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class af extends ag {
    final /* synthetic */ int a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ int d;
    final /* synthetic */ i e;
    final /* synthetic */ ar f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public af(ar arVar, i iVar, int i, String str, String str2, int i2, i iVar2) {
        super(iVar);
        this.f = arVar;
        this.a = i;
        this.b = str;
        this.c = str2;
        this.d = i2;
        this.e = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        com.google.android.play.core.internal.af afVar;
        ap apVar;
        String str;
        Bundle e;
        try {
            apVar = this.f.e;
            str = this.f.c;
            Bundle c = ar.c(this.a, this.b, this.c, this.d);
            e = ar.e();
            ((s) apVar.b()).a(str, c, e, new ak(this.f, this.e, (char[]) null));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.a(e2, "notifyChunkTransferred", new Object[0]);
        }
    }
}
