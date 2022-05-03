package com.google.android.play.core.splitcompat;

import com.google.android.play.core.splitinstall.n;
import java.util.Set;

/* loaded from: classes2.dex */
public final class l implements n {
    final /* synthetic */ SplitCompat a;

    public l(SplitCompat splitCompat) {
        this.a = splitCompat;
    }

    @Override // com.google.android.play.core.splitinstall.n
    public final Set<String> a() {
        Set<String> c;
        c = this.a.c();
        return c;
    }
}
