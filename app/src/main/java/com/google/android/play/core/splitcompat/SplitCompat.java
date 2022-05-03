package com.google.android.play.core.splitcompat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.play.core.internal.as;
import com.google.android.play.core.internal.at;
import com.google.android.play.core.internal.au;
import com.google.android.play.core.internal.av;
import com.google.android.play.core.internal.bo;
import com.google.android.play.core.internal.ci;
import com.google.android.play.core.splitinstall.l;
import com.google.android.play.core.splitinstall.o;
import com.google.android.play.core.splitinstall.p;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes2.dex */
public class SplitCompat {
    private static final AtomicReference<SplitCompat> a = new AtomicReference<>(null);
    private final c b;
    private final Set<String> c = new HashSet();
    private final a d;

    private SplitCompat(Context context) {
        try {
            this.b = new c(context);
            this.d = new a(this.b);
        } catch (PackageManager.NameNotFoundException e) {
            throw new bo(e);
        }
    }

    public static boolean a() {
        return a.get() != null;
    }

    public static boolean a(Context context) {
        return a(context, true);
    }

    private static boolean a(Context context, boolean z) {
        if (b()) {
            return false;
        }
        boolean compareAndSet = a.compareAndSet(null, new SplitCompat(context));
        SplitCompat splitCompat = a.get();
        if (compareAndSet) {
            l.a.a(new as(context, p.a(), new at(context, splitCompat.b, new av(), null), splitCompat.b, new p()));
            o.a(new l(splitCompat));
            p.a().execute(new m(context));
        }
        try {
            splitCompat.b(context, z);
            return true;
        } catch (Exception e) {
            Log.e("SplitCompat", "Error installing additional splits", e);
            return false;
        }
    }

    private final synchronized void b(Context context, boolean z) throws IOException {
        IOException e;
        ZipFile zipFile;
        if (z) {
            this.b.a();
        } else {
            p.a().execute(new n(this));
        }
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            List<String> arrayList = packageInfo.splitNames == null ? new ArrayList() : Arrays.asList(packageInfo.splitNames);
            Set<q> d = this.b.d();
            HashSet hashSet = new HashSet();
            Iterator<q> it = d.iterator();
            while (it.hasNext()) {
                String b = it.next().b();
                if (arrayList.contains(b)) {
                    if (z) {
                        this.b.f(b);
                    } else {
                        hashSet.add(b);
                    }
                    it.remove();
                }
            }
            if (!hashSet.isEmpty()) {
                p.a().execute(new o(this, hashSet));
            }
            HashSet hashSet2 = new HashSet();
            for (q qVar : d) {
                String b2 = qVar.b();
                if (!p.b(b2)) {
                    hashSet2.add(b2);
                }
            }
            for (String str : arrayList) {
                if (!p.b(str)) {
                    hashSet2.add(str);
                }
            }
            HashSet<q> hashSet3 = new HashSet(d.size());
            for (q qVar2 : d) {
                if (!p.a(qVar2.b())) {
                    String b3 = qVar2.b();
                    if (hashSet2.contains(p.a(b3) ? "" : b3.split("\\.config\\.", 2)[0])) {
                    }
                }
                hashSet3.add(qVar2);
            }
            k kVar = new k(this.b);
            au a2 = av.a();
            ClassLoader classLoader = context.getClassLoader();
            if (z) {
                a2.a(classLoader, kVar.a());
            } else {
                Iterator it2 = hashSet3.iterator();
                while (it2.hasNext()) {
                    Set<File> a3 = kVar.a((q) it2.next());
                    if (a3 == null) {
                        it2.remove();
                    } else {
                        a2.a(classLoader, a3);
                    }
                }
            }
            HashSet hashSet4 = new HashSet();
            for (q qVar3 : hashSet3) {
                try {
                    zipFile = new ZipFile(qVar3.a());
                } catch (IOException e2) {
                    e = e2;
                    zipFile = null;
                }
                try {
                    ZipEntry entry = zipFile.getEntry("classes.dex");
                    zipFile.close();
                    if (entry != null && !a2.a(classLoader, this.b.c(qVar3.b()), qVar3.a(), z)) {
                        String valueOf = String.valueOf(qVar3.a());
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 24);
                        sb.append("split was not installed ");
                        sb.append(valueOf);
                        Log.w("SplitCompat", sb.toString());
                    }
                    hashSet4.add(qVar3.a());
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
            a.b(context, hashSet4);
            HashSet hashSet5 = new HashSet();
            for (q qVar4 : hashSet3) {
                if (hashSet4.contains(qVar4.a())) {
                    String b4 = qVar4.b();
                    StringBuilder sb2 = new StringBuilder(String.valueOf(b4).length() + 30);
                    sb2.append("Split '");
                    sb2.append(b4);
                    sb2.append("' installation emulated");
                    Log.d("SplitCompat", sb2.toString());
                    hashSet5.add(qVar4.b());
                } else {
                    String b5 = qVar4.b();
                    StringBuilder sb3 = new StringBuilder(String.valueOf(b5).length() + 35);
                    sb3.append("Split '");
                    sb3.append(b5);
                    sb3.append("' installation not emulated.");
                    Log.d("SplitCompat", sb3.toString());
                }
            }
            synchronized (this.c) {
                this.c.addAll(hashSet5);
            }
        } catch (PackageManager.NameNotFoundException e5) {
            throw new IOException(String.format("Cannot load data for application '%s'", packageName), e5);
        }
    }

    private static boolean b() {
        return Build.VERSION.SDK_INT < 21;
    }

    public final Set<String> c() {
        HashSet hashSet;
        synchronized (this.c) {
            hashSet = new HashSet(this.c);
        }
        return hashSet;
    }

    public static boolean install(@NonNull Context context) {
        return a(context, false);
    }

    public static boolean installActivity(@NonNull Context context) {
        if (b()) {
            return false;
        }
        SplitCompat splitCompat = a.get();
        if (splitCompat != null) {
            return splitCompat.d.a(context, splitCompat.c());
        }
        throw new IllegalStateException("SplitCompat.installActivity can only be called if SplitCompat.install is first called at startup on application context.");
    }
}
