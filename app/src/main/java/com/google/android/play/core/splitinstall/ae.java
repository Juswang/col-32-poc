package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes2.dex */
public final class ae extends ag {
    final /* synthetic */ Collection a;
    final /* synthetic */ Collection b;
    final /* synthetic */ i c;
    final /* synthetic */ av d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ae(av avVar, i iVar, Collection collection, Collection collection2, i iVar2) {
        super(iVar);
        this.d = avVar;
        this.a = collection;
        this.b = collection2;
        this.c = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        ArrayList a = av.a(this.a);
        a.addAll(av.b(this.b));
        try {
            str = this.d.d;
            this.d.a.b().a(str, a, av.b(), new at(this.d, this.c));
        } catch (RemoteException e) {
            afVar = av.b;
            afVar.a(e, "startInstall(%s,%s)", this.a, this.b);
            this.c.b((Exception) new RuntimeException(e));
        }
    }
}
