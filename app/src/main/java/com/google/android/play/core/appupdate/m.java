package com.google.android.play.core.appupdate;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.o;
import com.google.android.play.core.tasks.i;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class m<T> extends o {
    final af a;
    final i<T> b;
    final /* synthetic */ p c;

    public m(p pVar, af afVar, i<T> iVar) {
        this.c = pVar;
        this.a = afVar;
        this.b = iVar;
    }

    @Override // com.google.android.play.core.internal.p
    public void a(Bundle bundle) throws RemoteException {
        this.c.a.a();
        this.a.c("onRequestInfo", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.p
    public void b(Bundle bundle) throws RemoteException {
        this.c.a.a();
        this.a.c("onCompleteUpdate", new Object[0]);
    }
}
