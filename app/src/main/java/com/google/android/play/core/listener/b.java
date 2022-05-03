package com.google.android.play.core.listener;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.Nullable;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.av;
import com.google.android.play.core.splitcompat.p;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes2.dex */
public abstract class b<StateT> {
    protected final af a;
    private final IntentFilter c;
    private final Context d;
    protected final Set<StateUpdatedListener<StateT>> b = new HashSet();
    @Nullable
    private a e = null;
    private volatile boolean f = false;

    public b(af afVar, IntentFilter intentFilter, Context context) {
        this.a = afVar;
        this.c = intentFilter;
        this.d = p.a(context);
    }

    private final void c() {
        a aVar;
        if ((this.f || !this.b.isEmpty()) && this.e == null) {
            this.e = new a(this);
            this.d.registerReceiver(this.e, this.c);
        }
        if (!this.f && this.b.isEmpty() && (aVar = this.e) != null) {
            this.d.unregisterReceiver(aVar);
            this.e = null;
        }
    }

    public final synchronized void a() {
        this.a.c("clearListeners", new Object[0]);
        this.b.clear();
        c();
    }

    public abstract void a(Context context, Intent intent);

    public final synchronized void a(StateUpdatedListener<StateT> stateUpdatedListener) {
        this.a.c("registerListener", new Object[0]);
        av.a(stateUpdatedListener, "Registered Play Core listener should not be null.");
        this.b.add(stateUpdatedListener);
        c();
    }

    public final synchronized void a(StateT statet) {
        Iterator it = new HashSet(this.b).iterator();
        while (it.hasNext()) {
            ((StateUpdatedListener) it.next()).onStateUpdate(statet);
        }
    }

    public final synchronized void a(boolean z) {
        this.f = z;
        c();
    }

    public final synchronized void b(StateUpdatedListener<StateT> stateUpdatedListener) {
        this.a.c("unregisterListener", new Object[0]);
        av.a(stateUpdatedListener, "Unregistered Play Core listener should not be null.");
        this.b.remove(stateUpdatedListener);
        c();
    }

    public final synchronized boolean b() {
        return this.e != null;
    }
}
