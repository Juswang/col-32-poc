package com.google.android.play.core.appupdate;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class c extends ResultReceiver {
    final /* synthetic */ i a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(Handler handler, i iVar) {
        super(handler);
        this.a = iVar;
    }

    @Override // android.os.ResultReceiver
    public final void onReceiveResult(int i, Bundle bundle) {
        i iVar;
        int i2 = 1;
        if (i == 1) {
            iVar = this.a;
            i2 = -1;
        } else if (i != 2) {
            iVar = this.a;
        } else {
            iVar = this.a;
            i2 = 0;
        }
        iVar.b((i) Integer.valueOf(i2));
    }
}
