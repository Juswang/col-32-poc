package androidx.core.view;

import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;

/* loaded from: classes2.dex */
public final class OneShotPreDrawListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    private final Runnable mRunnable;
    private final View mView;
    private ViewTreeObserver mViewTreeObserver;

    private OneShotPreDrawListener(View view, Runnable runnable) {
        this.mView = view;
        this.mViewTreeObserver = view.getViewTreeObserver();
        this.mRunnable = runnable;
    }

    @NonNull
    public static OneShotPreDrawListener add(@NonNull View view, @NonNull Runnable runnable) {
        if (view == null) {
            throw new NullPointerException("view == null");
        } else if (runnable != null) {
            OneShotPreDrawListener listener = new OneShotPreDrawListener(view, runnable);
            view.getViewTreeObserver().addOnPreDrawListener(listener);
            view.addOnAttachStateChangeListener(listener);
            return listener;
        } else {
            throw new NullPointerException("runnable == null");
        }
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        removeListener();
        this.mRunnable.run();
        return true;
    }

    public void removeListener() {
        if (this.mViewTreeObserver.isAlive()) {
            this.mViewTreeObserver.removeOnPreDrawListener(this);
        } else {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        this.mView.removeOnAttachStateChangeListener(this);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View v) {
        this.mViewTreeObserver = v.getViewTreeObserver();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View v) {
        removeListener();
    }
}
