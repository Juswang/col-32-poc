package com.google.android.play.core.assetpacks;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public final class bz {
    private final Map<String, Double> a = new HashMap();

    public final synchronized double a(String str, cr crVar) {
        double d;
        d = (((bs) crVar).e + 1.0d) / ((bs) crVar).f;
        this.a.put(str, Double.valueOf(d));
        return d;
    }

    public final synchronized void a(String str) {
        this.a.put(str, Double.valueOf(0.0d));
    }

    public final synchronized double b(String str) {
        Double d = this.a.get(str);
        if (d == null) {
            return 0.0d;
        }
        return d.doubleValue();
    }
}
