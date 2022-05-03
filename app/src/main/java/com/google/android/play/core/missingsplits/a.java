package com.google.android.play.core.missingsplits;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.play.core.internal.af;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class a {
    private static final af a = new af("MissingSplitsAppComponentsHelper");
    private final Context b;
    private final PackageManager c;

    public a(Context context, PackageManager packageManager) {
        this.b = context;
        this.c = packageManager;
    }

    private final void a(List<ComponentInfo> list, int i) {
        for (ComponentInfo componentInfo : list) {
            this.c.setComponentEnabledSetting(new ComponentName(componentInfo.packageName, componentInfo.name), i, 1);
        }
    }

    private final List<ComponentInfo> d() {
        try {
            ArrayList arrayList = new ArrayList();
            PackageInfo packageInfo = this.c.getPackageInfo(this.b.getPackageName(), 526);
            if (packageInfo.providers != null) {
                Collections.addAll(arrayList, packageInfo.providers);
            }
            if (packageInfo.receivers != null) {
                Collections.addAll(arrayList, packageInfo.receivers);
            }
            if (packageInfo.services != null) {
                Collections.addAll(arrayList, packageInfo.services);
            }
            return arrayList;
        } catch (PackageManager.NameNotFoundException e) {
            a.d("Failed to resolve own package : %s", e);
            return Collections.emptyList();
        }
    }

    public final boolean a() {
        for (ComponentInfo componentInfo : d()) {
            if (this.c.getComponentEnabledSetting(new ComponentName(componentInfo.packageName, componentInfo.name)) != 2) {
                a.a("Not all non-activity components are disabled", new Object[0]);
                return false;
            }
        }
        a.a("All non-activity components are disabled", new Object[0]);
        return true;
    }

    public final void b() {
        a.c("Disabling all non-activity components", new Object[0]);
        a(d(), 2);
    }

    public final void c() {
        a.c("Resetting enabled state of all non-activity components", new Object[0]);
        a(d(), 0);
    }
}
