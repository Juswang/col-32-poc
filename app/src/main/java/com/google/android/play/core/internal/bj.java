package com.google.android.play.core.internal;

import android.util.Log;
import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class bj implements ay {
    @Override // com.google.android.play.core.internal.ay
    public final boolean a(Object obj, File file, File file2) {
        try {
            return !((Boolean) bq.a(Class.forName("dalvik.system.DexFile"), Boolean.class, String.class, file.getPath())).booleanValue();
        } catch (ClassNotFoundException e) {
            Log.e("SplitCompat", "Unexpected missing dalvik.system.DexFile.");
            return false;
        }
    }
}
