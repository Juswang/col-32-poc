package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public final class l extends i implements n {
    public l(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.appupdate.protocol.IAppUpdateService");
    }

    @Override // com.google.android.play.core.internal.n
    public final void a(String str, Bundle bundle, p pVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, pVar);
        a(2, a);
    }

    @Override // com.google.android.play.core.internal.n
    public final void b(String str, Bundle bundle, p pVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, pVar);
        a(3, a);
    }
}
