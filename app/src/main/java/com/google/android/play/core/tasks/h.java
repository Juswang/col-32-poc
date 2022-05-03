package com.google.android.play.core.tasks;

import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: classes2.dex */
public final class h<ResultT> {
    private final Object a = new Object();
    private Queue<g<ResultT>> b;
    private boolean c;

    public final void a(Task<ResultT> task) {
        g<ResultT> poll;
        synchronized (this.a) {
            if (this.b != null && !this.c) {
                this.c = true;
                while (true) {
                    synchronized (this.a) {
                        poll = this.b.poll();
                        if (poll == null) {
                            this.c = false;
                            return;
                        }
                    }
                    poll.a(task);
                }
            }
        }
    }

    public final void a(g<ResultT> gVar) {
        synchronized (this.a) {
            if (this.b == null) {
                this.b = new ArrayDeque();
            }
            this.b.add(gVar);
        }
    }
}
