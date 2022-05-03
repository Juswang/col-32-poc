package com.google.android.play.core.review;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.ac;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.tasks.i;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class f<T> extends ac {
    final af a;
    final i<T> b;
    final /* synthetic */ h c;

    public f(h hVar, af afVar, i<T> iVar) {
        this.c = hVar;
        this.a = afVar;
        this.b = iVar;
    }

    @Override // com.google.android.play.core.internal.ad
    public void a(Bundle bundle) throws RemoteException {
        this.c.a.a();
        this.a.c("onGetLaunchReviewFlowInfo", new Object[0]);
    }
}
