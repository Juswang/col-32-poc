package com.google.android.play.core.review;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class g extends f<ReviewInfo> {
    public g(h hVar, i iVar) {
        super(hVar, new af("OnRequestInstallCallback"), iVar);
    }

    @Override // com.google.android.play.core.review.f, com.google.android.play.core.internal.ad
    public final void a(Bundle bundle) throws RemoteException {
        super.a(bundle);
        this.b.b((i<T>) ReviewInfo.a((PendingIntent) bundle.get("confirmation_intent")));
    }
}
