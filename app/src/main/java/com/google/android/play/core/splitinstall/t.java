package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.listener.b;

/* loaded from: classes2.dex */
public final class t extends b<SplitInstallSessionState> {
    @Nullable
    private static t c = null;
    private final Handler d = new Handler(Looper.getMainLooper());
    private final e e;

    public t(Context context, e eVar) {
        super(new af("SplitInstallListenerRegistry"), new IntentFilter("com.google.android.play.core.splitinstall.receiver.SplitInstallUpdateIntentService"), context);
        this.e = eVar;
    }

    public static synchronized t a(Context context) {
        t tVar;
        synchronized (t.class) {
            if (c == null) {
                c = new t(context, l.a);
            }
            tVar = c;
        }
        return tVar;
    }

    @Override // com.google.android.play.core.listener.b
    public final void a(Context context, Intent intent) {
        Bundle bundleExtra = intent.getBundleExtra("session_state");
        if (bundleExtra != null) {
            SplitInstallSessionState a = SplitInstallSessionState.a(bundleExtra);
            this.a.a("ListenerRegistryBroadcastReceiver.onReceive: %s", a);
            f a2 = this.e.a();
            if (a.status() != 3 || a2 == null) {
                a((t) a);
            } else {
                a2.a(a.c(), new r(this, a, intent, context));
            }
        }
    }
}
