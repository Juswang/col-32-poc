package com.google.android.play.core.internal;

import android.util.Log;
import com.google.android.play.core.splitinstall.d;
import java.util.List;

/* loaded from: classes2.dex */
final class ar implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ d b;
    final /* synthetic */ as c;

    public ar(as asVar, List list, d dVar) {
        this.c = asVar;
        this.a = list;
        this.b = dVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        at atVar;
        try {
            atVar = this.c.c;
            if (atVar.a(this.a)) {
                as.a(this.c, this.b);
            } else {
                as.a(this.c, this.a, this.b);
            }
        } catch (Exception e) {
            Log.e("SplitCompat", "Error checking verified files.", e);
            this.b.a(-11);
        }
    }
}
