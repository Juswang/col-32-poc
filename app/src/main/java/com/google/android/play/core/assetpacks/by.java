package com.google.android.play.core.assetpacks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class by extends OutputStream {
    private final dd a = new dd();
    private final File b;
    private final dr c;
    private long d;
    private long e;
    private FileOutputStream f;
    private dx g;

    public by(File file, dr drVar) {
        this.b = file;
        this.c = drVar;
    }

    @Override // java.io.OutputStream
    public final void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        while (i2 > 0) {
            if (this.d == 0 && this.e == 0) {
                int a = this.a.a(bArr, i, i2);
                if (a != -1) {
                    i += a;
                    i2 -= a;
                    this.g = this.a.a();
                    if (this.g.g()) {
                        this.d = 0L;
                        this.c.b(this.g.h(), this.g.h().length);
                        this.e = this.g.h().length;
                    } else if (!this.g.b() || this.g.a()) {
                        byte[] h = this.g.h();
                        this.c.b(h, h.length);
                        this.d = this.g.d();
                    } else {
                        this.c.a(this.g.h());
                        File file = new File(this.b, this.g.c());
                        file.getParentFile().mkdirs();
                        this.d = this.g.d();
                        this.f = new FileOutputStream(file);
                    }
                } else {
                    return;
                }
            }
            if (!this.g.a()) {
                if (this.g.g()) {
                    this.c.a(this.e, bArr, i, i2);
                    this.e += i2;
                    i3 = i2;
                } else if (this.g.b()) {
                    i3 = (int) Math.min(i2, this.d);
                    this.f.write(bArr, i, i3);
                    long j = this.d - i3;
                    this.d = j;
                    if (j == 0) {
                        this.f.close();
                    }
                } else {
                    i3 = (int) Math.min(i2, this.d);
                    this.c.a((this.g.h().length + this.g.d()) - this.d, bArr, i, i3);
                    this.d -= i3;
                }
                i += i3;
                i2 -= i3;
            }
        }
    }
}
