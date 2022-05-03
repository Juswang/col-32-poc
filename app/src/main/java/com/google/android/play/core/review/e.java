package com.google.android.play.core.review;

import android.os.RemoteException;
import com.google.android.play.core.common.PlayCoreVersion;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ag;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class e extends ag {
    final /* synthetic */ i a;
    final /* synthetic */ h b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(h hVar, i iVar, i iVar2) {
        super(iVar);
        this.b = hVar;
        this.a = iVar2;
    }

    @Override // com.google.android.play.core.internal.ag
    protected final void a() {
        af afVar;
        String str;
        String str2;
        try {
            str2 = this.b.c;
            this.b.a.b().a(str2, PlayCoreVersion.a(), new g(this.b, this.a));
        } catch (RemoteException e) {
            afVar = h.b;
            str = this.b.c;
            afVar.a(e, "error requesting in-app review for %s", str);
            this.a.b((Exception) new RuntimeException(e));
        }
    }
}
