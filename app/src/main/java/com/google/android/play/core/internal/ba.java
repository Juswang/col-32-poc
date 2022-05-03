package com.google.android.play.core.internal;

import android.util.Log;
import com.google.android.play.core.splitinstall.k;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public final class ba implements au {
    public static az a() {
        return new aw();
    }

    public static Object a(ClassLoader classLoader) {
        return bq.a(classLoader, "pathList", Object.class).a();
    }

    public static boolean a(ClassLoader classLoader, File file, File file2, boolean z, az azVar, String str, ay ayVar) {
        ArrayList<IOException> arrayList = new ArrayList<>();
        Object a = a(classLoader);
        bp b = bq.b(a, "dexElements", Object.class);
        List<Object> asList = Arrays.asList((Object[]) b.a());
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : asList) {
            arrayList2.add((File) bq.a(obj, str, File.class).a());
        }
        if (arrayList2.contains(file2)) {
            return true;
        }
        if (z || ayVar.a(a, file2, file)) {
            b.a((Collection) Arrays.asList(azVar.a(a, new ArrayList<>(Collections.singleton(file2)), file, arrayList)));
            if (arrayList.isEmpty()) {
                return true;
            }
            bo boVar = new bo("DexPathList.makeDexElement failed");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                IOException iOException = arrayList.get(i);
                Log.e("SplitCompat", "DexPathList.makeDexElement failed", iOException);
                ci.a(boVar, iOException);
            }
            bq.b(a, "dexElementsSuppressedExceptions", IOException.class).a((Collection) arrayList);
            throw boVar;
        }
        String valueOf = String.valueOf(file2.getPath());
        Log.w("SplitCompat", valueOf.length() != 0 ? "Should be optimized ".concat(valueOf) : new String("Should be optimized "));
        return false;
    }

    public static ay b() {
        return new ax();
    }

    public static void b(ClassLoader classLoader, Set<File> set) {
        if (!set.isEmpty()) {
            HashSet hashSet = new HashSet();
            for (File file : set) {
                String valueOf = String.valueOf(file.getParentFile().getAbsolutePath());
                Log.d("Splitcompat", valueOf.length() != 0 ? "Adding native library parent directory: ".concat(valueOf) : new String("Adding native library parent directory: "));
                hashSet.add(file.getParentFile());
            }
            bp b = bq.b(a(classLoader), "nativeLibraryDirectories", File.class);
            hashSet.removeAll(Arrays.asList((File[]) b.a()));
            synchronized (k.class) {
                int size = hashSet.size();
                StringBuilder sb = new StringBuilder(30);
                sb.append("Adding directories ");
                sb.append(size);
                Log.d("Splitcompat", sb.toString());
                b.b(hashSet);
            }
        }
    }

    @Override // com.google.android.play.core.internal.au
    public final void a(ClassLoader classLoader, Set<File> set) {
        b(classLoader, set);
    }

    @Override // com.google.android.play.core.internal.au
    public final boolean a(ClassLoader classLoader, File file, File file2, boolean z) {
        return a(classLoader, file, file2, z, a(), "zip", b());
    }
}
