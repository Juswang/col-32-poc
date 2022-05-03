package com.google.android.play.core.internal;

/* loaded from: classes2.dex */
public final class bo extends RuntimeException {
    public bo(String str) {
        super(str);
    }

    public bo(Throwable th) {
        super("Failed to initialize FileStorage", th);
    }
}
