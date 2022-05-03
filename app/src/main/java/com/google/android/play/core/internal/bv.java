package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public abstract class bv extends j implements bw {
    public bv() {
        super("com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceCallback");
    }

    @Override // com.google.android.play.core.internal.j
    protected final boolean a(int i, Parcel parcel) throws RemoteException {
        switch (i) {
            case 2:
                c(parcel.readInt(), (Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 3:
                int readInt = parcel.readInt();
                Bundle bundle = (Bundle) k.a(parcel, Bundle.CREATOR);
                a(readInt);
                return true;
            case 4:
                a(parcel.readInt(), (Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 5:
                b(parcel.readInt(), (Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 6:
                e((Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 7:
                a(parcel.createTypedArrayList(Bundle.CREATOR));
                return true;
            case 8:
                d((Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 9:
                a((Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 10:
                Bundle bundle2 = (Bundle) k.a(parcel, Bundle.CREATOR);
                b();
                return true;
            case 11:
                Bundle bundle3 = (Bundle) k.a(parcel, Bundle.CREATOR);
                a();
                return true;
            case 12:
                b((Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            case 13:
                c((Bundle) k.a(parcel, Bundle.CREATOR));
                return true;
            default:
                return false;
        }
    }
}
