package com.google.android.play.core.assetpacks;

import androidx.annotation.Nullable;
import com.google.android.play.core.assetpacks.model.AssetPackStorageMethod;

/* loaded from: classes2.dex */
public abstract class AssetPackLocation {
    private static final AssetPackLocation a = new bg(1, null, null);

    public static AssetPackLocation a() {
        return a;
    }

    public static AssetPackLocation a(String str, String str2) {
        return new bg(0, str, str2);
    }

    @Nullable
    public abstract String assetsPath();

    @AssetPackStorageMethod
    public abstract int packStorageMethod();

    @Nullable
    public abstract String path();
}
