package com.google.android.play.core.assetpacks;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.play.core.common.a;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.cj;
import com.google.android.play.core.listener.b;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class aw extends b<AssetPackState> {
    private final cp c;
    private final bw d;
    private final cj<w> e;
    private final bn f;
    private final bz g;
    private final a h;
    private final cj<Executor> i;
    private final cj<Executor> j;
    private final Handler k = new Handler(Looper.getMainLooper());

    public aw(Context context, cp cpVar, bw bwVar, cj<w> cjVar, bz bzVar, bn bnVar, a aVar, cj<Executor> cjVar2, cj<Executor> cjVar3) {
        super(new af("AssetPackServiceListenerRegistry"), new IntentFilter("com.google.android.play.core.assetpacks.receiver.ACTION_SESSION_UPDATE"), context);
        this.c = cpVar;
        this.d = bwVar;
        this.e = cjVar;
        this.g = bzVar;
        this.f = bnVar;
        this.h = aVar;
        this.i = cjVar2;
        this.j = cjVar3;
    }

    @Override // com.google.android.play.core.listener.b
    public final void a(Context context, Intent intent) {
        Bundle bundleExtra = intent.getBundleExtra("com.google.android.play.core.assetpacks.receiver.EXTRA_SESSION_STATE");
        if (bundleExtra != null) {
            ArrayList<String> stringArrayList = bundleExtra.getStringArrayList("pack_names");
            if (stringArrayList == null || stringArrayList.size() != 1) {
                this.a.b("Corrupt bundle received from broadcast.", new Object[0]);
                return;
            }
            Bundle bundleExtra2 = intent.getBundleExtra("com.google.android.play.core.FLAGS");
            if (bundleExtra2 != null) {
                this.h.a(bundleExtra2);
            }
            AssetPackState a = AssetPackState.a(bundleExtra, stringArrayList.get(0), this.g, ay.a);
            this.a.a("ListenerRegistryBroadcastReceiver.onReceive: %s", a);
            PendingIntent pendingIntent = (PendingIntent) bundleExtra.getParcelable("confirmation_intent");
            if (pendingIntent != null) {
                this.f.a(pendingIntent);
            }
            this.j.a().execute(new Runnable(this, bundleExtra, a) { // from class: com.google.android.play.core.assetpacks.au
                private final aw a;
                private final Bundle b;
                private final AssetPackState c;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.a = this;
                    this.b = bundleExtra;
                    this.c = a;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.a.a(this.b, this.c);
                }
            });
            this.i.a().execute(new Runnable(this, bundleExtra) { // from class: com.google.android.play.core.assetpacks.av
                private final aw a;
                private final Bundle b;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.a = this;
                    this.b = bundleExtra;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.a.a(this.b);
                }
            });
            return;
        }
        this.a.b("Empty bundle received from broadcast.", new Object[0]);
    }

    public final /* synthetic */ void a(Bundle bundle) {
        if (this.c.a(bundle)) {
            this.d.a();
        }
    }

    public final /* synthetic */ void a(Bundle bundle, AssetPackState assetPackState) {
        if (this.c.b(bundle)) {
            a(assetPackState);
            this.e.a().a();
        }
    }

    public final void a(AssetPackState assetPackState) {
        this.k.post(new Runnable(this, assetPackState) { // from class: com.google.android.play.core.assetpacks.at
            private final aw a;
            private final AssetPackState b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = assetPackState;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a((aw) this.b);
            }
        });
    }
}
