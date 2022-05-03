package com.google.android.play.core.tasks;

/* loaded from: classes2.dex */
final class e implements Runnable {
    final /* synthetic */ Task a;
    final /* synthetic */ f b;

    public e(f fVar, Task task) {
        this.b = fVar;
        this.a = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnSuccessListener onSuccessListener;
        OnSuccessListener onSuccessListener2;
        obj = this.b.b;
        synchronized (obj) {
            onSuccessListener = this.b.c;
            if (onSuccessListener != null) {
                onSuccessListener2 = this.b.c;
                onSuccessListener2.onSuccess(this.a.getResult());
            }
        }
    }
}
