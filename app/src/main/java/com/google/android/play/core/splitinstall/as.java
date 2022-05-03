package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.play.core.tasks.i;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
final class as extends au<List<SplitInstallSessionState>> {
    public as(av avVar, i<List<SplitInstallSessionState>> iVar) {
        super(avVar, iVar);
    }

    @Override // com.google.android.play.core.splitinstall.au, com.google.android.play.core.internal.bw
    public final void a(List<Bundle> list) throws RemoteException {
        super.a(list);
        ArrayList arrayList = new ArrayList(list.size());
        int size = list.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(SplitInstallSessionState.a(list.get(i)));
        }
        this.a.b((i<T>) arrayList);
    }
}
