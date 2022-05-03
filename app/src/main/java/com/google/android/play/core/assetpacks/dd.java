package com.google.android.play.core.assetpacks;

import androidx.annotation.Nullable;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class dd {
    private byte[] a = new byte[4096];
    private int b;
    private long c;
    private long d;
    private int e;
    private int f;
    private int g;
    private boolean h;
    @Nullable
    private String i;

    public dd() {
        c();
    }

    private final int a(int i, byte[] bArr, int i2, int i3) {
        int i4 = this.b;
        if (i4 >= i) {
            return 0;
        }
        int min = Math.min(i3, i - i4);
        System.arraycopy(bArr, i2, this.a, this.b, min);
        int i5 = this.b + min;
        this.b = i5;
        if (i5 < i) {
            return -1;
        }
        return min;
    }

    public final int a(byte[] bArr, int i, int i2) {
        int a = a(30, bArr, i, i2);
        if (a == -1) {
            return -1;
        }
        if (this.c == -1) {
            long b = db.b(this.a, 0);
            this.c = b;
            if (b == 67324752) {
                this.h = false;
                this.d = db.b(this.a, 18);
                this.g = db.c(this.a, 8);
                this.e = db.c(this.a, 26);
                int c = this.e + 30 + db.c(this.a, 28);
                this.f = c;
                int length = this.a.length;
                if (length < c) {
                    do {
                        length += length;
                    } while (length < c);
                    this.a = Arrays.copyOf(this.a, length);
                }
            } else {
                this.h = true;
            }
        }
        int a2 = a(this.f, bArr, i + a, i2 - a);
        if (a2 == -1) {
            return -1;
        }
        int i3 = a + a2;
        if (!this.h && this.i == null) {
            this.i = new String(this.a, 30, this.e);
        }
        return i3;
    }

    public final dx a() {
        int i = this.b;
        int i2 = this.f;
        if (i < i2) {
            return dx.a(this.i, this.d, this.g, true, Arrays.copyOf(this.a, i), this.h);
        }
        dx a = dx.a(this.i, this.d, this.g, false, Arrays.copyOf(this.a, i2), this.h);
        c();
        return a;
    }

    public final int b() {
        return this.f;
    }

    public final void c() {
        this.b = 0;
        this.e = -1;
        this.c = -1L;
        this.h = false;
        this.f = 30;
        this.d = -1L;
        this.g = -1;
        this.i = null;
    }
}
