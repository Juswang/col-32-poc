package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes2.dex */
public final class bs extends i implements bu {
    public bs(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.splitinstall.protocol.ISplitInstallService");
    }

    @Override // com.google.android.play.core.internal.bu
    public final void a(String str, int i, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeInt(i);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(4, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void a(String str, int i, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeInt(i);
        k.a(a, bwVar);
        a(5, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void a(String str, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bwVar);
        a(6, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void a(String str, List<Bundle> list, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(2, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void b(String str, List<Bundle> list, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(7, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void c(String str, List<Bundle> list, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(8, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void d(String str, List<Bundle> list, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(13, a);
    }

    @Override // com.google.android.play.core.internal.bu
    public final void e(String str, List<Bundle> list, Bundle bundle, bw bwVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, bwVar);
        a(14, a);
    }
}
