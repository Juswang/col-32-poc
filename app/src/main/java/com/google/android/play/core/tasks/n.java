package com.google.android.play.core.tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class n implements OnSuccessListener, OnFailureListener {
    private final CountDownLatch a = new CountDownLatch(1);

    private n() {
    }

    public /* synthetic */ n(byte[] bArr) {
    }

    public final void a() throws InterruptedException {
        this.a.await();
    }

    public final boolean a(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.a.await(j, timeUnit);
    }

    @Override // com.google.android.play.core.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        this.a.countDown();
    }

    @Override // com.google.android.play.core.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        this.a.countDown();
    }
}
