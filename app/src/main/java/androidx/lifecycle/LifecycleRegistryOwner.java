package androidx.lifecycle;

import androidx.annotation.NonNull;

@Deprecated
/* loaded from: classes2.dex */
public interface LifecycleRegistryOwner extends LifecycleOwner {
    @Override // androidx.lifecycle.LifecycleOwner
    @NonNull
    LifecycleRegistry getLifecycle();
}
