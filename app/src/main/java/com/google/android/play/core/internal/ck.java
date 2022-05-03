package com.google.android.play.core.internal;

/* loaded from: classes2.dex */
public final class ck<T> implements cn<T> {
    private cn<T> a;

    public static <T> void a(cn<T> cnVar, cn<T> cnVar2) {
        bq.a(cnVar2);
        ck ckVar = (ck) cnVar;
        if (ckVar.a == null) {
            ckVar.a = cnVar2;
            return;
        }
        throw new IllegalStateException();
    }

    @Override // com.google.android.play.core.internal.cn
    public final T a() {
        cn<T> cnVar = this.a;
        if (cnVar != null) {
            return cnVar.a();
        }
        throw new IllegalStateException();
    }
}
