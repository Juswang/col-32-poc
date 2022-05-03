package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public abstract class o extends j implements p {
    public o() {
        super("com.google.android.play.core.appupdate.protocol.IAppUpdateServiceCallback");
    }

    @Override // com.google.android.play.core.internal.j
    protected final boolean a(int i, Parcel parcel) throws RemoteException {
        if (i == 2) {
            a((Bundle) k.a(parcel, Bundle.CREATOR));
            return true;
        } else if (i != 3) {
            return false;
        } else {
            b((Bundle) k.a(parcel, Bundle.CREATOR));
            return true;
        }
    }
}
