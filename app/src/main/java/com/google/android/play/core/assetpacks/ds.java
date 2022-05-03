package com.google.android.play.core.assetpacks;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes2.dex */
public final /* synthetic */ class ds implements FilenameFilter {
    static final FilenameFilter a = new ds();

    private ds() {
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        boolean matches;
        matches = dt.a.matcher(str).matches();
        return matches;
    }
}
