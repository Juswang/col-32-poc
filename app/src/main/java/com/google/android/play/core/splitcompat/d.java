package com.google.android.play.core.splitcompat;

import java.io.File;
import java.io.IOException;

/* loaded from: classes2.dex */
final class d implements i {
    final /* synthetic */ e a;

    public d(e eVar) {
        this.a = eVar;
    }

    @Override // com.google.android.play.core.splitcompat.i
    public final void a(j jVar, File file, boolean z) throws IOException {
        this.a.b.add(file);
        if (!z) {
            this.a.c.set(false);
        }
    }
}
