package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.t;
import com.google.android.play.core.tasks.i;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ak<T> extends t {
    final i<T> a;
    final /* synthetic */ ar b;

    public ak(ar arVar, i<T> iVar) {
        this.b = arVar;
        this.a = iVar;
    }

    public ak(ar arVar, i iVar, byte[] bArr) {
        this(arVar, iVar);
    }

    public ak(ar arVar, i iVar, char[] cArr) {
        this(arVar, iVar);
    }

    public ak(ar arVar, i iVar, int[] iArr) {
        this(arVar, iVar);
    }

    public ak(ar arVar, i iVar, short[] sArr) {
        this(arVar, iVar);
    }

    @Override // com.google.android.play.core.internal.u
    public void a() {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onCancelDownloads()", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.u
    public final void a(int i) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onCancelDownload(%d)", Integer.valueOf(i));
    }

    @Override // com.google.android.play.core.internal.u
    public void a(int i, Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onStartDownload(%d)", Integer.valueOf(i));
    }

    @Override // com.google.android.play.core.internal.u
    public void a(Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        int i = bundle.getInt("error_code");
        afVar = ar.a;
        afVar.b("onError(%d)", Integer.valueOf(i));
        this.a.b(new AssetPackException(i));
    }

    @Override // com.google.android.play.core.internal.u
    public void a(Bundle bundle, Bundle bundle2) {
        ap apVar;
        af afVar;
        apVar = this.b.f;
        apVar.a();
        afVar = ar.a;
        afVar.c("onKeepAlive(%b)", Boolean.valueOf(bundle.getBoolean("keep_alive")));
    }

    @Override // com.google.android.play.core.internal.u
    public void a(List<Bundle> list) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onGetSessionStates", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.u
    public void b() {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onRemoveModule()", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.u
    public final void b(int i) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onGetSession(%d)", Integer.valueOf(i));
    }

    @Override // com.google.android.play.core.internal.u
    public void b(Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onNotifyChunkTransferred(%s, %s, %d, session=%d)", bundle.getString("module_name"), bundle.getString("slice_id"), Integer.valueOf(bundle.getInt("chunk_number")), Integer.valueOf(bundle.getInt("session_id")));
    }

    @Override // com.google.android.play.core.internal.u
    public void b(Bundle bundle, Bundle bundle2) throws RemoteException {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onGetChunkFileDescriptor", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.u
    public void c(Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onNotifyModuleCompleted(%s, sessionId=%d)", bundle.getString("module_name"), Integer.valueOf(bundle.getInt("session_id")));
    }

    @Override // com.google.android.play.core.internal.u
    public void c(Bundle bundle, Bundle bundle2) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onRequestDownloadInfo()", new Object[0]);
    }

    @Override // com.google.android.play.core.internal.u
    public void d(Bundle bundle) {
        ap apVar;
        af afVar;
        apVar = this.b.e;
        apVar.a();
        afVar = ar.a;
        afVar.c("onNotifySessionFailed(%d)", Integer.valueOf(bundle.getInt("session_id")));
    }
}
