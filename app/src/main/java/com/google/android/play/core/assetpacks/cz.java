package com.google.android.play.core.assetpacks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import androidx.core.app.NotificationCompat;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;
import com.google.android.play.core.common.LocalTestingException;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.av;
import com.google.android.play.core.internal.cj;
import com.google.android.play.core.internal.h;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;
import com.google.android.play.core.tasks.i;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public final class cz implements w {
    private static final af a = new af("FakeAssetPackService");
    private static final AtomicInteger h = new AtomicInteger(1);
    private final String b;
    private final aw c;
    private final bz d;
    private final Context e;
    private final dl f;
    private final cj<Executor> g;
    private final Handler i = new Handler(Looper.getMainLooper());

    public cz(File file, aw awVar, bz bzVar, Context context, dl dlVar, cj<Executor> cjVar) {
        this.b = file.getAbsolutePath();
        this.c = awVar;
        this.d = bzVar;
        this.e = context;
        this.f = dlVar;
        this.g = cjVar;
    }

    static long a(@AssetPackStatus int i, long j) {
        if (i == 2) {
            return j / 2;
        }
        if (i == 3 || i == 4) {
            return j;
        }
        return 0L;
    }

    private final AssetPackState a(String str, @AssetPackStatus int i) throws LocalTestingException {
        long j = 0;
        for (File file : b(str)) {
            j += file.length();
        }
        return AssetPackState.a(str, i, 0, a(i, j), j, this.d.b(str));
    }

    private static String a(File file) throws LocalTestingException {
        try {
            return db.a(Arrays.asList(file));
        } catch (IOException e) {
            throw new LocalTestingException(String.format("Could not digest file: %s.", file), e);
        } catch (NoSuchAlgorithmException e2) {
            throw new LocalTestingException("SHA256 algorithm not supported.", e2);
        }
    }

    private final void a(int i, String str, @AssetPackStatus int i2) throws LocalTestingException {
        Bundle bundle = new Bundle();
        bundle.putInt("app_version_code", this.f.a());
        bundle.putInt("session_id", i);
        File[] b = b(str);
        ArrayList<String> arrayList = new ArrayList<>();
        long j = 0;
        for (File file : b) {
            j += file.length();
            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
            arrayList2.add(i2 == 3 ? new Intent().setData(Uri.EMPTY) : null);
            String a2 = av.a(file);
            bundle.putParcelableArrayList(h.a("chunk_intents", str, a2), arrayList2);
            bundle.putString(h.a("uncompressed_hash_sha256", str, a2), a(file));
            bundle.putLong(h.a("uncompressed_size", str, a2), file.length());
            arrayList.add(a2);
        }
        bundle.putStringArrayList(h.a("slice_ids", str), arrayList);
        bundle.putLong(h.a("pack_version", str), this.f.a());
        bundle.putInt(h.a(NotificationCompat.CATEGORY_STATUS, str), i2);
        bundle.putInt(h.a("error_code", str), 0);
        bundle.putLong(h.a("bytes_downloaded", str), a(i2, j));
        bundle.putLong(h.a("total_bytes_to_download", str), j);
        bundle.putStringArrayList("pack_names", new ArrayList<>(Arrays.asList(str)));
        bundle.putLong("bytes_downloaded", a(i2, j));
        bundle.putLong("total_bytes_to_download", j);
        this.i.post(new Runnable(this, new Intent("com.google.android.play.core.assetpacks.receiver.ACTION_SESSION_UPDATE").putExtra("com.google.android.play.core.assetpacks.receiver.EXTRA_SESSION_STATE", bundle)) { // from class: com.google.android.play.core.assetpacks.cy
            private final cz a;
            private final Intent b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = putExtra;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b);
            }
        });
    }

    private final File[] b(String str) throws LocalTestingException {
        File file = new File(this.b);
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles(new FilenameFilter(str) { // from class: com.google.android.play.core.assetpacks.cx
                private final String a;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.a = str;
                }

                @Override // java.io.FilenameFilter
                public final boolean accept(File file2, String str2) {
                    return str2.startsWith(String.valueOf(this.a).concat("-")) && str2.endsWith(".apk");
                }
            });
            if (listFiles != null) {
                if (listFiles.length != 0) {
                    for (File file2 : listFiles) {
                        if (av.a(file2).equals(str)) {
                            return listFiles;
                        }
                    }
                    throw new LocalTestingException(String.format("No master slice available for pack '%s'.", str));
                }
                throw new LocalTestingException(String.format("No APKs available for pack '%s'.", str));
            }
            throw new LocalTestingException(String.format("Failed fetching APKs for pack '%s'.", str));
        }
        throw new LocalTestingException(String.format("Local testing directory '%s' not found.", file));
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final Task<AssetPackStates> a(List<String> list, az azVar, Map<String, Long> map) {
        a.c("getPackStates(%s)", list);
        i iVar = new i();
        this.g.a().execute(new Runnable(this, list, azVar, iVar) { // from class: com.google.android.play.core.assetpacks.cv
            private final cz a;
            private final List b;
            private final az c;
            private final i d;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = list;
                this.c = azVar;
                this.d = iVar;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b, this.c, this.d);
            }
        });
        return iVar.a();
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final Task<AssetPackStates> a(List<String> list, List<String> list2, Map<String, Long> map) {
        a.c("startDownload(%s)", list2);
        i iVar = new i();
        this.g.a().execute(new Runnable(this, list2, iVar, list) { // from class: com.google.android.play.core.assetpacks.cu
            private final cz a;
            private final List b;
            private final i c;
            private final List d;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = list2;
                this.c = iVar;
                this.d = list;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b, this.c, this.d);
            }
        });
        return iVar.a();
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final Task<List<String>> a(Map<String, Long> map) {
        a.c("syncPacks()", new Object[0]);
        return Tasks.a(new ArrayList());
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a() {
        a.c("keepAlive", new Object[0]);
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a(int i) {
        a.c("notifySessionFailed", new Object[0]);
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a(int i, String str) {
        a.c("notifyModuleCompleted", new Object[0]);
        this.g.a().execute(new Runnable(this, i, str) { // from class: com.google.android.play.core.assetpacks.cw
            private final cz a;
            private final int b;
            private final String c;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = i;
                this.c = str;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.b(this.b, this.c);
            }
        });
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a(int i, String str, String str2, int i2) {
        a.c("notifyChunkTransferred", new Object[0]);
    }

    public final /* synthetic */ void a(Intent intent) {
        this.c.a(this.e, intent);
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a(String str) {
        a.c("removePack(%s)", str);
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final void a(List<String> list) {
        a.c("cancelDownload(%s)", list);
    }

    public final /* synthetic */ void a(List list, az azVar, i iVar) {
        HashMap hashMap = new HashMap();
        Iterator it = list.iterator();
        long j = 0;
        while (it.hasNext()) {
            String str = (String) it.next();
            try {
                AssetPackState a2 = a(str, azVar.a(8, str));
                j += a2.totalBytesToDownload();
                hashMap.put(str, a2);
            } catch (LocalTestingException e) {
                iVar.a((Exception) e);
                return;
            }
        }
        iVar.a((i) AssetPackStates.a(j, hashMap));
    }

    public final /* synthetic */ void a(List list, i iVar, List list2) {
        HashMap hashMap = new HashMap();
        int size = list.size();
        long j = 0;
        for (int i = 0; i < size; i++) {
            String str = (String) list.get(i);
            try {
                AssetPackState a2 = a(str, 1);
                j += a2.totalBytesToDownload();
                hashMap.put(str, a2);
            } catch (LocalTestingException e) {
                iVar.a((Exception) e);
                return;
            }
        }
        int size2 = list.size();
        for (int i2 = 0; i2 < size2; i2++) {
            String str2 = (String) list.get(i2);
            try {
                int andIncrement = h.getAndIncrement();
                a(andIncrement, str2, 1);
                a(andIncrement, str2, 2);
                a(andIncrement, str2, 3);
            } catch (LocalTestingException e2) {
                iVar.a((Exception) e2);
                return;
            }
        }
        int size3 = list2.size();
        for (int i3 = 0; i3 < size3; i3++) {
            String str3 = (String) list2.get(i3);
            hashMap.put(str3, AssetPackState.a(str3, 4, 0, 0L, 0L, 0.0d));
        }
        iVar.a((i) AssetPackStates.a(j, hashMap));
    }

    @Override // com.google.android.play.core.assetpacks.w
    public final Task<ParcelFileDescriptor> b(int i, String str, String str2, int i2) {
        File[] b;
        int i3;
        a.c("getChunkFileDescriptor(session=%d, %s, %s, %d)", Integer.valueOf(i), str, str2, Integer.valueOf(i2));
        i iVar = new i();
        try {
        } catch (LocalTestingException e) {
            a.d("getChunkFileDescriptor failed", e);
            iVar.a((Exception) e);
        } catch (FileNotFoundException e2) {
            a.d("getChunkFileDescriptor failed", e2);
            iVar.a((Exception) new LocalTestingException("Asset Slice file not found.", e2));
        }
        for (File file : b(str)) {
            if (av.a(file).equals(str2)) {
                iVar.a((i) ParcelFileDescriptor.open(file, 268435456));
                return iVar.a();
            }
        }
        throw new LocalTestingException(String.format("Local testing slice for '%s' not found.", str2));
    }

    public final /* synthetic */ void b(int i, String str) {
        try {
            a(i, str, 4);
        } catch (LocalTestingException e) {
            a.d("notifyModuleCompleted failed", e);
        }
    }
}
