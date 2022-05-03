package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.bv;
import com.google.android.play.core.tasks.i;
import java.util.List;

/* loaded from: classes2.dex */
public class au<T> extends bv {
    final i<T> a;
    final /* synthetic */ av b;

    public au(av avVar, i<T> iVar) {
        this.b = avVar;
        this.a = iVar;
    }

    @Override // com.google.android.play.core.internal.bw
    public final void a() throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onCompleteInstallForAppUpdate", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.bw
    public final void a(int i) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onCompleteInstall(%d)", Integer.valueOf(i));
    }

    public void a(int i, Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onCancelInstall(%d)", Integer.valueOf(i));
    }

    public void a(Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onDeferredInstall", new Object[0]);
    }

    public void a(List<Bundle> list) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onGetSessionStates", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.bw
    public final void b() throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onGetSplitsForAppUpdate", new Object[0]);
    }

    public void b(int i, Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onGetSession(%d)", Integer.valueOf(i));
    }

    public void b(Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onDeferredLanguageInstall", new Object[0]);
    }

    public void c(int i, Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onStartInstall(%d)", Integer.valueOf(i));
    }

    public void c(Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onDeferredLanguageUninstall", new Object[0]);
    }

    public void d(Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        afVar = av.b;
        afVar.c("onDeferredUninstall", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.bw
    public final void e(Bundle bundle) throws RemoteException {
        af afVar;
        this.b.a.a();
        int i = bundle.getInt("error_code");
        afVar = av.b;
        afVar.b("onError(%d)", Integer.valueOf(i));
        this.a.b(new SplitInstallException(i));
    }
}
