package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.af;

/* loaded from: classes2.dex */
public final class cs {
    private static final af a = new af("ExtractorTaskFinder");
    private final cp b;
    private final bb c;
    private final bk d;

    public cs(cp cpVar, bb bbVar, bk bkVar) {
        this.b = cpVar;
        this.c = bbVar;
        this.d = bkVar;
    }

    private final boolean a(cm cmVar, cn cnVar) {
        bb bbVar = this.c;
        cl clVar = cmVar.c;
        return bbVar.e(clVar.a, cmVar.b, clVar.b, cnVar.a).exists();
    }

    private static boolean a(cn cnVar) {
        int i = cnVar.f;
        return i == 1 || i == 2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0218, code lost:
        r0 = com.google.android.play.core.assetpacks.cs.a;
        r4 = new java.lang.Object[r8];
        r4[r7] = java.lang.Integer.valueOf(r6.a);
        r4[1] = r6.c.a;
        r4[2] = r10.a;
        r0.a("Found extraction task for patch for session %s, pack %s, slice %s.", r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0235, code lost:
        r11 = r33.c;
        r4 = r6.c;
        r0 = new java.io.FileInputStream(r11.e(r4.a, r6.b, r4.b, r10.a));
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x024c, code lost:
        r9 = r6.a;
        r11 = r6.c;
        r0 = new com.google.android.play.core.assetpacks.bs(r9, r11.a, r6.b, r11.b, r10.a, 0, 0, 1, r11.d, r11.c, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x027c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x02a2, code lost:
        throw new com.google.android.play.core.assetpacks.bv(java.lang.String.format("Error finding patch, session %s packName %s sliceId %s", java.lang.Integer.valueOf(r6.a), r6.c.a, r10.a), r0, r6.a);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x040c, code lost:
        if (r0 != null) goto L100;
     */
    @androidx.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.play.core.assetpacks.cr a() {
        /*
            Method dump skipped, instructions count: 1058
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.play.core.assetpacks.cs.a():com.google.android.play.core.assetpacks.cr");
    }
}
