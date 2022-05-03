package com.google.android.play.core.appupdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.play.core.install.InstallException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.internal.by;
import com.google.android.play.core.internal.n;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class p {
    private static final af b = new af("AppUpdateService");
    private static final Intent c = new Intent("com.google.android.play.core.install.BIND_UPDATE_SERVICE").setPackage("com.android.vending");
    @Nullable
    ap<n> a;
    private final String d;
    private final Context e;
    private final r f;

    public p(Context context, r rVar) {
        this.d = context.getPackageName();
        this.e = context;
        this.f = rVar;
        if (by.a(context)) {
            this.a = new ap<>(com.google.android.play.core.splitcompat.p.a(context), b, "AppUpdateService", c, j.a);
        }
    }

    public static /* synthetic */ Bundle a(p pVar, String str) {
        Integer num;
        Bundle bundle = new Bundle();
        bundle.putAll(d());
        bundle.putString("package.name", str);
        try {
            num = Integer.valueOf(pVar.e.getPackageManager().getPackageInfo(pVar.e.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            b.b("The current version of the app could not be retrieved", new Object[0]);
            num = null;
        }
        if (num != null) {
            bundle.putInt("app.version.code", num.intValue());
        }
        return bundle;
    }

    private static <T> Task<T> c() {
        b.b("onError(%d)", -9);
        return Tasks.a((Exception) new InstallException(-9));
    }

    public static Bundle d() {
        Bundle bundle = new Bundle();
        bundle.putInt("playcore.version.code", 10800);
        return bundle;
    }

    public final Task<AppUpdateInfo> a(String str) {
        if (this.a == null) {
            return c();
        }
        b.c("requestUpdateInfo(%s)", str);
        i iVar = new i();
        this.a.a(new k(this, iVar, str, iVar));
        return iVar.a();
    }

    public final Task<Void> b(String str) {
        if (this.a == null) {
            return c();
        }
        b.c("completeUpdate(%s)", str);
        i iVar = new i();
        this.a.a(new l(this, iVar, iVar, str));
        return iVar.a();
    }
}
