package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.s;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class ai extends ag {
    final /* synthetic */ int a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ int d;
    final /* synthetic */ i e;
    final /* synthetic */ ar f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ai(ar arVar, i iVar, int i, String str, String str2, int i2, i iVar2) {
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
        af afVar;
        ap apVar;
        String str;
        Bundle e;
        try {
            apVar = this.f.e;
            str = this.f.c;
            Bundle c = ar.c(this.a, this.b, this.c, this.d);
            e = ar.e();
            ((s) apVar.b()).d(str, c, e, new al(this.f, this.e));
        } catch (RemoteException e2) {
            afVar = ar.a;
            afVar.b("getChunkFileDescriptor(%s, %s, %d, session=%d)", this.b, this.c, Integer.valueOf(this.d), Integer.valueOf(this.a));
            this.e.b((Exception) new RuntimeException(e2));
        }
    }
}
