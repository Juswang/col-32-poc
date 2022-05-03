package com.google.android.play.core.assetpacks;

/* loaded from: classes2.dex */
public abstract class AssetLocation {
    public static AssetLocation a(String str, long j, long j2) {
        return new bf(str, j, j2);
    }

    public abstract long offset();

    public abstract String path();

    public abstract long size();
}
