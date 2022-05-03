package com.google.android.play.core.tasks;

/* loaded from: classes2.dex */
final class c implements Runnable {
    final /* synthetic */ Task a;
    final /* synthetic */ d b;

    public c(d dVar, Task task) {
        this.b = dVar;
        this.a = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnFailureListener onFailureListener;
        OnFailureListener onFailureListener2;
        obj = this.b.b;
        synchronized (obj) {
            onFailureListener = this.b.c;
            if (onFailureListener != null) {
                onFailureListener2 = this.b.c;
                onFailureListener2.onFailure(this.a.getException());
            }
        }
    }
}
