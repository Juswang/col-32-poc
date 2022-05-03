package com.google.android.play.core.assetpacks;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class bm extends FilterInputStream {
    private long c;
    private final dd a = new dd();
    private byte[] b = new byte[4096];
    private boolean d = false;
    private boolean e = false;

    public bm(InputStream inputStream) {
        super(inputStream);
    }

    private final int a(byte[] bArr, int i, int i2) throws IOException {
        return Math.max(0, super.read(bArr, i, i2));
    }

    private final boolean a(int i) throws IOException {
        int a = a(this.b, 0, i);
        if (a != i) {
            int i2 = i - a;
            if (a(this.b, a, i2) != i2) {
                this.a.a(this.b, 0, a);
                return false;
            }
        }
        this.a.a(this.b, 0, i);
        return true;
    }

    public final dx a() throws IOException {
        byte[] bArr;
        if (this.c > 0) {
            do {
                bArr = this.b;
            } while (read(bArr, 0, bArr.length) != -1);
            if (!this.d || this.e) {
                return new dx(null, -1L, -1, false, false, null);
            }
            if (!a(30)) {
                this.d = true;
                return this.a.a();
            }
            dx a = this.a.a();
            if (a.g()) {
                this.e = true;
                return a;
            } else if (a.d() != 4294967295L) {
                int b = this.a.b() - 30;
                long j = b;
                int length = this.b.length;
                if (j > length) {
                    do {
                        length += length;
                    } while (length < j);
                    this.b = Arrays.copyOf(this.b, length);
                }
                if (!a(b)) {
                    this.d = true;
                    return this.a.a();
                }
                dx a2 = this.a.a();
                this.c = a2.d();
                return a2;
            } else {
                throw new bv("Files bigger than 4GiB are not supported.");
            }
        } else {
            if (!this.d) {
            }
            return new dx(null, -1L, -1, false, false, null);
        }
    }

    public final boolean b() {
        return this.d;
    }

    public final boolean c() {
        return this.e;
    }

    public final long d() {
        return this.c;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr, int i, int i2) throws IOException {
        long j = this.c;
        if (j <= 0 || this.d) {
            return -1;
        }
        int a = a(bArr, i, (int) Math.min(j, i2));
        this.c -= a;
        if (a != 0) {
            return a;
        }
        this.d = true;
        return 0;
    }
}
