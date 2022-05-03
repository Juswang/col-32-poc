package com.google.android.play.core.splitinstall.testing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.internal.ae;
import com.google.android.play.core.internal.av;
import com.google.android.play.core.internal.bx;
import com.google.android.play.core.splitinstall.SplitInstallException;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.e;
import com.google.android.play.core.splitinstall.h;
import com.google.android.play.core.splitinstall.l;
import com.google.android.play.core.splitinstall.p;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public class FakeSplitInstallManager implements SplitInstallManager {
    public static final /* synthetic */ int a = 0;
    private static final long c = TimeUnit.SECONDS.toMillis(1);
    private final Handler b;
    private final Context d;
    private final p e;
    private final bx f;
    private final ae<SplitInstallSessionState> g;
    private final Executor h;
    private final e i;
    private final File j;
    private final AtomicReference<SplitInstallSessionState> k;
    private final Set<String> l;
    private final Set<String> m;
    private final AtomicBoolean n;
    private final a o;

    @Deprecated
    public FakeSplitInstallManager(Context context, File file) {
        this(context, file, new p(context, context.getPackageName()));
    }

    public FakeSplitInstallManager(Context context, @Nullable File file, p pVar) {
        Executor a2 = com.google.android.play.core.splitcompat.p.a();
        bx bxVar = new bx(context);
        a aVar = a.a;
        this.b = new Handler(Looper.getMainLooper());
        this.k = new AtomicReference<>();
        this.l = Collections.synchronizedSet(new HashSet());
        this.m = Collections.synchronizedSet(new HashSet());
        this.n = new AtomicBoolean(false);
        this.d = context;
        this.j = file;
        this.e = pVar;
        this.h = a2;
        this.f = bxVar;
        this.o = aVar;
        this.g = new ae<>();
        this.i = l.a;
    }

    public static final /* synthetic */ SplitInstallSessionState a(int i, SplitInstallSessionState splitInstallSessionState) {
        int status;
        if (splitInstallSessionState != null && i == splitInstallSessionState.sessionId() && ((status = splitInstallSessionState.status()) == 1 || status == 2 || status == 8 || status == 9 || status == 7)) {
            return SplitInstallSessionState.create(i, 7, splitInstallSessionState.errorCode(), splitInstallSessionState.bytesDownloaded(), splitInstallSessionState.totalBytesToDownload(), splitInstallSessionState.moduleNames(), splitInstallSessionState.languages());
        }
        throw new SplitInstallException(-3);
    }

    @Nullable
    private final synchronized SplitInstallSessionState a(j jVar) {
        SplitInstallSessionState c2 = c();
        SplitInstallSessionState a2 = jVar.a(c2);
        if (this.k.compareAndSet(c2, a2)) {
            return a2;
        }
        return null;
    }

    public static final /* synthetic */ SplitInstallSessionState a(Integer num, int i, int i2, Long l, Long l2, List list, List list2, SplitInstallSessionState splitInstallSessionState) {
        SplitInstallSessionState create = splitInstallSessionState == null ? SplitInstallSessionState.create(0, 0, 0, 0L, 0L, new ArrayList(), new ArrayList()) : splitInstallSessionState;
        return SplitInstallSessionState.create(num == null ? create.sessionId() : num.intValue(), i, i2, l == null ? create.bytesDownloaded() : l.longValue(), l2 == null ? create.totalBytesToDownload() : l2.longValue(), list == null ? create.moduleNames() : list, list2 == null ? create.languages() : list2);
    }

    private static String a(String str) {
        return str.split("\\.config\\.", 2)[0];
    }

    public final void a(List<Intent> list, List<String> list2, List<String> list3, long j, boolean z) {
        this.i.a().a(list, new i(this, list2, list3, j, z, list));
    }

    public final boolean a(int i) {
        return a(6, i, null, null, null, null, null);
    }

    public final boolean a(int i, int i2, @Nullable Long l, @Nullable Long l2, @Nullable List<String> list, @Nullable Integer num, @Nullable List<String> list2) {
        SplitInstallSessionState a2 = a(new j(num, i, i2, l, l2, list, list2) { // from class: com.google.android.play.core.splitinstall.testing.b
            private final Integer a;
            private final int b;
            private final int c;
            private final Long d;
            private final Long e;
            private final List f;
            private final List g;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = num;
                this.b = i;
                this.c = i2;
                this.d = l;
                this.e = l2;
                this.f = list;
                this.g = list2;
            }

            @Override // com.google.android.play.core.splitinstall.testing.j
            public final SplitInstallSessionState a(SplitInstallSessionState splitInstallSessionState) {
                return FakeSplitInstallManager.a(this.a, this.b, this.c, this.d, this.e, this.f, this.g, splitInstallSessionState);
            }
        });
        if (a2 == null) {
            return false;
        }
        b(a2);
        return true;
    }

    static final /* synthetic */ void b() {
        SystemClock.sleep(c);
    }

    private final void b(SplitInstallSessionState splitInstallSessionState) {
        this.b.post(new Runnable(this, splitInstallSessionState) { // from class: com.google.android.play.core.splitinstall.testing.f
            private final FakeSplitInstallManager a;
            private final SplitInstallSessionState b;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = splitInstallSessionState;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b);
            }
        });
    }

    @Nullable
    private final SplitInstallSessionState c() {
        return this.k.get();
    }

    private final h d() {
        h c2 = this.e.c();
        if (c2 != null) {
            return c2;
        }
        throw new IllegalStateException("Language information could not be found. Make sure you are using the target application context, not the tests context, and the app is built as a bundle.");
    }

    public final File a() {
        return this.j;
    }

    public final /* synthetic */ void a(long j, List list, List list2, List list3) {
        long j2 = j / 3;
        long j3 = 0;
        for (int i = 0; i < 3; i++) {
            j3 = Math.min(j, j3 + j2);
            a(2, 0, Long.valueOf(j3), Long.valueOf(j), null, null, null);
            b();
            SplitInstallSessionState c2 = c();
            if (c2.status() == 9 || c2.status() == 7 || c2.status() == 6) {
                return;
            }
        }
        this.h.execute(new Runnable(this, list, list2, list3, j) { // from class: com.google.android.play.core.splitinstall.testing.h
            private final FakeSplitInstallManager a;
            private final List b;
            private final List c;
            private final List d;
            private final long e;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.a = this;
                this.b = list;
                this.c = list2;
                this.d = list3;
                this.e = j;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.a.a(this.b, this.c, this.d, this.e);
            }
        });
    }

    public final /* synthetic */ void a(SplitInstallSessionState splitInstallSessionState) {
        this.g.a((ae<SplitInstallSessionState>) splitInstallSessionState);
    }

    public final /* synthetic */ void a(List list, List list2) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            File file = (File) list.get(i);
            String a2 = av.a(file);
            Uri fromFile = Uri.fromFile(file);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(fromFile, this.d.getContentResolver().getType(fromFile));
            intent.addFlags(1);
            intent.putExtra("module_name", a(a2));
            intent.putExtra("split_id", a2);
            arrayList.add(intent);
            arrayList2.add(a(av.a(file)));
        }
        SplitInstallSessionState c2 = c();
        if (c2 != null) {
            this.h.execute(new Runnable(this, c2.totalBytesToDownload(), arrayList, arrayList2, list2) { // from class: com.google.android.play.core.splitinstall.testing.g
                private final FakeSplitInstallManager a;
                private final long b;
                private final List c;
                private final List d;
                private final List e;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.a = this;
                    this.b = j;
                    this.c = arrayList;
                    this.d = arrayList2;
                    this.e = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.a.a(this.b, this.c, this.d, this.e);
                }
            });
        }
    }

    public final /* synthetic */ void a(List list, List list2, List list3, long j) {
        if (this.n.get()) {
            a(-6);
        } else {
            a((List<Intent>) list, (List<String>) list2, (List<String>) list3, j, false);
        }
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Void> cancelInstall(int i) {
        try {
            SplitInstallSessionState a2 = a(new j(i) { // from class: com.google.android.play.core.splitinstall.testing.e
                private final int a;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.a = i;
                }

                @Override // com.google.android.play.core.splitinstall.testing.j
                public final SplitInstallSessionState a(SplitInstallSessionState splitInstallSessionState) {
                    return FakeSplitInstallManager.a(this.a, splitInstallSessionState);
                }
            });
            if (a2 != null) {
                b(a2);
            }
            return Tasks.a((Object) null);
        } catch (SplitInstallException e) {
            return Tasks.a((Exception) e);
        }
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Void> deferredInstall(List<String> list) {
        return Tasks.a((Exception) new SplitInstallException(-5));
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Void> deferredLanguageInstall(List<Locale> list) {
        return Tasks.a((Exception) new SplitInstallException(-5));
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Void> deferredLanguageUninstall(List<Locale> list) {
        return Tasks.a((Exception) new SplitInstallException(-5));
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Void> deferredUninstall(List<String> list) {
        return Tasks.a((Exception) new SplitInstallException(-5));
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Set<String> getInstalledLanguages() {
        return new HashSet(this.m);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Set<String> getInstalledModules() {
        return new HashSet(this.l);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<SplitInstallSessionState> getSessionState(int i) {
        SplitInstallSessionState c2 = c();
        return (c2 == null || c2.sessionId() != i) ? Tasks.a((Exception) new SplitInstallException(-4)) : Tasks.a(c2);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<List<SplitInstallSessionState>> getSessionStates() {
        SplitInstallSessionState c2 = c();
        return Tasks.a(c2 != null ? Collections.singletonList(c2) : Collections.emptyList());
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final void registerListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        this.g.a(splitInstallStateUpdatedListener);
    }

    public void setShouldNetworkError(boolean z) {
        this.n.set(z);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final boolean startConfirmationDialogForResult(SplitInstallSessionState splitInstallSessionState, Activity activity, int i) throws IntentSender.SendIntentException {
        return false;
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final boolean startConfirmationDialogForResult(SplitInstallSessionState splitInstallSessionState, IntentSenderForResultStarter intentSenderForResultStarter, int i) throws IntentSender.SendIntentException {
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0133, code lost:
        if (r0.contains(r6) == false) goto L42;
     */
    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.play.core.tasks.Task<java.lang.Integer> startInstall(com.google.android.play.core.splitinstall.SplitInstallRequest r22) {
        /*
            Method dump skipped, instructions count: 566
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager.startInstall(com.google.android.play.core.splitinstall.SplitInstallRequest):com.google.android.play.core.tasks.Task");
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final void unregisterListener(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        this.g.b(splitInstallStateUpdatedListener);
    }
}
