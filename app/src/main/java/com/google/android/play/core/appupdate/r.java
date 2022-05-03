package com.google.android.play.core.appupdate;

import android.content.Context;
import java.io.File;

/* loaded from: classes2.dex */
public final class r {
    private final Context a;

    public r(Context context) {
        this.a = context;
    }

    private static long a(File file) {
        if (!file.isDirectory()) {
            return file.length();
        }
        File[] listFiles = file.listFiles();
        long j = 0;
        if (listFiles != null) {
            for (File file2 : listFiles) {
                j += a(file2);
            }
        }
        return j;
    }

    public final long a() {
        return a(new File(this.a.getFilesDir(), "assetpacks"));
    }
}
