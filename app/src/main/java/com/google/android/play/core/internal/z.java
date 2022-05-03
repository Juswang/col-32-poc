package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public final class z extends i implements ab {
    public z(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.inappreview.protocol.IInAppReviewService");
    }

    @Override // com.google.android.play.core.internal.ab
    public final void a(String str, Bundle bundle, ad adVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, adVar);
        a(2, a);
    }
}
