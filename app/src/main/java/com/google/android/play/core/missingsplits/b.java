package com.google.android.play.core.missingsplits;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.google.android.play.core.internal.af;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public final class b implements MissingSplitsManager {
    private static final af a = new af("MissingSplitsManagerImpl");
    private final Context b;
    private final Runtime c;
    private final a d;
    private final AtomicReference<Boolean> e;

    public b(Context context, Runtime runtime, a aVar, AtomicReference<Boolean> atomicReference) {
        this.b = context;
        this.c = runtime;
        this.d = aVar;
        this.e = atomicReference;
    }

    @TargetApi(21)
    private final boolean a() {
        try {
            ApplicationInfo applicationInfo = this.b.getPackageManager().getApplicationInfo(this.b.getPackageName(), 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null)) {
                if (Boolean.TRUE.equals(applicationInfo.metaData.get("com.android.vending.splits.required"))) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            a.d("App '%s' is not found in the PackageManager", this.b.getPackageName());
            return false;
        }
    }

    private static boolean a(Set<String> set) {
        if (!set.isEmpty()) {
            return set.size() == 1 && set.contains("");
        }
        return true;
    }

    private final Set<String> b() {
        if (Build.VERSION.SDK_INT < 21) {
            return Collections.emptySet();
        }
        try {
            PackageInfo packageInfo = this.b.getPackageManager().getPackageInfo(this.b.getPackageName(), 0);
            HashSet hashSet = new HashSet();
            if (packageInfo == null || packageInfo.splitNames == null) {
                return hashSet;
            }
            Collections.addAll(hashSet, packageInfo.splitNames);
            return hashSet;
        } catch (PackageManager.NameNotFoundException e) {
            a.d("App '%s' is not found in PackageManager", this.b.getPackageName());
            return Collections.emptySet();
        }
    }

    @TargetApi(21)
    private final List<ActivityManager.AppTask> c() {
        List<ActivityManager.AppTask> appTasks = ((ActivityManager) this.b.getSystemService("activity")).getAppTasks();
        return appTasks != null ? appTasks : Collections.emptyList();
    }

    @Override // com.google.android.play.core.missingsplits.MissingSplitsManager
    public final boolean disableAppIfMissingRequiredSplits() {
        boolean booleanValue;
        boolean z;
        Class<?> cls;
        boolean z2;
        ApplicationInfo applicationInfo;
        Set set;
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        synchronized (this.e) {
            if (this.e.get() == null) {
                AtomicReference<Boolean> atomicReference = this.e;
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        applicationInfo = this.b.getPackageManager().getApplicationInfo(this.b.getPackageName(), 128);
                    } catch (PackageManager.NameNotFoundException e) {
                        a.d("App '%s' is not found in the PackageManager", this.b.getPackageName());
                    }
                    if (!(applicationInfo == null || applicationInfo.metaData == null)) {
                        if (Boolean.TRUE.equals(applicationInfo.metaData.get("com.android.vending.splits.required"))) {
                            if (Build.VERSION.SDK_INT >= 21) {
                                try {
                                    PackageInfo packageInfo = this.b.getPackageManager().getPackageInfo(this.b.getPackageName(), 0);
                                    HashSet hashSet = new HashSet();
                                    if (!(packageInfo == null || packageInfo.splitNames == null)) {
                                        Collections.addAll(hashSet, packageInfo.splitNames);
                                    }
                                    set = hashSet;
                                } catch (PackageManager.NameNotFoundException e2) {
                                    a.d("App '%s' is not found in PackageManager", this.b.getPackageName());
                                }
                                if (!set.isEmpty() || (set.size() == 1 && set.contains(""))) {
                                    z2 = true;
                                    atomicReference.set(Boolean.valueOf(z2));
                                }
                            }
                            set = Collections.emptySet();
                            if (!set.isEmpty()) {
                            }
                            z2 = true;
                            atomicReference.set(Boolean.valueOf(z2));
                        }
                    }
                }
                z2 = false;
                atomicReference.set(Boolean.valueOf(z2));
            }
            booleanValue = this.e.get().booleanValue();
        }
        if (booleanValue) {
            Iterator<ActivityManager.AppTask> it = c().iterator();
            while (true) {
                if (it.hasNext()) {
                    ActivityManager.AppTask next = it.next();
                    if (!(next.getTaskInfo() == null || next.getTaskInfo().baseIntent == null || next.getTaskInfo().baseIntent.getComponent() == null || !PlayCoreMissingSplitsActivity.class.getName().equals(next.getTaskInfo().baseIntent.getComponent().getClassName()))) {
                        break;
                    }
                } else {
                    loop1: for (ActivityManager.AppTask appTask : c()) {
                        ActivityManager.RecentTaskInfo taskInfo = appTask.getTaskInfo();
                        if (!(taskInfo == null || taskInfo.baseIntent == null || taskInfo.baseIntent.getComponent() == null)) {
                            ComponentName component = taskInfo.baseIntent.getComponent();
                            String className = component.getClassName();
                            try {
                                cls = Class.forName(className);
                            } catch (ClassNotFoundException e3) {
                                a.d("ClassNotFoundException when scanning class hierarchy of '%s'", className);
                                try {
                                    if (this.b.getPackageManager().getActivityInfo(component, 0) != null) {
                                    }
                                } catch (PackageManager.NameNotFoundException e4) {
                                }
                            }
                            while (cls != null) {
                                if (cls.equals(Activity.class)) {
                                    z = true;
                                    break loop1;
                                }
                                Class<? super Object> superclass = cls.getSuperclass();
                                cls = superclass != cls ? superclass : null;
                            }
                            continue;
                        }
                    }
                    z = false;
                    this.d.b();
                    for (ActivityManager.AppTask appTask2 : c()) {
                        appTask2.finishAndRemoveTask();
                    }
                    if (z) {
                        this.b.getPackageManager().setComponentEnabledSetting(new ComponentName(this.b, PlayCoreMissingSplitsActivity.class), 1, 1);
                        this.b.startActivity(new Intent(this.b, PlayCoreMissingSplitsActivity.class).addFlags(884998144));
                    }
                    this.c.exit(0);
                }
            }
            return true;
        }
        if (this.d.a()) {
            this.d.c();
            this.c.exit(0);
        }
        return false;
    }

    @Override // com.google.android.play.core.missingsplits.MissingSplitsManager
    public final boolean isMissingRequiredSplits() {
        boolean booleanValue;
        synchronized (this.e) {
            if (this.e.get() == null) {
                AtomicReference<Boolean> atomicReference = this.e;
                boolean z = false;
                if (Build.VERSION.SDK_INT >= 21 && a() && a(b())) {
                    z = true;
                }
                atomicReference.set(Boolean.valueOf(z));
            }
            booleanValue = this.e.get().booleanValue();
        }
        return booleanValue;
    }
}
