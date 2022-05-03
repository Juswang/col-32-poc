package com.google.android.play.core.assetpacks;

import android.content.Context;
import android.content.pm.PackageInfo;
import androidx.annotation.Nullable;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ci;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class bb {
    private static final af a = new af("AssetPackStorage");
    private static final long b = TimeUnit.DAYS.toMillis(14);
    private static final long c = TimeUnit.DAYS.toMillis(28);
    private final Context d;
    private final dl e;

    public bb(Context context, dl dlVar) {
        this.d = context;
        this.e = dlVar;
    }

    private final File a(String str, int i) {
        return new File(g(str), String.valueOf(i));
    }

    private static List<String> a(PackageInfo packageInfo, String str) {
        ArrayList arrayList = new ArrayList();
        if (packageInfo.splitNames == null) {
            return arrayList;
        }
        for (int i = (-Arrays.binarySearch(packageInfo.splitNames, str)) - 1; i < packageInfo.splitNames.length && packageInfo.splitNames[i].startsWith(str); i++) {
            arrayList.add(packageInfo.applicationInfo.splitSourceDirs[i]);
        }
        return arrayList;
    }

    private static void a(File file) {
        File[] listFiles;
        if (file.listFiles() != null && file.listFiles().length > 1) {
            long b2 = b(file);
            for (File file2 : file.listFiles()) {
                if (!file2.getName().equals(String.valueOf(b2)) && !file2.getName().equals("stale.tmp")) {
                    c(file2);
                }
            }
        }
    }

    private static long b(File file) {
        File[] listFiles;
        if (!file.exists()) {
            return -1L;
        }
        ArrayList arrayList = new ArrayList();
        try {
            for (File file2 : file.listFiles()) {
                if (!file2.getName().equals("stale.tmp")) {
                    arrayList.add(Long.valueOf(file2.getName()));
                }
            }
        } catch (NumberFormatException e) {
            a.a(e, "Corrupt asset pack directories.", new Object[0]);
        }
        if (arrayList.isEmpty()) {
            return -1L;
        }
        Collections.sort(arrayList);
        return ((Long) arrayList.get(arrayList.size() - 1)).longValue();
    }

    private static boolean c(File file) {
        boolean z;
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            z = true;
            for (File file2 : listFiles) {
                z &= c(file2);
            }
        } else {
            z = true;
        }
        return file.delete() && true == z;
    }

    private final File g(String str) {
        return new File(i(), str);
    }

    private final File g(String str, int i, long j) {
        return new File(c(str, i, j), "merge.tmp");
    }

    private final List<File> g() {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        try {
        } catch (IOException e) {
            a.b("Could not process directory while scanning installed packs. %s", e);
        }
        if (i().exists() && i().listFiles() != null) {
            for (File file : i().listFiles()) {
                if (!file.getCanonicalPath().equals(h().getCanonicalPath())) {
                    arrayList.add(file);
                }
            }
            return arrayList;
        }
        return arrayList;
    }

    private final File h() {
        return new File(i(), "_tmp");
    }

    private final File h(String str, int i, long j) {
        return new File(new File(new File(h(), str), String.valueOf(i)), String.valueOf(j));
    }

    private final File i() {
        return new File(this.d.getFilesDir(), "assetpacks");
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0074  */
    @androidx.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.play.core.assetpacks.AssetLocation a(java.lang.String r10, java.lang.String r11) {
        /*
            r9 = this;
            r0 = 0
            r1 = 0
            android.content.Context r2 = r9.d     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L13
            android.content.pm.PackageManager r2 = r2.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L13
            android.content.Context r3 = r9.d     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L13
            java.lang.String r3 = r3.getPackageName()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L13
            android.content.pm.PackageInfo r2 = r2.getPackageInfo(r3, r1)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L13
            goto L1e
        L13:
            r2 = move-exception
            com.google.android.play.core.internal.af r2 = com.google.android.play.core.assetpacks.bb.a
            java.lang.Object[] r3 = new java.lang.Object[r1]
            java.lang.String r4 = "Could not find PackageInfo."
            r2.b(r4, r3)
            r2 = r0
        L1e:
            r3 = 1
            if (r2 != 0) goto L23
            r4 = r0
            goto L88
        L23:
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            int r5 = android.os.Build.VERSION.SDK_INT
            r6 = 21
            if (r5 >= r6) goto L36
            android.content.pm.ApplicationInfo r2 = r2.applicationInfo
            java.lang.String r2 = r2.sourceDir
            r4.add(r2)
            goto L88
        L36:
            java.lang.String[] r5 = r2.splitNames
            if (r5 == 0) goto L5c
            android.content.pm.ApplicationInfo r5 = r2.applicationInfo
            java.lang.String[] r5 = r5.splitSourceDirs
            if (r5 != 0) goto L41
            goto L5c
        L41:
            java.lang.String[] r5 = r2.splitNames
            int r5 = java.util.Arrays.binarySearch(r5, r10)
            if (r5 >= 0) goto L55
            com.google.android.play.core.internal.af r5 = com.google.android.play.core.assetpacks.bb.a
            java.lang.Object[] r6 = new java.lang.Object[r3]
            r6[r1] = r10
            java.lang.String r7 = "Asset Pack '%s' is not installed."
            r5.a(r7, r6)
            goto L67
        L55:
            android.content.pm.ApplicationInfo r6 = r2.applicationInfo
            java.lang.String[] r6 = r6.splitSourceDirs
            r5 = r6[r5]
            goto L68
        L5c:
            com.google.android.play.core.internal.af r5 = com.google.android.play.core.assetpacks.bb.a
            java.lang.Object[] r6 = new java.lang.Object[r3]
            r6[r1] = r10
            java.lang.String r7 = "No splits present for package %s."
            r5.a(r7, r6)
        L67:
            r5 = r0
        L68:
            if (r5 != 0) goto L74
            android.content.pm.ApplicationInfo r5 = r2.applicationInfo
            java.lang.String r5 = r5.sourceDir
            r4.add(r5)
            java.lang.String r5 = "config."
            goto L81
        L74:
            r4.add(r5)
            java.lang.String r5 = java.lang.String.valueOf(r10)
            java.lang.String r6 = ".config."
            java.lang.String r5 = r5.concat(r6)
        L81:
            java.util.List r2 = a(r2, r5)
            r4.addAll(r2)
        L88:
            if (r4 != 0) goto L8b
            goto Lcd
        L8b:
            java.io.File r2 = new java.io.File
            java.lang.String r5 = "assets"
            r2.<init>(r5, r11)
            java.lang.String r2 = r2.getPath()
            int r5 = r4.size()
            r6 = 0
        L9b:
            r7 = 2
            if (r6 >= r5) goto Lbd
            java.lang.Object r8 = r4.get(r6)
            java.lang.String r8 = (java.lang.String) r8
            com.google.android.play.core.assetpacks.AssetLocation r7 = com.google.android.play.core.assetpacks.db.a(r8, r2)     // Catch: java.io.IOException -> Lae
            int r6 = r6 + 1
            if (r7 == 0) goto L9b
            r0 = r7
            goto Lcd
        Lae:
            r10 = move-exception
            com.google.android.play.core.internal.af r2 = com.google.android.play.core.assetpacks.bb.a
            java.lang.Object[] r4 = new java.lang.Object[r7]
            r4[r1] = r8
            r4[r3] = r11
            java.lang.String r11 = "Failed to parse APK file '%s' looking for asset '%s'."
            r2.a(r10, r11, r4)
            goto Lcd
        Lbd:
            com.google.android.play.core.internal.af r2 = com.google.android.play.core.assetpacks.bb.a
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r1] = r11
            r5[r3] = r10
            r5[r7] = r4
            java.lang.String r10 = "The asset %s is not present in Asset Pack %s. Searched in APKs: %s"
            r2.a(r10, r5)
        Lcd:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.play.core.assetpacks.bb.a(java.lang.String, java.lang.String):com.google.android.play.core.assetpacks.AssetLocation");
    }

    @Nullable
    public final AssetLocation a(String str, String str2, AssetPackLocation assetPackLocation) {
        File file = new File(assetPackLocation.assetsPath(), str2);
        if (file.exists()) {
            return AssetLocation.a(file.getPath(), 0L, file.length());
        }
        a.a("The asset %s is not present in Asset Pack %s. Searched in folder: %s", str2, str, assetPackLocation.assetsPath());
        return null;
    }

    public final File a(String str, int i, long j) {
        return new File(a(str, i), String.valueOf(j));
    }

    public final File a(String str, int i, long j, String str2) {
        return new File(new File(new File(h(str, i, j), "_slices"), "_unverified"), str2);
    }

    public final Map<String, AssetPackLocation> a() {
        HashMap hashMap = new HashMap();
        try {
            for (File file : g()) {
                AssetPackLocation b2 = b(file.getName());
                if (b2 != null) {
                    hashMap.put(file.getName(), b2);
                }
            }
        } catch (IOException e) {
            a.b("Could not process directory while scanning installed packs: %s", e);
        }
        return hashMap;
    }

    public final void a(String str, int i, long j, int i2) throws IOException {
        File g = g(str, i, j);
        Properties properties = new Properties();
        properties.put("numberOfMerges", String.valueOf(i2));
        g.getParentFile().mkdirs();
        g.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(g);
        properties.store(fileOutputStream, (String) null);
        fileOutputStream.close();
    }

    public final void a(List<String> list) {
        int a2 = this.e.a();
        List<File> g = g();
        int size = g.size();
        for (int i = 0; i < size; i++) {
            File file = g.get(i);
            if (!list.contains(file.getName()) && b(file) != a2) {
                c(file);
            }
        }
    }

    public final boolean a(String str) {
        try {
            return c(str) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Nullable
    public final AssetPackLocation b(String str) throws IOException {
        String c2 = c(str);
        if (c2 == null) {
            return null;
        }
        File file = new File(c2, "assets");
        if (file.isDirectory()) {
            return AssetPackLocation.a(c2, file.getCanonicalPath());
        }
        a.b("Failed to find assets directory: %s", file);
        return null;
    }

    public final File b(String str, int i, long j) {
        return new File(a(str, i, j), "_metadata");
    }

    public final File b(String str, int i, long j, String str2) {
        return new File(new File(new File(h(str, i, j), "_slices"), "_verified"), str2);
    }

    public final Map<String, Long> b() {
        HashMap hashMap = new HashMap();
        for (String str : a().keySet()) {
            hashMap.put(str, Long.valueOf(f(str)));
        }
        return hashMap;
    }

    public final File c(String str, int i, long j) {
        return new File(h(str, i, j), "_packs");
    }

    public final File c(String str, int i, long j, String str2) {
        return new File(f(str, i, j, str2), "checkpoint.dat");
    }

    @Nullable
    final String c(String str) throws IOException {
        int length;
        File file = new File(i(), str);
        if (!file.exists()) {
            a.a("Pack not found with pack name: %s", str);
            return null;
        }
        File file2 = new File(file, String.valueOf(this.e.a()));
        if (!file2.exists()) {
            a.a("Pack not found with pack name: %s app version: %s", str, Integer.valueOf(this.e.a()));
            return null;
        }
        File[] listFiles = file2.listFiles();
        if (listFiles == null || (length = listFiles.length) == 0) {
            a.a("No pack version found for pack name: %s app version: %s", str, Integer.valueOf(this.e.a()));
            return null;
        } else if (length <= 1) {
            return listFiles[0].getCanonicalPath();
        } else {
            a.b("Multiple pack versions found for pack name: %s app version: %s", str, Integer.valueOf(this.e.a()));
            return null;
        }
    }

    public final void c() {
        List<File> g = g();
        int size = g.size();
        for (int i = 0; i < size; i++) {
            File file = g.get(i);
            if (file.listFiles() != null) {
                a(file);
                long b2 = b(file);
                if (this.e.a() != b2) {
                    try {
                        new File(new File(file, String.valueOf(b2)), "stale.tmp").createNewFile();
                    } catch (IOException e) {
                        a.b("Could not write staleness marker.", new Object[0]);
                    }
                }
                for (File file2 : file.listFiles()) {
                    a(file2);
                }
            }
        }
    }

    public final int d(String str, int i, long j) throws IOException {
        File g = g(str, i, j);
        if (!g.exists()) {
            return 0;
        }
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(g);
        try {
            properties.load(fileInputStream);
            fileInputStream.close();
            if (properties.getProperty("numberOfMerges") != null) {
                try {
                    return Integer.parseInt(properties.getProperty("numberOfMerges"));
                } catch (NumberFormatException e) {
                    throw new bv("Merge checkpoint file corrupt.", e);
                }
            } else {
                throw new bv("Merge checkpoint file corrupt.");
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                ci.a(th, th2);
            }
            throw th;
        }
    }

    public final File d(String str, int i, long j, String str2) {
        return new File(f(str, i, j, str2), "checkpoint_ext.dat");
    }

    public final void d() {
        File[] listFiles;
        List<File> g = g();
        int size = g.size();
        for (int i = 0; i < size; i++) {
            File file = g.get(i);
            if (file.listFiles() != null) {
                for (File file2 : file.listFiles()) {
                    File file3 = new File(file2, "stale.tmp");
                    if (file3.exists() && System.currentTimeMillis() - file3.lastModified() > c) {
                        c(file2);
                    }
                }
            }
        }
    }

    public final boolean d(String str) {
        if (!g(str).exists()) {
            return true;
        }
        return c(g(str));
    }

    public final int e(String str) {
        return (int) b(g(str));
    }

    public final File e(String str, int i, long j) {
        return new File(new File(h(str, i, j), "_slices"), "_metadata");
    }

    public final File e(String str, int i, long j, String str2) {
        return new File(f(str, i, j, str2), "slice.zip");
    }

    public final void e() {
        File[] listFiles;
        if (h().exists()) {
            for (File file : h().listFiles()) {
                if (System.currentTimeMillis() - file.lastModified() > b) {
                    c(file);
                } else {
                    a(file);
                }
            }
        }
    }

    public final long f(String str) {
        return b(a(str, e(str)));
    }

    public final File f(String str, int i, long j, String str2) {
        return new File(e(str, i, j), str2);
    }

    public final void f() {
        c(i());
    }

    public final void f(String str, int i, long j) {
        if (h(str, i, j).exists()) {
            c(h(str, i, j));
        }
    }
}
