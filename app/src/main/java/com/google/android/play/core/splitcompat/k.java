package com.google.android.play.core.splitcompat;

import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.play.core.internal.ci;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes2.dex */
public final class k {
    public static final /* synthetic */ int a = 0;
    private static final Pattern b = Pattern.compile("lib/([^/]+)/(.*\\.so)$");
    private final c c;

    public k(c cVar) throws IOException {
        this.c = cVar;
    }

    public static /* synthetic */ Set a(k kVar, Set set, q qVar, ZipFile zipFile) throws IOException {
        HashSet hashSet = new HashSet();
        kVar.a(qVar, set, new g(hashSet, qVar, zipFile));
        return hashSet;
    }

    private static void a(q qVar, h hVar) throws IOException {
        IOException e;
        ZipFile zipFile;
        String[] strArr;
        String format;
        try {
            zipFile = new ZipFile(qVar.a());
        } catch (IOException e2) {
            e = e2;
            zipFile = null;
        }
        try {
            String b2 = qVar.b();
            HashMap hashMap = new HashMap();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry nextElement = entries.nextElement();
                Matcher matcher = b.matcher(nextElement.getName());
                if (matcher.matches()) {
                    String group = matcher.group(1);
                    String group2 = matcher.group(2);
                    Log.d("SplitCompat", String.format("NativeLibraryExtractor: split '%s' has native library '%s' for ABI '%s'", b2, group2, group));
                    Set set = (Set) hashMap.get(group);
                    if (set == null) {
                        set = new HashSet();
                        hashMap.put(group, set);
                    }
                    set.add(new j(nextElement, group2));
                }
            }
            HashMap hashMap2 = new HashMap();
            for (String str : Build.SUPPORTED_ABIS) {
                if (hashMap.containsKey(str)) {
                    Log.d("SplitCompat", String.format("NativeLibraryExtractor: there are native libraries for supported ABI %s; will use this ABI", str));
                    for (j jVar : (Set) hashMap.get(str)) {
                        if (hashMap2.containsKey(jVar.a)) {
                            format = String.format("NativeLibraryExtractor: skipping library %s for ABI %s; already present for a better ABI", jVar.a, str);
                        } else {
                            hashMap2.put(jVar.a, jVar);
                            format = String.format("NativeLibraryExtractor: using library %s for ABI %s", jVar.a, str);
                        }
                        Log.d("SplitCompat", format);
                    }
                } else {
                    Log.d("SplitCompat", String.format("NativeLibraryExtractor: there are no native libraries for supported ABI %s", str));
                }
            }
            hVar.a(zipFile, new HashSet(hashMap2.values()));
            zipFile.close();
        } catch (IOException e3) {
            e = e3;
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e4) {
                    ci.a(e, e4);
                }
            }
            throw e;
        }
    }

    public final void a(q qVar, Set<j> set, i iVar) throws IOException {
        for (j jVar : set) {
            File a2 = this.c.a(qVar.b(), jVar.a);
            boolean z = false;
            if (a2.exists() && a2.length() == jVar.b.getSize()) {
                z = true;
            }
            iVar.a(jVar, a2, z);
        }
    }

    public final Set<File> a() throws IOException {
        Log.d("SplitCompat", "NativeLibraryExtractor: synchronizing native libraries");
        Set<q> d = this.c.d();
        for (String str : this.c.e()) {
            Iterator<q> it = d.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().b().equals(str)) {
                        break;
                    }
                } else {
                    Log.i("SplitCompat", String.format("NativeLibraryExtractor: extracted split '%s' has no corresponding split; deleting", str));
                    this.c.d(str);
                    break;
                }
            }
        }
        HashSet hashSet = new HashSet();
        for (q qVar : d) {
            HashSet hashSet2 = new HashSet();
            a(qVar, new f(this, hashSet2, qVar));
            for (File file : this.c.e(qVar.b())) {
                if (!hashSet2.contains(file)) {
                    Log.i("SplitCompat", String.format("NativeLibraryExtractor: file '%s' found in split '%s' that is not in the split file '%s'; removing", file.getAbsolutePath(), qVar.b(), qVar.a().getAbsolutePath()));
                    this.c.b(file);
                }
            }
            hashSet.addAll(hashSet2);
        }
        return hashSet;
    }

    @Nullable
    public final Set<File> a(q qVar) throws IOException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        HashSet hashSet = new HashSet();
        a(qVar, new e(this, qVar, hashSet, atomicBoolean));
        if (atomicBoolean.get()) {
            return hashSet;
        }
        return null;
    }
}
