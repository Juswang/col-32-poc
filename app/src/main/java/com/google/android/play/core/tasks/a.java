package com.google.android.play.core.tasks;

/* loaded from: classes2.dex */
final class a implements Runnable {
    final /* synthetic */ Task a;
    final /* synthetic */ b b;

    public a(b bVar, Task task) {
        this.b = bVar;
        this.a = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnCompleteListener onCompleteListener;
        OnCompleteListener onCompleteListener2;
        obj = this.b.b;
        synchronized (obj) {
            onCompleteListener = this.b.c;
            if (onCompleteListener != null) {
                onCompleteListener2 = this.b.c;
                onCompleteListener2.onComplete(this.a);
            }
        }
    }
}
