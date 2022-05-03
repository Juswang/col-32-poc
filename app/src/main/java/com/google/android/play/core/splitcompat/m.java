package com.google.android.play.core.splitcompat;

import android.content.Context;
import com.google.android.play.core.splitinstall.t;

/* loaded from: classes2.dex */
public final class m implements Runnable {
    final /* synthetic */ Context a;

    public m(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        t.a(this.a).a(true);
    }
}
