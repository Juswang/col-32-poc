package com.google.android.play.core.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/* loaded from: classes2.dex */
public final class ao implements ServiceConnection {
    final /* synthetic */ ap a;

    public /* synthetic */ ao(ap apVar) {
        this.a = apVar;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        af afVar;
        afVar = this.a.c;
        afVar.c("ServiceConnectionImpl.onServiceConnected(%s)", componentName);
        this.a.b(new am(this, iBinder));
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        af afVar;
        afVar = this.a.c;
        afVar.c("ServiceConnectionImpl.onServiceDisconnected(%s)", componentName);
        this.a.b(new an(this));
    }
}
