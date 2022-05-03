package com.google.android.play.core.assetpacks;

import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.cj;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final class bw {
    private static final af a = new af("ExtractorLooper");
    private final cp b;
    private final bt c;
    private final dv d;
    private final df e;
    private final dj f;
    private final Cdo g;
    private final cj<w> h;
    private final cs i;
    private final AtomicBoolean j = new AtomicBoolean(false);

    public bw(cp cpVar, cj<w> cjVar, bt btVar, dv dvVar, df dfVar, dj djVar, Cdo doVar, cs csVar) {
        this.b = cpVar;
        this.h = cjVar;
        this.c = btVar;
        this.d = dvVar;
        this.e = dfVar;
        this.f = djVar;
        this.g = doVar;
        this.i = csVar;
    }

    private final void a(int i, Exception exc) {
        try {
            this.b.d(i);
            this.b.a(i);
        } catch (bv e) {
            a.b("Error during error handling: %s", exc.getMessage());
        }
    }

    public final void a() {
        a.a("Run extractor loop", new Object[0]);
        if (this.j.compareAndSet(false, true)) {
            while (true) {
                cr crVar = null;
                try {
                    crVar = this.i.a();
                } catch (bv e) {
                    a.b("Error while getting next extraction task: %s", e.getMessage());
                    if (e.a >= 0) {
                        this.h.a().a(e.a);
                        a(e.a, e);
                    }
                }
                if (crVar != null) {
                    try {
                        if (crVar instanceof bs) {
                            this.c.a((bs) crVar);
                        } else if (crVar instanceof du) {
                            this.d.a((du) crVar);
                        } else if (crVar instanceof de) {
                            this.e.a((de) crVar);
                        } else if (crVar instanceof dh) {
                            this.f.a((dh) crVar);
                        } else if (crVar instanceof dn) {
                            this.g.a((dn) crVar);
                        } else {
                            a.b("Unknown task type: %s", crVar.getClass().getName());
                        }
                    } catch (Exception e2) {
                        a.b("Error during extraction task: %s", e2.getMessage());
                        this.h.a().a(crVar.j);
                        a(crVar.j, e2);
                    }
                } else {
                    this.j.set(false);
                    return;
                }
            }
        } else {
            a.d("runLoop already looping; return", new Object[0]);
        }
    }
}
