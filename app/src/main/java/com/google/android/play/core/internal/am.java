package com.google.android.play.core.internal;

import android.os.IBinder;
import android.os.IInterface;
import java.util.List;

/* loaded from: classes2.dex */
final class am extends ag {
    final /* synthetic */ IBinder a;
    final /* synthetic */ ao b;

    public am(ao aoVar, IBinder iBinder) {
        this.b = aoVar;
        this.a = iBinder;
    }

    @Override // com.google.android.play.core.internal.ag
    public final void a() {
        al alVar;
        List<Runnable> list;
        List list2;
        ap apVar = this.b.a;
        alVar = apVar.h;
        apVar.l = (IInterface) alVar.a(this.a);
        ap.f(this.b.a);
        this.b.a.f = false;
        list = this.b.a.e;
        for (Runnable runnable : list) {
            runnable.run();
        }
        list2 = this.b.a.e;
        list2.clear();
    }
}
