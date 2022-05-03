package com.google.android.play.core.assetpacks;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.by;
import com.google.android.play.core.internal.v;
import com.google.android.play.core.internal.y;
import java.util.Arrays;

/* loaded from: classes2.dex */
final class b extends v {
    private final af a = new af("AssetPackExtractionService");
    private final Context b;
    private final AssetPackExtractionService c;
    private final bb d;

    public b(Context context, AssetPackExtractionService assetPackExtractionService, bb bbVar) {
        this.b = context;
        this.c = assetPackExtractionService;
        this.d = bbVar;
    }

    @Override // com.google.android.play.core.internal.w
    public final void a(Bundle bundle, y yVar) throws RemoteException {
        String[] packagesForUid;
        this.a.a("updateServiceState AIDL call", new Object[0]);
        if (!by.a(this.b) || (packagesForUid = this.b.getPackageManager().getPackagesForUid(Binder.getCallingUid())) == null || !Arrays.asList(packagesForUid).contains("com.android.vending")) {
            yVar.a(new Bundle());
            this.c.a();
            return;
        }
        yVar.a(this.c.a(bundle), new Bundle());
    }

    @Override // com.google.android.play.core.internal.w
    public final void a(y yVar) throws RemoteException {
        this.d.f();
        yVar.b(new Bundle());
    }
}
