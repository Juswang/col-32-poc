package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
final class ap extends ak<AssetPackStates> {
    private final bz c;
    private final az d;

    public ap(ar arVar, i<AssetPackStates> iVar, bz bzVar, az azVar) {
        super(arVar, iVar);
        this.c = bzVar;
        this.d = azVar;
    }

    @Override // com.google.android.play.core.assetpacks.ak, com.google.android.play.core.internal.u
    public final void c(Bundle bundle, Bundle bundle2) {
        super.c(bundle, bundle2);
        this.a.b((i<T>) AssetPackStates.a(bundle, this.c, this.d));
    }
}
