package com.google.android.play.core.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public abstract class ca implements Closeable {
    public abstract long a();

    public abstract InputStream a(long j, long j2) throws IOException;

    public synchronized InputStream b() throws IOException {
        return a(0L, a());
    }
}
