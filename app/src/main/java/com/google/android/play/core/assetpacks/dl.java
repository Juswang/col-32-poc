package com.google.android.play.core.assetpacks;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.play.core.internal.af;

/* loaded from: classes2.dex */
public final class dl {
    private static final af a = new af("PackageStateCache");
    private final Context b;
    private int c = -1;

    public dl(Context context) {
        this.b = context;
    }

    public final synchronized int a() {
        if (this.c == -1) {
            try {
                this.c = this.b.getPackageManager().getPackageInfo(this.b.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                a.b("The current version of the app could not be retrieved", new Object[0]);
            }
        }
        return this.c;
    }
}
