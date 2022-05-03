package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class at extends au<Integer> {
    public at(av avVar, i<Integer> iVar) {
        super(avVar, iVar);
    }

    @Override // com.google.android.play.core.splitinstall.au, com.google.android.play.core.internal.bw
    public final void c(int i, Bundle bundle) throws RemoteException {
        super.c(i, bundle);
        this.a.b((i<T>) Integer.valueOf(i));
    }
}
