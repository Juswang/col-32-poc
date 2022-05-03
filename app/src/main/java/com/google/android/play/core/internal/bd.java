package com.google.android.play.core.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;

/* loaded from: classes2.dex */
public final class bd implements be {
    @Override // com.google.android.play.core.internal.be
    public final Object[] a(Object obj, List<File> list, List<IOException> list2) {
        return (Object[]) bq.a(obj, "makePathElements", Object[].class, List.class, list, File.class, null, List.class, list2);
    }
}
