package com.google.android.play.core.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public final class bc implements az {
    @Override // com.google.android.play.core.internal.az
    public final Object[] a(Object obj, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) {
        return (Object[]) bq.a(obj, "makePathElements", Object[].class, List.class, arrayList, File.class, file, List.class, arrayList2);
    }
}
