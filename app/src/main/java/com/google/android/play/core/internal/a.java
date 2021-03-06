package com.google.android.play.core.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/* loaded from: classes2.dex */
public final class a implements b {
    private final ByteBuffer a;

    public a(ByteBuffer byteBuffer) {
        this.a = byteBuffer.slice();
    }

    @Override // com.google.android.play.core.internal.b
    public final long a() {
        return this.a.capacity();
    }

    @Override // com.google.android.play.core.internal.b
    public final void a(MessageDigest[] messageDigestArr, long j, int i) throws IOException {
        ByteBuffer slice;
        synchronized (this.a) {
            int i2 = (int) j;
            this.a.position(i2);
            this.a.limit(i2 + i);
            slice = this.a.slice();
        }
        for (MessageDigest messageDigest : messageDigestArr) {
            slice.position(0);
            messageDigest.update(slice);
        }
    }
}
