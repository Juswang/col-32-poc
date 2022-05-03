package com.google.android.play.core.tasks;

/* loaded from: classes2.dex */
public final class i<ResultT> {
    private final m<ResultT> a = new m<>();

    public final Task<ResultT> a() {
        return this.a;
    }

    public final void a(Exception exc) {
        this.a.a(exc);
    }

    public final void a(ResultT resultt) {
        this.a.a((m<ResultT>) resultt);
    }

    public final void b(Exception exc) {
        this.a.b(exc);
    }

    public final void b(ResultT resultt) {
        this.a.b((m<ResultT>) resultt);
    }
}
