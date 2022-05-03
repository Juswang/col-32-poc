package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class ar extends au<SplitInstallSessionState> {
    public ar(av avVar, i<SplitInstallSessionState> iVar) {
        super(avVar, iVar);
    }

    @Override // com.google.android.play.core.splitinstall.au, com.google.android.play.core.internal.bw
    public final void b(int i, Bundle bundle) throws RemoteException {
        super.b(i, bundle);
        this.a.b((i<T>) SplitInstallSessionState.a(bundle));
    }
}
