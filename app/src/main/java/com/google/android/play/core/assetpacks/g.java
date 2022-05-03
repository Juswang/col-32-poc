package com.google.android.play.core.assetpacks;

import com.google.android.play.core.tasks.OnSuccessListener;
import java.util.List;

/* loaded from: classes2.dex */
public final /* synthetic */ class g implements OnSuccessListener {
    private final bb a;

    private g(bb bbVar) {
        this.a = bbVar;
    }

    public static OnSuccessListener a(bb bbVar) {
        return new g(bbVar);
    }

    @Override // com.google.android.play.core.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        this.a.a((List) obj);
    }
}
