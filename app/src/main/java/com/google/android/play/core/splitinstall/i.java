package com.google.android.play.core.splitinstall;

import android.app.Activity;
import android.content.IntentSender;
import androidx.annotation.NonNull;
import com.google.android.play.core.common.IntentSenderForResultStarter;
import com.google.android.play.core.internal.cj;
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager;
import com.google.android.play.core.tasks.Task;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes2.dex */
public final class i implements SplitInstallManager {
    private final cj<w> a;
    private final cj<FakeSplitInstallManager> b;
    private final cj<File> c;

    public i(cj<w> cjVar, cj<FakeSplitInstallManager> cjVar2, cj<File> cjVar3) {
        this.a = cjVar;
        this.b = cjVar2;
        this.c = cjVar3;
    }

    private final SplitInstallManager a() {
        return (SplitInstallManager) (this.c.a() == null ? this.a : this.b).a();
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<Void> cancelInstall(int i) {
        return a().cancelInstall(i);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<Void> deferredInstall(List<String> list) {
        return a().deferredInstall(list);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<Void> deferredLanguageInstall(List<Locale> list) {
        return a().deferredLanguageInstall(list);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<Void> deferredLanguageUninstall(List<Locale> list) {
        return a().deferredLanguageUninstall(list);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<Void> deferredUninstall(List<String> list) {
        return a().deferredUninstall(list);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Set<String> getInstalledLanguages() {
        return a().getInstalledLanguages();
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Set<String> getInstalledModules() {
        return a().getInstalledModules();
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<SplitInstallSessionState> getSessionState(int i) {
        return a().getSessionState(i);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    @NonNull
    public final Task<List<SplitInstallSessionState>> getSessionStates() {
        return a().getSessionStates();
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final void registerListener(@NonNull SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        a().registerListener(splitInstallStateUpdatedListener);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final boolean startConfirmationDialogForResult(@NonNull SplitInstallSessionState splitInstallSessionState, @NonNull Activity activity, int i) throws IntentSender.SendIntentException {
        return a().startConfirmationDialogForResult(splitInstallSessionState, activity, i);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final boolean startConfirmationDialogForResult(@NonNull SplitInstallSessionState splitInstallSessionState, @NonNull IntentSenderForResultStarter intentSenderForResultStarter, int i) throws IntentSender.SendIntentException {
        return a().startConfirmationDialogForResult(splitInstallSessionState, intentSenderForResultStarter, i);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final Task<Integer> startInstall(@NonNull SplitInstallRequest splitInstallRequest) {
        return a().startInstall(splitInstallRequest);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public final void unregisterListener(@NonNull SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        a().unregisterListener(splitInstallStateUpdatedListener);
    }
}
