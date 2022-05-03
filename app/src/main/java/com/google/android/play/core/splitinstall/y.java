package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import java.io.File;

/* loaded from: classes2.dex */
public final class y {
    private final Context a;

    public y(Context context) {
        this.a = context;
    }

    @Nullable
    public static File a(Context context) {
        String string;
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle == null || (string = bundle.getString("local_testing_dir")) == null) {
                return null;
            }
            return new File(context.getExternalFilesDir(null), string);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public final t a() {
        return t.a(this.a);
    }

    public final Context b() {
        return this.a;
    }
}
