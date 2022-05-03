package com.google.android.play.core.splitinstall;

import android.content.Context;
import com.google.android.play.core.splitcompat.p;

/* loaded from: classes2.dex */
public final class k {
    private static m a;

    private k() {
    }

    public static synchronized m a(Context context) {
        m mVar;
        synchronized (k.class) {
            if (a == null) {
                b bVar = new b(null);
                bVar.a(new y(p.a(context)));
                a = bVar.a();
            }
            mVar = a;
        }
        return mVar;
    }
}
