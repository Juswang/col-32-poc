package com.google.android.play.core.splitinstall.testing;

import android.content.Intent;
import com.google.android.play.core.splitinstall.d;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public final class i implements d {
    final /* synthetic */ List a;
    final /* synthetic */ List b;
    final /* synthetic */ long c;
    final /* synthetic */ boolean d;
    final /* synthetic */ List e;
    final /* synthetic */ FakeSplitInstallManager f;

    public i(FakeSplitInstallManager fakeSplitInstallManager, List list, List list2, long j, boolean z, List list3) {
        this.f = fakeSplitInstallManager;
        this.a = list;
        this.b = list2;
        this.c = j;
        this.d = z;
        this.e = list3;
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void a() {
        Set set;
        Set set2;
        set = this.f.l;
        set.addAll(this.a);
        set2 = this.f.m;
        set2.addAll(this.b);
        this.f.a(5, 0, Long.valueOf(this.c), Long.valueOf(this.c), null, null, null);
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void a(int i) {
        this.f.a(i);
    }

    @Override // com.google.android.play.core.splitinstall.d
    public final void b() {
        if (!this.d) {
            this.f.a((List<Intent>) this.e, (List<String>) this.a, (List<String>) this.b, this.c, true);
        }
    }
}
