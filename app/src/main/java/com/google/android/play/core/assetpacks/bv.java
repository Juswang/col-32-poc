package com.google.android.play.core.assetpacks;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class bv extends RuntimeException {
    final int a;

    public bv(String str) {
        super(str);
        this.a = -1;
    }

    public bv(String str, int i) {
        super(str);
        this.a = i;
    }

    public bv(String str, Exception exc) {
        super(str, exc);
        this.a = -1;
    }

    public bv(String str, Exception exc, int i) {
        super(str, exc);
        this.a = i;
    }
}
