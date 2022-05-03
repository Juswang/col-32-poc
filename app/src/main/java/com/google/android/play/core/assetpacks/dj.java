package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.cj;
import java.io.File;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class dj {
    private final bb a;
    private final cj<w> b;
    private final cp c;
    private final cj<Executor> d;
    private final bz e;

    public dj(bb bbVar, cj<w> cjVar, cp cpVar, cj<Executor> cjVar2, bz bzVar) {
        this.a = bbVar;
        this.b = cjVar;
        this.c = cpVar;
        this.d = cjVar2;
        this.e = bzVar;
    }

    public final void a(dh dhVar) {
        File c = this.a.c(dhVar.k, dhVar.a, dhVar.b);
        File e = this.a.e(dhVar.k, dhVar.a, dhVar.b);
        if (!c.exists() || !e.exists()) {
            throw new bv(String.format("Cannot find pack files to move for pack %s.", dhVar.k), dhVar.j);
        }
        File a = this.a.a(dhVar.k, dhVar.a, dhVar.b);
        a.mkdirs();
        if (c.renameTo(a)) {
            new File(this.a.a(dhVar.k, dhVar.a, dhVar.b), "merge.tmp").delete();
            File b = this.a.b(dhVar.k, dhVar.a, dhVar.b);
            b.mkdirs();
            if (e.renameTo(b)) {
                bb bbVar = this.a;
                bbVar.getClass();
                this.d.a().execute(di.a(bbVar));
                this.c.a(dhVar.k, dhVar.a, dhVar.b);
                this.e.a(dhVar.k);
                this.b.a().a(dhVar.j, dhVar.k);
                return;
            }
            throw new bv("Cannot move metadata files to final location.", dhVar.j);
        }
        throw new bv("Cannot move merged pack files to final location.", dhVar.j);
    }
}
