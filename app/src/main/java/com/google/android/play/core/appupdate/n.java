package com.google.android.play.core.appupdate;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.install.InstallException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class n extends m<Void> {
    public n(p pVar, i<Void> iVar) {
        super(pVar, new af("OnCompleteUpdateCallback"), iVar);
    }

    @Override // com.google.android.play.core.appupdate.m, com.google.android.play.core.internal.p
    public final void b(Bundle bundle) throws RemoteException {
        int i;
        int i2;
        super.b(bundle);
        i = bundle.getInt("error.code", -2);
        if (i != 0) {
            i<T> iVar = this.b;
            i2 = bundle.getInt("error.code", -2);
            iVar.b(new InstallException(i2));
            return;
        }
        this.b.b((i<T>) null);
    }
}
