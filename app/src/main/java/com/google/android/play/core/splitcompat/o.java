package com.google.android.play.core.splitcompat;

import android.util.Log;
import java.util.Set;

/* loaded from: classes2.dex */
public final class o implements Runnable {
    final /* synthetic */ Set a;
    final /* synthetic */ SplitCompat b;

    public o(SplitCompat splitCompat, Set set) {
        this.b = splitCompat;
        this.a = set;
    }

    @Override // java.lang.Runnable
    public final void run() {
        c cVar;
        try {
            for (String str : this.a) {
                cVar = this.b.b;
                cVar.f(str);
            }
        } catch (Exception e) {
            Log.e("SplitCompat", "Failed to remove from splitcompat storage split that is already installed", e);
        }
    }
}
