package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* loaded from: classes2.dex */
public abstract class FragmentContainer {
    @Nullable
    public abstract View onFindViewById(@IdRes int i);

    public abstract boolean onHasView();

    @NonNull
    @Deprecated
    public Fragment instantiate(@NonNull Context context, @NonNull String className, @Nullable Bundle arguments) {
        return Fragment.instantiate(context, className, arguments);
    }
}
