package com.google.android.play.core.internal;

import java.io.File;
import java.util.Set;

/* loaded from: classes2.dex */
public final class bn implements au {
    @Override // com.google.android.play.core.internal.au
    public final void a(ClassLoader classLoader, Set<File> set) {
        bk.b(classLoader, set);
    }

    @Override // com.google.android.play.core.internal.au
    public final boolean a(ClassLoader classLoader, File file, File file2, boolean z) {
        return ba.a(classLoader, file, file2, z, bf.a(), "path", new bm());
    }
}
