package com.google.android.play.core.splitcompat;

import android.util.Log;

/* loaded from: classes2.dex */
public final class n implements Runnable {
    final /* synthetic */ SplitCompat a;

    public n(SplitCompat splitCompat) {
        this.a = splitCompat;
    }

    @Override // java.lang.Runnable
    public final void run() {
        c cVar;
        try {
            cVar = this.a.b;
            cVar.a();
        } catch (Exception e) {
            Log.e("SplitCompat", "Failed to cleanup splitcompat storage", e);
        }
    }
}
