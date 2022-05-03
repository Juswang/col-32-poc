package com.google.android.play.core.splitinstall;

/* loaded from: classes2.dex */
public final class s implements Runnable {
    final /* synthetic */ SplitInstallSessionState a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ t d;

    public s(t tVar, SplitInstallSessionState splitInstallSessionState, int i, int i2) {
        this.d = tVar;
        this.a = splitInstallSessionState;
        this.b = i;
        this.c = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        t tVar = this.d;
        SplitInstallSessionState splitInstallSessionState = this.a;
        tVar.a((t) new a(splitInstallSessionState.sessionId(), this.b, this.c, splitInstallSessionState.bytesDownloaded(), splitInstallSessionState.totalBytesToDownload(), splitInstallSessionState.a(), splitInstallSessionState.b(), splitInstallSessionState.resolutionIntent(), splitInstallSessionState.c()));
    }
}
