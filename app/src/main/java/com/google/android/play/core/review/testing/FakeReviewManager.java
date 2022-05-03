package com.google.android.play.core.review.testing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.tasks.Tasks;

/* loaded from: classes2.dex */
public class FakeReviewManager implements ReviewManager {
    private final Context a;
    private ReviewInfo b;

    public FakeReviewManager(Context context) {
        this.a = context;
    }

    @Override // com.google.android.play.core.review.ReviewManager
    @NonNull
    public Task<Void> launchReviewFlow(@NonNull Activity activity, @NonNull ReviewInfo reviewInfo) {
        return reviewInfo != this.b ? Tasks.a((Exception) new a()) : Tasks.a((Object) null);
    }

    @Override // com.google.android.play.core.review.ReviewManager
    @NonNull
    public Task<ReviewInfo> requestReviewFlow() {
        this.b = ReviewInfo.a(PendingIntent.getBroadcast(this.a, 0, new Intent(), 0));
        return Tasks.a(this.b);
    }
}
