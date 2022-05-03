package com.google.android.play.core.assetpacks;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final /* synthetic */ class di implements Runnable {
    private final bb a;

    private di(bb bbVar) {
        this.a = bbVar;
    }

    public static Runnable a(bb bbVar) {
        return new di(bbVar);
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.c();
    }
}
