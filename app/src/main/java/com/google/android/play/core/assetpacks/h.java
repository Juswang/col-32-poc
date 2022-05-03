package com.google.android.play.core.assetpacks;

import com.google.android.play.core.tasks.OnFailureListener;

/* loaded from: classes2.dex */
public final /* synthetic */ class h implements OnFailureListener {
    static final OnFailureListener a = new h();

    private h() {
    }

    @Override // com.google.android.play.core.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        j.a.d(String.format("Could not sync active asset packs. %s", exc), new Object[0]);
    }
}
