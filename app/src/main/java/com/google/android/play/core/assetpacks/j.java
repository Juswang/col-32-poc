package com.google.android.play.core.assetpacks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;
import com.google.android.play.core.common.PlayCoreDialogWrapperActivity;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.cj;
import com.google.android.play.core.internal.h;
import com.google.android.play.core.listener.StateUpdatedListener;
import com.google.android.play.core.splitinstall.p;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;
import com.google.android.play.core.tasks.i;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class j implements AssetPackManager {
    private static final af a = new af("AssetPackManager");
    private final bb b;
    private final cj<w> c;
    private final aw d;
    private final p e;
    private final cp f;
    private final bz g;
    private final bn h;
    private final cj<Executor> i;
    private final Handler j = new Handler(Looper.getMainLooper());
    private boolean k;

    public j(bb bbVar, cj<w> cjVar, aw awVar, p pVar, cp cpVar, bz bzVar, bn bnVar, cj<Executor> cjVar2) {
        this.b = bbVar;
        this.c = cjVar;
        this.d = awVar;
        this.e = pVar;
        this.f = cpVar;
        this.g = bzVar;
        this.h = bnVar;
        this.i = cjVar2;
    }

    private final void c() {
        this.i.a().execute(new Runnable(this) { // from class: com.google.android.play.core.assetpacks.e
            private final j a;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.b();
            }
        });
    }

    private final void d() {
        this.i.a().execute(new f(this));
        this.k = true;
    }

    @AssetPackStatus
    public final int a(@AssetPackStatus int i, String str) {
        if (!this.b.a(str) && i == 4) {
            return 8;
        }
        if (!this.b.a(str) || i == 4) {
            return i;
        }
        return 4;
    }

    public final /* synthetic */ void a() {
        this.b.d();
        this.b.c();
        this.b.e();
    }

    public final /* synthetic */ void a(String str, i iVar) {
        if (this.b.d(str)) {
            iVar.a((i) null);
            this.c.a().a(str);
            return;
        }
        iVar.a((Exception) new IOException(String.format("Failed to remove pack %s.", str)));
    }

    public final void a(boolean z) {
        boolean b = this.d.b();
        this.d.a(z);
        if (z && !b) {
            c();
        }
    }

    public final /* synthetic */ void b() {
        bb bbVar = this.b;
        bbVar.getClass();
        this.c.a().a(this.b.b()).addOnSuccessListener(this.i.a(), g.a(bbVar)).addOnFailureListener(this.i.a(), h.a);
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final AssetPackStates cancel(List<String> list) {
        Map<String, Integer> a2 = this.f.a(list);
        HashMap hashMap = new HashMap();
        for (String str : list) {
            Integer num = a2.get(str);
            hashMap.put(str, AssetPackState.a(str, num == null ? 0 : num.intValue(), 0, 0L, 0L, 0.0d));
        }
        this.c.a().a(list);
        return AssetPackStates.a(0L, hashMap);
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final void clearListeners() {
        this.d.a();
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final Task<AssetPackStates> fetch(List<String> list) {
        Map<String, Long> b = this.b.b();
        ArrayList arrayList = new ArrayList(list);
        arrayList.removeAll(b.keySet());
        ArrayList arrayList2 = new ArrayList(list);
        arrayList2.removeAll(arrayList);
        if (!arrayList.isEmpty()) {
            return this.c.a().a(arrayList2, arrayList, b);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", 0);
        bundle.putInt("error_code", 0);
        for (String str : list) {
            bundle.putInt(h.a(NotificationCompat.CATEGORY_STATUS, str), 4);
            bundle.putInt(h.a("error_code", str), 0);
            bundle.putLong(h.a("total_bytes_to_download", str), 0L);
            bundle.putLong(h.a("bytes_downloaded", str), 0L);
        }
        bundle.putStringArrayList("pack_names", new ArrayList<>(list));
        bundle.putLong("total_bytes_to_download", 0L);
        bundle.putLong("bytes_downloaded", 0L);
        return Tasks.a(AssetPackStates.a(bundle, this.g));
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    @Nullable
    public final AssetLocation getAssetLocation(String str, String str2) {
        AssetPackLocation b;
        if (!this.k) {
            this.i.a().execute(new f(this));
            this.k = true;
        }
        if (this.b.a(str)) {
            try {
                b = this.b.b(str);
            } catch (IOException e) {
            }
        } else {
            if (this.e.a().contains(str)) {
                b = AssetPackLocation.a();
            }
            b = null;
        }
        if (b == null) {
            return null;
        }
        if (b.packStorageMethod() == 1) {
            return this.b.a(str, str2);
        }
        if (b.packStorageMethod() == 0) {
            return this.b.a(str, str2, b);
        }
        a.a("The asset %s is not present in Asset Pack %s", str2, str);
        return null;
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    @Nullable
    public final AssetPackLocation getPackLocation(String str) {
        if (!this.k) {
            d();
        }
        if (this.b.a(str)) {
            try {
                return this.b.b(str);
            } catch (IOException e) {
                return null;
            }
        } else if (this.e.a().contains(str)) {
            return AssetPackLocation.a();
        } else {
            return null;
        }
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final Map<String, AssetPackLocation> getPackLocations() {
        Map<String, AssetPackLocation> a2 = this.b.a();
        HashMap hashMap = new HashMap();
        for (String str : this.e.a()) {
            hashMap.put(str, AssetPackLocation.a());
        }
        a2.putAll(hashMap);
        return a2;
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final Task<AssetPackStates> getPackStates(List<String> list) {
        return this.c.a().a(list, new az(this) { // from class: com.google.android.play.core.assetpacks.c
            private final j a;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
            }

            @Override // com.google.android.play.core.assetpacks.az
            public final int a(int i, String str) {
                return this.a.a(i, str);
            }
        }, this.b.b());
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final synchronized void registerListener(AssetPackStateUpdateListener assetPackStateUpdateListener) {
        boolean b = this.d.b();
        this.d.a((StateUpdatedListener) assetPackStateUpdateListener);
        if (!b) {
            c();
        }
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final Task<Void> removePack(String str) {
        i iVar = new i();
        this.i.a().execute(new Runnable(this, str, iVar) { // from class: com.google.android.play.core.assetpacks.d
            private final j a;
            private final String b;
            private final i c;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = str;
                this.c = iVar;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b, this.c);
            }
        });
        return iVar.a();
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final Task<Integer> showCellularDataConfirmation(Activity activity) {
        if (this.h.a() == null) {
            return Tasks.a((Exception) new AssetPackException(-12));
        }
        Intent intent = new Intent(activity, PlayCoreDialogWrapperActivity.class);
        intent.putExtra("confirmation_intent", this.h.a());
        i iVar = new i();
        intent.putExtra("result_receiver", new i(this, this.j, iVar));
        activity.startActivity(intent);
        return iVar.a();
    }

    @Override // com.google.android.play.core.assetpacks.AssetPackManager
    public final void unregisterListener(AssetPackStateUpdateListener assetPackStateUpdateListener) {
        this.d.b(assetPackStateUpdateListener);
    }
}
