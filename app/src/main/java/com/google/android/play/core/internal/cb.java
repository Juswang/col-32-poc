package com.google.android.play.core.internal;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public final class cb extends ca {
    private final ca a;
    private final long b;
    private final long c;

    public cb(ca caVar, long j, long j2) {
        this.a = caVar;
        long a = a(j);
        this.b = a;
        this.c = a(a + j2);
    }

    private final long a(long j) {
        if (j < 0) {
            return 0L;
        }
        return j > this.a.a() ? this.a.a() : j;
    }

    @Override // com.google.android.play.core.internal.ca
    public final long a() {
        return this.c - this.b;
    }

    @Override // com.google.android.play.core.internal.ca
    public final InputStream a(long j, long j2) throws IOException {
        long a = a(this.b);
        return this.a.a(a, a(j2 + a) - a);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
    }
}
