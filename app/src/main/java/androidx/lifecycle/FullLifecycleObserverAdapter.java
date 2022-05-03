package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;

/* loaded from: classes2.dex */
public class FullLifecycleObserverAdapter implements LifecycleEventObserver {
    private final FullLifecycleObserver mFullLifecycleObserver;
    private final LifecycleEventObserver mLifecycleEventObserver;

    public FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver, LifecycleEventObserver lifecycleEventObserver) {
        this.mFullLifecycleObserver = fullLifecycleObserver;
        this.mLifecycleEventObserver = lifecycleEventObserver;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                this.mFullLifecycleObserver.onCreate(source);
                break;
            case ON_START:
                this.mFullLifecycleObserver.onStart(source);
                break;
            case ON_RESUME:
                this.mFullLifecycleObserver.onResume(source);
                break;
            case ON_PAUSE:
                this.mFullLifecycleObserver.onPause(source);
                break;
            case ON_STOP:
                this.mFullLifecycleObserver.onStop(source);
                break;
            case ON_DESTROY:
                this.mFullLifecycleObserver.onDestroy(source);
                break;
            case ON_ANY:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
        }
        LifecycleEventObserver lifecycleEventObserver = this.mLifecycleEventObserver;
        if (lifecycleEventObserver != null) {
            lifecycleEventObserver.onStateChanged(source, event);
        }
    }
}
