package com.google.android.play.core.internal;

/* loaded from: classes2.dex */
final class cf extends cc {
    private final ce a = new ce();

    @Override // com.google.android.play.core.internal.cc
    public final void a(Throwable th, Throwable th2) {
        if (th2 == th) {
            throw new IllegalArgumentException("Self suppression is not allowed.", th2);
        } else if (th2 != null) {
            this.a.a(th).add(th2);
        } else {
            throw new NullPointerException("The suppressed exception cannot be null.");
        }
    }
}
