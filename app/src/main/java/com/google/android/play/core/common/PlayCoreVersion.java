package com.google.android.play.core.common;

import android.os.Bundle;
import com.google.android.play.core.internal.af;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class PlayCoreVersion {
    private static final Set<String> a = new HashSet(Arrays.asList("review"));
    private static final Set<String> b = new HashSet(Arrays.asList("native", "unity"));
    private static final Map<String, Map<String, Integer>> c = new HashMap();
    private static final af d = new af("PlayCoreVersion");

    private PlayCoreVersion() {
    }

    public static Bundle a() {
        Bundle bundle = new Bundle();
        Map<String, Integer> a2 = a("review");
        bundle.putInt("playcore_version_code", a2.get("java").intValue());
        if (a2.containsKey("native")) {
            bundle.putInt("playcore_native_version", a2.get("native").intValue());
        }
        if (a2.containsKey("unity")) {
            bundle.putInt("playcore_unity_version", a2.get("unity").intValue());
        }
        return bundle;
    }

    public static synchronized Map<String, Integer> a(String str) {
        Map<String, Integer> map;
        synchronized (PlayCoreVersion.class) {
            if (!c.containsKey(str)) {
                HashMap hashMap = new HashMap();
                hashMap.put("java", 10800);
                c.put(str, hashMap);
            }
            map = c.get(str);
        }
        return map;
    }

    public static synchronized void addVersion(String str, String str2, int i) {
        synchronized (PlayCoreVersion.class) {
            if (!a.contains(str)) {
                d.d("Illegal module name: %s", str);
            } else if (!b.contains(str2)) {
                d.d("Illegal platform name: %s", str2);
            } else {
                a(str).put(str2, Integer.valueOf(i));
            }
        }
    }
}
