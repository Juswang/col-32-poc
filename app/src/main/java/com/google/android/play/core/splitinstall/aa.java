package com.google.android.play.core.splitinstall;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.play.core.internal.cn;
import java.io.File;

/* loaded from: classes2.dex */
public final class aa implements cn<File> {
    private final cn<Context> a;

    public aa(cn<Context> cnVar) {
        this.a = cnVar;
    }

    @Override // com.google.android.play.core.internal.cn
    @Nullable
    public final /* bridge */ /* synthetic */ File a() {
        return y.a(((z) this.a).a());
    }
}
