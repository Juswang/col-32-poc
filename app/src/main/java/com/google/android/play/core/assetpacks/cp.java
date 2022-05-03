package com.google.android.play.core.assetpacks;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.cj;
import com.google.android.play.core.internal.h;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public final class cp {
    private static final af a = new af("ExtractorSessionStoreView");
    private final bb b;
    private final cj<w> c;
    private final bz d;
    private final cj<Executor> e;
    private final Map<Integer, cm> f = new HashMap();
    private final ReentrantLock g = new ReentrantLock();

    public cp(bb bbVar, cj<w> cjVar, bz bzVar, cj<Executor> cjVar2) {
        this.b = bbVar;
        this.c = cjVar;
        this.d = bzVar;
        this.e = cjVar2;
    }

    private final <T> T a(co<T> coVar) {
        try {
            a();
            return coVar.a();
        } finally {
            b();
        }
    }

    private final Map<String, cm> d(List<String> list) {
        return (Map) a(new co(this, list) { // from class: com.google.android.play.core.assetpacks.cf
            private final cp a;
            private final List b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = list;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                return this.a.c(this.b);
            }
        });
    }

    private final cm e(int i) {
        Map<Integer, cm> map = this.f;
        Integer valueOf = Integer.valueOf(i);
        cm cmVar = map.get(valueOf);
        if (cmVar != null) {
            return cmVar;
        }
        throw new bv(String.format("Could not find session %d while trying to get it", valueOf), i);
    }

    private static String e(Bundle bundle) {
        ArrayList<String> stringArrayList = bundle.getStringArrayList("pack_names");
        if (stringArrayList != null && !stringArrayList.isEmpty()) {
            return stringArrayList.get(0);
        }
        throw new bv("Session without pack received.");
    }

    private static <T> List<T> e(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public final Map<String, Integer> a(List<String> list) {
        return (Map) a(new co(this, list) { // from class: com.google.android.play.core.assetpacks.ci
            private final cp a;
            private final List b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = list;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                return this.a.b(this.b);
            }
        });
    }

    public final void a() {
        this.g.lock();
    }

    public final void a(int i) {
        a(new co(this, i) { // from class: com.google.android.play.core.assetpacks.ch
            private final cp a;
            private final int b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = i;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                this.a.c(this.b);
                return null;
            }
        });
    }

    public final void a(String str, int i, long j) {
        a(new co(this, str, i, j) { // from class: com.google.android.play.core.assetpacks.ce
            private final cp a;
            private final String b;
            private final int c;
            private final long d;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = str;
                this.c = i;
                this.d = j;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                this.a.b(this.b, this.c, this.d);
                return null;
            }
        });
    }

    public final boolean a(Bundle bundle) {
        return ((Boolean) a(new co(this, bundle) { // from class: com.google.android.play.core.assetpacks.cc
            private final cp a;
            private final Bundle b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = bundle;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                return this.a.d(this.b);
            }
        })).booleanValue();
    }

    public final /* synthetic */ Map b(List list) {
        int i;
        Map<String, cm> d = d(list);
        HashMap hashMap = new HashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            cm cmVar = d.get(str);
            if (cmVar == null) {
                i = 8;
            } else {
                if (db.a(cmVar.c.c)) {
                    try {
                        cmVar.c.c = 6;
                        this.e.a().execute(new Runnable(this, cmVar) { // from class: com.google.android.play.core.assetpacks.cj
                            private final cp a;
                            private final cm b;

                            /* JADX INFO: Access modifiers changed from: package-private */
                            {
                                this.a = this;
                                this.b = cmVar;
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                                this.a.a(this.b.a);
                            }
                        });
                        this.d.a(str);
                    } catch (bv e) {
                        a.c("Session %d with pack %s does not exist, no need to cancel.", Integer.valueOf(cmVar.a), str);
                    }
                }
                i = cmVar.c.c;
            }
            hashMap.put(str, Integer.valueOf(i));
        }
        return hashMap;
    }

    public final void b() {
        this.g.unlock();
    }

    public final /* synthetic */ void b(int i) {
        e(i).c.c = 5;
    }

    public final /* synthetic */ void b(String str, int i, long j) {
        cm cmVar = d(Arrays.asList(str)).get(str);
        if (cmVar == null || db.b(cmVar.c.c)) {
            a.b(String.format("Could not find pack %s while trying to complete it", str), new Object[0]);
        }
        this.b.f(str, i, j);
        cmVar.c.c = 4;
    }

    public final boolean b(Bundle bundle) {
        return ((Boolean) a(new co(this, bundle) { // from class: com.google.android.play.core.assetpacks.cd
            private final cp a;
            private final Bundle b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = bundle;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                return this.a.c(this.b);
            }
        })).booleanValue();
    }

    public final /* synthetic */ Boolean c(Bundle bundle) {
        boolean z;
        int i = bundle.getInt("session_id");
        if (i == 0) {
            return true;
        }
        Map<Integer, cm> map = this.f;
        Integer valueOf = Integer.valueOf(i);
        if (!map.containsKey(valueOf)) {
            return true;
        }
        cm cmVar = this.f.get(valueOf);
        if (cmVar.c.c == 6) {
            z = false;
        } else {
            z = !db.a(cmVar.c.c, bundle.getInt(h.a(NotificationCompat.CATEGORY_STATUS, e(bundle))));
        }
        return Boolean.valueOf(z);
    }

    public final Map<Integer, cm> c() {
        return this.f;
    }

    public final /* synthetic */ Map c(List list) {
        HashMap hashMap = new HashMap();
        for (cm cmVar : this.f.values()) {
            String str = cmVar.c.a;
            if (list.contains(str)) {
                cm cmVar2 = (cm) hashMap.get(str);
                if ((cmVar2 == null ? -1 : cmVar2.a) < cmVar.a) {
                    hashMap.put(str, cmVar);
                }
            }
        }
        return hashMap;
    }

    public final /* synthetic */ void c(int i) {
        cm e = e(i);
        if (db.b(e.c.c)) {
            bb bbVar = this.b;
            cl clVar = e.c;
            bbVar.f(clVar.a, e.b, clVar.b);
            cl clVar2 = e.c;
            int i2 = clVar2.c;
            if (i2 == 5 || i2 == 6) {
                this.b.d(clVar2.a);
                return;
            }
            return;
        }
        throw new bv(String.format("Could not safely delete session %d because it is not in a terminal state.", Integer.valueOf(i)), i);
    }

    public final /* synthetic */ Boolean d(Bundle bundle) {
        Iterator it;
        int i = bundle.getInt("session_id");
        boolean z = false;
        if (i == 0) {
            return false;
        }
        Map<Integer, cm> map = this.f;
        Integer valueOf = Integer.valueOf(i);
        if (map.containsKey(valueOf)) {
            cm e = e(i);
            int i2 = bundle.getInt(h.a(NotificationCompat.CATEGORY_STATUS, e.c.a));
            if (db.a(e.c.c, i2)) {
                a.a("Found stale update for session %s with status %d.", valueOf, Integer.valueOf(e.c.c));
                cl clVar = e.c;
                String str = clVar.a;
                int i3 = clVar.c;
                if (i3 == 4) {
                    this.c.a().a(i, str);
                } else if (i3 == 5) {
                    this.c.a().a(i);
                } else if (i3 == 6) {
                    this.c.a().a(Arrays.asList(str));
                }
            } else {
                e.c.c = i2;
                if (db.b(i2)) {
                    a(i);
                    this.d.a(e.c.a);
                } else {
                    List<cn> list = e.c.e;
                    int size = list.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        cn cnVar = list.get(i4);
                        ArrayList parcelableArrayList = bundle.getParcelableArrayList(h.a("chunk_intents", e.c.a, cnVar.a));
                        if (parcelableArrayList != null) {
                            for (int i5 = 0; i5 < parcelableArrayList.size(); i5++) {
                                if (!(parcelableArrayList.get(i5) == null || ((Intent) parcelableArrayList.get(i5)).getData() == null)) {
                                    cnVar.d.get(i5).a = true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            String e2 = e(bundle);
            long j = bundle.getLong(h.a("pack_version", e2));
            int i6 = bundle.getInt(h.a(NotificationCompat.CATEGORY_STATUS, e2));
            long j2 = bundle.getLong(h.a("total_bytes_to_download", e2));
            ArrayList<String> stringArrayList = bundle.getStringArrayList(h.a("slice_ids", e2));
            ArrayList arrayList = new ArrayList();
            Iterator it2 = e(stringArrayList).iterator();
            while (it2.hasNext()) {
                String str2 = (String) it2.next();
                ArrayList parcelableArrayList2 = bundle.getParcelableArrayList(h.a("chunk_intents", e2, str2));
                ArrayList arrayList2 = new ArrayList();
                for (Intent intent : e(parcelableArrayList2)) {
                    if (intent != null) {
                        it = it2;
                        z = true;
                    } else {
                        it = it2;
                    }
                    arrayList2.add(new ck(z));
                    it2 = it;
                    z = false;
                }
                it2 = it2;
                String string = bundle.getString(h.a("uncompressed_hash_sha256", e2, str2));
                long j3 = bundle.getLong(h.a("uncompressed_size", e2, str2));
                int i7 = bundle.getInt(h.a("patch_format", e2, str2), 0);
                arrayList.add(i7 != 0 ? new cn(str2, string, j3, arrayList2, 0, i7) : new cn(str2, string, j3, arrayList2, bundle.getInt(h.a("compression_format", e2, str2), 0), 0));
                z = false;
            }
            this.f.put(Integer.valueOf(i), new cm(i, bundle.getInt("app_version_code"), new cl(e2, j, i6, j2, arrayList)));
        }
        return true;
    }

    public final void d(int i) {
        a(new co(this, i) { // from class: com.google.android.play.core.assetpacks.cg
            private final cp a;
            private final int b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = i;
            }

            @Override // com.google.android.play.core.assetpacks.co
            public final Object a() {
                this.a.b(this.b);
                return null;
            }
        });
    }
}
