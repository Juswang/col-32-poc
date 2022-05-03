package com.google.android.play.core.appupdate;

import android.content.Context;
import com.google.android.play.core.splitcompat.p;

/* loaded from: classes2.dex */
public final /* synthetic */ class w {
    private static y a;

    public static synchronized y a(Context context) {
        y yVar;
        synchronized (w.class) {
            if (a == null) {
                x xVar = new x(null);
                xVar.a(new g(p.a(context)));
                a = xVar.a();
            }
            yVar = a;
        }
        return yVar;
    }
}
