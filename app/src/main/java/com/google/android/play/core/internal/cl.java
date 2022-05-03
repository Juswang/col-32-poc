package com.google.android.play.core.internal;

/* loaded from: classes2.dex */
public final class cl<T> implements cn, cj {
    private static final Object a = new Object();
    private volatile cn<T> b;
    private volatile Object c = a;

    private cl(cn<T> cnVar) {
        this.b = cnVar;
    }

    public static <P extends cn<T>, T> cn<T> a(P p) {
        bq.a(p);
        return p instanceof cl ? p : new cl(p);
    }

    public static <P extends cn<T>, T> cj<T> b(P p) {
        if (p instanceof cj) {
            return (cj) p;
        }
        bq.a(p);
        return new cl(p);
    }

    @Override // com.google.android.play.core.internal.cn
    public final T a() {
        T t = (T) this.c;
        if (t == a) {
            synchronized (this) {
                t = this.c;
                if (t == a) {
                    t = this.b.a();
                    Object obj = this.c;
                    if (!(obj == a || (obj instanceof cm) || obj == t)) {
                        String valueOf = String.valueOf(obj);
                        String valueOf2 = String.valueOf(t);
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 118 + String.valueOf(valueOf2).length());
                        sb.append("Scoped provider was invoked recursively returning different results: ");
                        sb.append(valueOf);
                        sb.append(" & ");
                        sb.append(valueOf2);
                        sb.append(". This is likely due to a circular dependency.");
                        throw new IllegalStateException(sb.toString());
                    }
                    this.c = t;
                    this.b = null;
                }
            }
        }
        return t;
    }
}
