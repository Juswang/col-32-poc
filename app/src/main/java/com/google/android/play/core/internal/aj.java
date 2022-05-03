package com.google.android.play.core.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.IInterface;

/* loaded from: classes2.dex */
public final class aj extends ag {
    final /* synthetic */ ap a;

    public aj(ap apVar) {
        this.a = apVar;
    }

    @Override // com.google.android.play.core.internal.ag
    public final void a() {
        IInterface iInterface;
        af afVar;
        Context context;
        ServiceConnection serviceConnection;
        iInterface = this.a.l;
        if (iInterface != null) {
            afVar = this.a.c;
            afVar.c("Unbind from service.", new Object[0]);
            context = this.a.b;
            serviceConnection = this.a.k;
            context.unbindService(serviceConnection);
            this.a.f = false;
            this.a.l = null;
            this.a.k = null;
        }
    }
}
