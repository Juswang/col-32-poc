package com.google.android.play.core.install;

/* loaded from: classes2.dex */
final class NativeInstallStateUpdateListener implements InstallStateUpdatedListener {
    NativeInstallStateUpdateListener() {
    }

    public native void onStateUpdate(InstallState installState);
}
