package com.google.android.play.core.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes2.dex */
public final class q extends i implements s {
    public q(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.assetpacks.protocol.IAssetModuleService");
    }

    @Override // com.google.android.play.core.internal.s
    public final void a(String str, Bundle bundle, Bundle bundle2, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, bundle2);
        k.a(a, uVar);
        a(6, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void a(String str, Bundle bundle, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, uVar);
        a(5, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void a(String str, List<Bundle> list, Bundle bundle, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, uVar);
        a(2, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void b(String str, Bundle bundle, Bundle bundle2, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, bundle2);
        k.a(a, uVar);
        a(7, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void b(String str, Bundle bundle, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, uVar);
        a(10, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void b(String str, List<Bundle> list, Bundle bundle, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, uVar);
        a(14, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void c(String str, Bundle bundle, Bundle bundle2, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, bundle2);
        k.a(a, uVar);
        a(9, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void c(String str, List<Bundle> list, Bundle bundle, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        a.writeTypedList(list);
        k.a(a, bundle);
        k.a(a, uVar);
        a(12, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void d(String str, Bundle bundle, Bundle bundle2, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, bundle2);
        k.a(a, uVar);
        a(11, a);
    }

    @Override // com.google.android.play.core.internal.s
    public final void e(String str, Bundle bundle, Bundle bundle2, u uVar) throws RemoteException {
        Parcel a = a();
        a.writeString(str);
        k.a(a, bundle);
        k.a(a, bundle2);
        k.a(a, uVar);
        a(13, a);
    }
}
