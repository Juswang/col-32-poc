package com.google.android.play.core.assetpacks;

import androidx.annotation.Nullable;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class dx {
    private final String a;
    private final long b;
    private final int c;
    private final boolean d;
    private final boolean e;
    private final byte[] f;

    dx() {
    }

    public dx(@Nullable String str, long j, int i, boolean z, boolean z2, @Nullable byte[] bArr) {
        this();
        this.a = str;
        this.b = j;
        this.c = i;
        this.d = z;
        this.e = z2;
        this.f = bArr;
    }

    public static dx a(@Nullable String str, long j, int i, boolean z, byte[] bArr, boolean z2) {
        return new dx(str, j, i, z, z2, bArr);
    }

    public final boolean a() {
        if (c() == null) {
            return false;
        }
        return c().endsWith("/");
    }

    public final boolean b() {
        return e() == 0;
    }

    @Nullable
    public String c() {
        return this.a;
    }

    public long d() {
        return this.b;
    }

    public int e() {
        return this.c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof dx) {
            dx dxVar = (dx) obj;
            String str = this.a;
            if (str != null ? str.equals(dxVar.c()) : dxVar.c() == null) {
                if (this.b == dxVar.d() && this.c == dxVar.e() && this.d == dxVar.f() && this.e == dxVar.g()) {
                    if (Arrays.equals(this.f, dxVar instanceof dx ? dxVar.f : dxVar.h())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean f() {
        return this.d;
    }

    public boolean g() {
        return this.e;
    }

    @Nullable
    public byte[] h() {
        return this.f;
    }

    public int hashCode() {
        String str = this.a;
        int hashCode = str == null ? 0 : str.hashCode();
        long j = this.b;
        int i = 1237;
        int i2 = (((((((hashCode ^ 1000003) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ this.c) * 1000003) ^ (true != this.d ? 1237 : 1231)) * 1000003;
        if (true == this.e) {
            i = 1231;
        }
        return ((i2 ^ i) * 1000003) ^ Arrays.hashCode(this.f);
    }

    public String toString() {
        String str = this.a;
        long j = this.b;
        int i = this.c;
        boolean z = this.d;
        boolean z2 = this.e;
        String arrays = Arrays.toString(this.f);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 126 + String.valueOf(arrays).length());
        sb.append("ZipEntry{name=");
        sb.append(str);
        sb.append(", size=");
        sb.append(j);
        sb.append(", compressionMethod=");
        sb.append(i);
        sb.append(", isPartial=");
        sb.append(z);
        sb.append(", isEndOfArchive=");
        sb.append(z2);
        sb.append(", headerBytes=");
        sb.append(arrays);
        sb.append("}");
        return sb.toString();
    }
}
