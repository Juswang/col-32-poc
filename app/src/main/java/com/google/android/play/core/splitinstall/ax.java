package com.google.android.play.core.splitinstall;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public final class ax {
    private final Context a;

    public ax(@NonNull Context context) {
        this.a = context;
    }

    private final SharedPreferences b() {
        return this.a.getSharedPreferences("playcore_split_install_internal", 0);
    }

    public final synchronized Set<String> a() {
        Set<String> stringSet;
        try {
            stringSet = b().getStringSet("deferred_uninstall_module_list", new HashSet());
            if (stringSet == null) {
                stringSet = new HashSet<>();
            }
        } catch (Exception e) {
            return new HashSet();
        }
        return stringSet;
    }

    public final synchronized void a(Collection<String> collection) {
        Set<String> a = a();
        boolean z = false;
        for (String str : collection) {
            z |= a.add(str);
        }
        if (z) {
            try {
                b().edit().putStringSet("deferred_uninstall_module_list", a).apply();
            } catch (Exception e) {
            }
        }
    }
}
