package com.google.android.play.core.assetpacks;

import android.content.Context;
import androidx.annotation.NonNull;

/* loaded from: classes2.dex */
public class AssetPackManagerFactory {
    @NonNull
    public static synchronized AssetPackManager getInstance(@NonNull Context context) {
        AssetPackManager a;
        synchronized (AssetPackManagerFactory.class) {
            a = db.a(context).a();
        }
        return a;
    }
}
