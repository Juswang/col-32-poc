package com.google.android.play.core.splitinstall;

import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode;
import com.google.android.play.core.splitinstall.model.a;
import com.google.android.play.core.tasks.j;

/* loaded from: classes2.dex */
public class SplitInstallException extends j {
    @SplitInstallErrorCode
    private final int a;

    public SplitInstallException(@SplitInstallErrorCode int i) {
        super(String.format("Split Install Error(%d): %s", Integer.valueOf(i), a.a(i)));
        if (i != 0) {
            this.a = i;
            return;
        }
        throw new IllegalArgumentException("errorCode should not be 0.");
    }

    @Override // com.google.android.play.core.tasks.j
    @SplitInstallErrorCode
    public int getErrorCode() {
        return this.a;
    }
}
