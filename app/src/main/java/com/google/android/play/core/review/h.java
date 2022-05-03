package com.google.android.play.core.review;

import android.content.Context;
import android.content.Intent;
import com.google.android.play.core.internal.ab;
import com.google.android.play.core.internal.af;
import com.google.android.play.core.internal.ap;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.i;

/* loaded from: classes2.dex */
public final class h {
    private static final af b = new af("ReviewService");
    final ap<ab> a;
    private final String c;

    public h(Context context) {
        this.c = context.getPackageName();
        this.a = new ap<>(context, b, "com.google.android.finsky.inappreviewservice.InAppReviewService", new Intent("com.google.android.finsky.BIND_IN_APP_REVIEW_SERVICE").setPackage("com.android.vending"), d.a);
    }

    public final Task<ReviewInfo> a() {
        b.c("requestInAppReview (%s)", this.c);
        i iVar = new i();
        this.a.a(new e(this, iVar, iVar));
        return iVar.a();
    }
}
