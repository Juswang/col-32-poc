package com.google.android.play.core.assetpacks;

import android.os.Bundle;
import com.google.android.play.core.tasks.i;
import java.util.List;

/* loaded from: classes2.dex */
final class aq extends ak<AssetPackStates> {
    private final List<String> c;
    private final bz d;

    public aq(ar arVar, i<AssetPackStates> iVar, bz bzVar, List<String> list) {
        super(arVar, iVar);
        this.d = bzVar;
        this.c = list;
    }

    @Override // com.google.android.play.core.assetpacks.ak, com.google.android.play.core.internal.u
    public final void a(int i, Bundle bundle) {
        super.a(i, bundle);
        this.a.b((i<T>) AssetPackStates.a(bundle, this.d, this.c));
    }
}
