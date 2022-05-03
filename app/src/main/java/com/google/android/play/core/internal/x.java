package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public final class x extends i implements y {
    public x(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.assetpacks.protocol.IAssetPackExtractionServiceCallback");
    }

    @Override // com.google.android.play.core.internal.y
    public final void a(Bundle bundle) throws RemoteException {
        Parcel a = a();
        k.a(a, bundle);
        a(3, a);
    }

    @Override // com.google.android.play.core.internal.y
    public final void a(Bundle bundle, Bundle bundle2) throws RemoteException {
        Parcel a = a();
        k.a(a, bundle);
        k.a(a, bundle2);
        a(2, a);
    }

    @Override // com.google.android.play.core.internal.y
    public final void b(Bundle bundle) throws RemoteException {
        Parcel a = a();
        k.a(a, bundle);
        a(4, a);
    }
}
