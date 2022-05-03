package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.Intent;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode;

/* loaded from: classes2.dex */
final class r implements d {
    final /* synthetic */ SplitInstallSessionState a;
    final /* synthetic */ Intent b;
    final /* synthetic */ Context c;
    final /* synthetic */ t d;

    public r(t tVar, SplitInstallSessionState splitInstallSessionState, Intent intent, Context context) {
        this.d = tVar;
        this.a = splitInstallSessionState;
        this.b = intent;
        this.c = context;
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void a() {
        r0.d.post(new s(this.d, this.a, 5, 0));
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void a(@SplitInstallErrorCode int i) {
        r0.d.post(new s(this.d, this.a, 6, i));
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void b() {
        af afVar;
        if (!this.b.getBooleanExtra("triggered_from_app_after_verification", false)) {
            this.b.putExtra("triggered_from_app_after_verification", true);
            this.c.sendBroadcast(this.b);
            return;
        }
        afVar = this.d.a;
        afVar.b("Splits copied and verified more than once.", new Object[0]);
    }
}
