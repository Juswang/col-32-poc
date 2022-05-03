package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class FragmentManager {
    static final FragmentFactory DEFAULT_FACTORY = new FragmentFactory();
    public static final int POP_BACK_STACK_INCLUSIVE = 1;
    private FragmentFactory mFragmentFactory = null;

    /* loaded from: classes2.dex */
    public interface BackStackEntry {
        @Nullable
        CharSequence getBreadCrumbShortTitle();

        @StringRes
        int getBreadCrumbShortTitleRes();

        @Nullable
        CharSequence getBreadCrumbTitle();

        @StringRes
        int getBreadCrumbTitleRes();

        int getId();

        @Nullable
        String getName();
    }

    /* loaded from: classes2.dex */
    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    public abstract void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener onBackStackChangedListener);

    @NonNull
    public abstract FragmentTransaction beginTransaction();

    public abstract void dump(@NonNull String str, @Nullable FileDescriptor fileDescriptor, @NonNull PrintWriter printWriter, @Nullable String[] strArr);

    public abstract boolean executePendingTransactions();

    @Nullable
    public abstract Fragment findFragmentById(@IdRes int i);

    @Nullable
    public abstract Fragment findFragmentByTag(@Nullable String str);

    @NonNull
    public abstract BackStackEntry getBackStackEntryAt(int i);

    public abstract int getBackStackEntryCount();

    @Nullable
    public abstract Fragment getFragment(@NonNull Bundle bundle, @NonNull String str);

    @NonNull
    public abstract List<Fragment> getFragments();

    @Nullable
    public abstract Fragment getPrimaryNavigationFragment();

    public abstract boolean isDestroyed();

    public abstract boolean isStateSaved();

    public abstract void popBackStack();

    public abstract void popBackStack(int i, int i2);

    public abstract void popBackStack(@Nullable String str, int i);

    public abstract boolean popBackStackImmediate();

    public abstract boolean popBackStackImmediate(int i, int i2);

    public abstract boolean popBackStackImmediate(@Nullable String str, int i);

    public abstract void putFragment(@NonNull Bundle bundle, @NonNull String str, @NonNull Fragment fragment);

    public abstract void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z);

    public abstract void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener onBackStackChangedListener);

    @Nullable
    public abstract Fragment.SavedState saveFragmentInstanceState(@NonNull Fragment fragment);

    public abstract void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks fragmentLifecycleCallbacks);

    @NonNull
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
    @Deprecated
    public FragmentTransaction openTransaction() {
        return beginTransaction();
    }

    public void setFragmentFactory(@NonNull FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    @NonNull
    public FragmentFactory getFragmentFactory() {
        if (this.mFragmentFactory == null) {
            this.mFragmentFactory = DEFAULT_FACTORY;
        }
        return this.mFragmentFactory;
    }

    public static void enableDebugLogging(boolean enabled) {
        FragmentManagerImpl.DEBUG = enabled;
    }

    /* loaded from: classes2.dex */
    public static abstract class FragmentLifecycleCallbacks {
        public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        }

        public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        }

        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        }

        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        }

        public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        }

        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
        }

        public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
        }

        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }

        public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
        }
    }
}
