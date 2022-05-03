package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
final class u implements Runnable {
    final /* synthetic */ SplitInstallRequest a;
    final /* synthetic */ w b;

    public u(w wVar, SplitInstallRequest splitInstallRequest) {
        this.b = wVar;
        this.a = splitInstallRequest;
    }

    @Override // java.lang.Runnable
    public final void run() {
        t tVar;
        List b;
        tVar = this.b.b;
        List<String> moduleNames = this.a.getModuleNames();
        b = w.b(this.a.getLanguages());
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", 0);
        bundle.putInt(NotificationCompat.CATEGORY_STATUS, 5);
        bundle.putInt("error_code", 0);
        if (!moduleNames.isEmpty()) {
            bundle.putStringArrayList("module_names", new ArrayList<>(moduleNames));
        }
        if (!b.isEmpty()) {
            bundle.putStringArrayList("languages", new ArrayList<>(b));
        }
        bundle.putLong("total_bytes_to_download", 0L);
        bundle.putLong("bytes_downloaded", 0L);
        tVar.a((t) SplitInstallSessionState.a(bundle));
    }
}
