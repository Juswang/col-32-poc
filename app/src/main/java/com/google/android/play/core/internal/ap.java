package com.google.android.play.core.internal;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import com.google.android.play.core.tasks.i;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public final class ap<T extends IInterface> {
    private static final Map<String, Handler> a = new HashMap();
    private final Context b;
    private final af c;
    private final String d;
    private boolean f;
    private final Intent g;
    private final al<T> h;
    @Nullable
    private ServiceConnection k;
    @Nullable
    private T l;
    private final List<ag> e = new ArrayList();
    private final IBinder.DeathRecipient j = new IBinder.DeathRecipient(this) { // from class: com.google.android.play.core.internal.ah
        private final ap a;

        /* JADX INFO: Access modifiers changed from: package-private */
        {
            this.a = this;
        }

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            this.a.c();
        }
    };
    private final WeakReference<ak> i = new WeakReference<>(null);

    public ap(Context context, af afVar, String str, Intent intent, al<T> alVar) {
        this.b = context;
        this.c = afVar;
        this.d = str;
        this.g = intent;
        this.h = alVar;
    }

    public static /* synthetic */ void a(ap apVar, ag agVar) {
        if (apVar.l == null && !apVar.f) {
            apVar.c.c("Initiate binding to the service.", new Object[0]);
            apVar.e.add(agVar);
            apVar.k = new ao(apVar);
            apVar.f = true;
            if (!apVar.b.bindService(apVar.g, apVar.k, 1)) {
                apVar.c.c("Failed to bind to the service.", new Object[0]);
                apVar.f = false;
                List<ag> list = apVar.e;
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    i<?> b = list.get(i).b();
                    if (b != null) {
                        b.b((Exception) new aq());
                    }
                }
                apVar.e.clear();
            }
        } else if (apVar.f) {
            apVar.c.c("Waiting to bind to the service.", new Object[0]);
            apVar.e.add(agVar);
        } else {
            agVar.run();
        }
    }

    public final void b(ag agVar) {
        Handler handler;
        synchronized (a) {
            if (!a.containsKey(this.d)) {
                HandlerThread handlerThread = new HandlerThread(this.d, 10);
                handlerThread.start();
                a.put(this.d, new Handler(handlerThread.getLooper()));
            }
            handler = a.get(this.d);
        }
        handler.post(agVar);
    }

    public static /* synthetic */ void f(ap apVar) {
        apVar.c.c("linkToDeath", new Object[0]);
        try {
            apVar.l.asBinder().linkToDeath(apVar.j, 0);
        } catch (RemoteException e) {
            apVar.c.a(e, "linkToDeath failed", new Object[0]);
        }
    }

    public static /* synthetic */ void h(ap apVar) {
        apVar.c.c("unlinkToDeath", new Object[0]);
        apVar.l.asBinder().unlinkToDeath(apVar.j, 0);
    }

    public final void a() {
        b(new aj(this));
    }

    public final void a(ag agVar) {
        b(new ai(this, agVar.b(), agVar));
    }

    @Nullable
    public final T b() {
        return this.l;
    }

    public final /* bridge */ /* synthetic */ void c() {
        this.c.c("reportBinderDeath", new Object[0]);
        ak akVar = this.i.get();
        if (akVar == null) {
            this.c.c("%s : Binder has died.", this.d);
            List<ag> list = this.e;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                i<?> b = list.get(i).b();
                if (b != null) {
                    b.b((Exception) (Build.VERSION.SDK_INT < 15 ? new RemoteException() : new RemoteException(String.valueOf(this.d).concat(" : Binder has died."))));
                }
            }
            this.e.clear();
            return;
        }
        this.c.c("calling onBinderDied", new Object[0]);
        akVar.a();
    }
}
