package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.cj;
import java.io.File;

/* loaded from: classes2.dex */
public final class bt {
    private static final af a = new af("ExtractChunkTaskHandler");
    private final byte[] b = new byte[8192];
    private final bb c;
    private final cj<w> d;
    private final cj<aw> e;
    private final bz f;

    public bt(bb bbVar, cj<w> cjVar, cj<aw> cjVar2, bz bzVar) {
        this.c = bbVar;
        this.d = cjVar;
        this.e = cjVar2;
        this.f = bzVar;
    }

    private final File b(bs bsVar) {
        File a2 = this.c.a(bsVar.k, bsVar.a, bsVar.b, bsVar.c);
        if (!a2.exists()) {
            a2.mkdirs();
        }
        return a2;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:111|6|(1:8)(2:9|10)|11|(2:13|(12:15|(1:(1:(2:19|(2:22|23))(2:24|25))(2:26|(10:28|(7:51|(4:52|(2:56|(1:64)(4:60|(1:62)|116|63))|65|(1:67)(1:114))|115|69|(1:71)|72|(2:74|(1:76)(2:77|(1:79)(3:80|(2:82|(1:84)(2:85|86))(1:87)|88))))|89|90|(2:107|92)|96|109|97|100|(2:102|103)(1:117))(2:29|30)))(2:31|(4:33|(4:34|(1:36)|37|(1:41)(1:112))|42|(1:44))(2:45|46))|21|(0)|89|90|(0)|96|109|97|100|(0)(0))(2:47|48))|49|(0)|89|90|(0)|96|109|97|100|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02f1, code lost:
        com.google.android.play.core.assetpacks.bt.a.d("Could not close file for chunk %s of slice %s of pack %s.", java.lang.Integer.valueOf(r23.e), r23.c, r23.k);
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0294 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:117:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0180 A[Catch: all -> 0x0330, TryCatch #4 {IOException -> 0x033c, blocks: (B:6:0x002f, B:9:0x0037, B:89:0x028b, B:11:0x0040, B:13:0x0046, B:15:0x0054, B:19:0x005e, B:22:0x0078, B:23:0x0081, B:24:0x0082, B:25:0x009b, B:26:0x009c, B:28:0x00c3, B:29:0x00cf, B:30:0x00d8, B:31:0x00d9, B:33:0x00f7, B:34:0x0109, B:36:0x011e, B:37:0x0123, B:42:0x0132, B:44:0x013b, B:45:0x0153, B:46:0x015c, B:47:0x015d, B:48:0x017c, B:51:0x0180, B:52:0x0189, B:54:0x0193, B:56:0x0199, B:58:0x019f, B:60:0x01a5, B:62:0x01c9, B:63:0x01d5, B:64:0x01d9, B:65:0x01e0, B:67:0x01e6, B:69:0x01ec, B:71:0x01f2, B:72:0x0202, B:74:0x0208, B:76:0x020e, B:77:0x0221, B:79:0x0227, B:80:0x0236, B:82:0x023c, B:85:0x0264, B:86:0x026b, B:87:0x026c, B:88:0x027d), top: B:111:0x002f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(com.google.android.play.core.assetpacks.bs r23) {
        /*
            Method dump skipped, instructions count: 886
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.play.core.assetpacks.bt.a(com.google.android.play.core.assetpacks.bs):void");
    }
}
