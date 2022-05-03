package com.google.android.play.core.common;

import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public final class a {
    private final Map<String, Object> a = new HashMap();

    public final synchronized void a(Bundle bundle) {
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj != null && this.a.get(str) == null) {
                this.a.put(str, obj);
            }
        }
    }

    public final synchronized boolean a() {
        Object obj = this.a.get("usingExtractorStream");
        if (!(obj instanceof Boolean)) {
            return false;
        }
        return ((Boolean) obj).booleanValue();
    }
}
