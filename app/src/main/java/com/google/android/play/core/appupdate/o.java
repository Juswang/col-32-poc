package com.google.android.play.core.appupdate;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.install.InstallException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class o extends m<AppUpdateInfo> {
    final /* synthetic */ p d;
    private final String e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(p pVar, i<AppUpdateInfo> iVar, String str) {
        super(pVar, new af("OnRequestInstallCallback"), iVar);
        this.d = pVar;
        this.e = str;
    }

    @Override // com.google.android.play.core.appupdate.m, com.google.android.play.core.internal.p
    public final void a(Bundle bundle) throws RemoteException {
        int i;
        AppUpdateInfo a;
        int i2;
        super.a(bundle);
        i = bundle.getInt("error.code", -2);
        if (i != 0) {
            i<T> iVar = this.b;
            i2 = bundle.getInt("error.code", -2);
            iVar.b(new InstallException(i2));
            return;
        }
        i<T> iVar2 = this.b;
        a = AppUpdateInfo.a(this.e, bundle.getInt("version.code", -1), bundle.getInt("update.availability"), bundle.getInt("install.status", 0), r22.getInt("client.version.staleness", -1) == -1 ? null : Integer.valueOf(bundle.getInt("client.version.staleness")), bundle.getInt("in.app.update.priority", 0), bundle.getLong("bytes.downloaded"), bundle.getLong("total.bytes.to.download"), bundle.getLong("additional.size.required"), this.d.f.a(), (PendingIntent) bundle.getParcelable("blocking.intent"), (PendingIntent) bundle.getParcelable("nonblocking.intent"), (PendingIntent) bundle.getParcelable("blocking.destructive.intent"), (PendingIntent) bundle.getParcelable("nonblocking.destructive.intent"));
        iVar2.b((i<T>) a);
    }
}
