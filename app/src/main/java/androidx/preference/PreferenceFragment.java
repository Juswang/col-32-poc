package androidx.preference;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.XmlRes;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@Deprecated
/* loaded from: classes2.dex */
public abstract class PreferenceFragment extends Fragment implements PreferenceManager.OnPreferenceTreeClickListener, PreferenceManager.OnDisplayPreferenceDialogListener, PreferenceManager.OnNavigateToScreenListener, DialogPreference.TargetFragment {
    @Deprecated
    public static final String ARG_PREFERENCE_ROOT = "androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private boolean mHavePrefs;
    private boolean mInitDone;
    RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private Runnable mSelectPreferenceRunnable;
    private Context mStyledContext;
    private final DividerDecoration mDividerDecoration = new DividerDecoration();
    private int mLayoutResId = R.layout.preference_list_fragment;
    private final Handler mHandler = new Handler() { // from class: androidx.preference.PreferenceFragment.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                PreferenceFragment.this.bindPreferences();
            }
        }
    };
    private final Runnable mRequestFocus = new Runnable() { // from class: androidx.preference.PreferenceFragment.2
        @Override // java.lang.Runnable
        public void run() {
            PreferenceFragment.this.mList.focusableViewAvailable(PreferenceFragment.this.mList);
        }
    };

    /* loaded from: classes2.dex */
    public interface OnPreferenceDisplayDialogCallback {
        boolean onPreferenceDisplayDialog(@NonNull PreferenceFragment preferenceFragment, Preference preference);
    }

    /* loaded from: classes2.dex */
    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference);
    }

    /* loaded from: classes2.dex */
    public interface OnPreferenceStartScreenCallback {
        boolean onPreferenceStartScreen(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen);
    }

    @Deprecated
    public abstract void onCreatePreferences(Bundle bundle, String str);

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        String rootKey;
        super.onCreate(savedInstanceState);
        TypedValue tv = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, tv, true);
        int theme = tv.resourceId;
        if (theme == 0) {
            theme = R.style.PreferenceThemeOverlay;
        }
        this.mStyledContext = new ContextThemeWrapper(getActivity(), theme);
        this.mPreferenceManager = new PreferenceManager(this.mStyledContext);
        this.mPreferenceManager.setOnNavigateToScreenListener(this);
        Bundle args = getArguments();
        if (args != null) {
            rootKey = getArguments().getString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT");
        } else {
            rootKey = null;
        }
        onCreatePreferences(savedInstanceState, rootKey);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TypedArray a = this.mStyledContext.obtainStyledAttributes(null, R.styleable.PreferenceFragment, TypedArrayUtils.getAttr(this.mStyledContext, R.attr.preferenceFragmentStyle, AndroidResources.ANDROID_R_PREFERENCE_FRAGMENT_STYLE), 0);
        this.mLayoutResId = a.getResourceId(R.styleable.PreferenceFragment_android_layout, this.mLayoutResId);
        Drawable divider = a.getDrawable(R.styleable.PreferenceFragment_android_divider);
        int dividerHeight = a.getDimensionPixelSize(R.styleable.PreferenceFragment_android_dividerHeight, -1);
        boolean allowDividerAfterLastItem = a.getBoolean(R.styleable.PreferenceFragment_allowDividerAfterLastItem, true);
        a.recycle();
        LayoutInflater themedInflater = inflater.cloneInContext(this.mStyledContext);
        View view = themedInflater.inflate(this.mLayoutResId, container, false);
        View rawListContainer = view.findViewById(AndroidResources.ANDROID_R_LIST_CONTAINER);
        if (rawListContainer instanceof ViewGroup) {
            ViewGroup listContainer = (ViewGroup) rawListContainer;
            RecyclerView listView = onCreateRecyclerView(themedInflater, listContainer, savedInstanceState);
            if (listView != null) {
                this.mList = listView;
                listView.addItemDecoration(this.mDividerDecoration);
                setDivider(divider);
                if (dividerHeight != -1) {
                    setDividerHeight(dividerHeight);
                }
                this.mDividerDecoration.setAllowDividerAfterLastItem(allowDividerAfterLastItem);
                if (this.mList.getParent() == null) {
                    listContainer.addView(this.mList);
                }
                this.mHandler.post(this.mRequestFocus);
                return view;
            }
            throw new RuntimeException("Could not create RecyclerView");
        }
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    @Deprecated
    public void setDivider(Drawable divider) {
        this.mDividerDecoration.setDivider(divider);
    }

    @Deprecated
    public void setDividerHeight(int height) {
        this.mDividerDecoration.setDividerHeight(height);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle container;
        PreferenceScreen preferenceScreen;
        super.onViewCreated(view, savedInstanceState);
        if (!(savedInstanceState == null || (container = savedInstanceState.getBundle(PREFERENCES_TAG)) == null || (preferenceScreen = getPreferenceScreen()) == null)) {
            preferenceScreen.restoreHierarchyState(container);
        }
        if (this.mHavePrefs) {
            bindPreferences();
            Runnable runnable = this.mSelectPreferenceRunnable;
            if (runnable != null) {
                runnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle container = new Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(PREFERENCES_TAG, container);
        }
    }

    @Deprecated
    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    @Deprecated
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                postBindPreferences();
            }
        }
    }

    @Deprecated
    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }

    @Deprecated
    public void addPreferencesFromResource(@XmlRes int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromResource(this.mStyledContext, preferencesResId, getPreferenceScreen()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    @Deprecated
    public void setPreferencesFromResource(@XmlRes int preferencesResId, @Nullable String key) {
        Preference root;
        requirePreferenceManager();
        PreferenceScreen xmlRoot = this.mPreferenceManager.inflateFromResource(this.mStyledContext, preferencesResId, null);
        if (key != null) {
            root = xmlRoot.findPreference(key);
            if (!(root instanceof PreferenceScreen)) {
                throw new IllegalArgumentException("Preference object with key " + key + " is not a PreferenceScreen");
            }
        } else {
            root = xmlRoot;
        }
        setPreferenceScreen(root);
    }

    @Override // androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    @Deprecated
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() == null) {
            return false;
        }
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceStartFragmentCallback) {
            handled = ((OnPreferenceStartFragmentCallback) getCallbackFragment()).onPreferenceStartFragment(this, preference);
        }
        if (handled || !(getActivity() instanceof OnPreferenceStartFragmentCallback)) {
            return handled;
        }
        boolean handled2 = ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(this, preference);
        return handled2;
    }

    @Override // androidx.preference.PreferenceManager.OnNavigateToScreenListener
    @Deprecated
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceStartScreenCallback) {
            handled = ((OnPreferenceStartScreenCallback) getCallbackFragment()).onPreferenceStartScreen(this, preferenceScreen);
        }
        if (!handled && (getActivity() instanceof OnPreferenceStartScreenCallback)) {
            ((OnPreferenceStartScreenCallback) getActivity()).onPreferenceStartScreen(this, preferenceScreen);
        }
    }

    @Override // androidx.preference.DialogPreference.TargetFragment
    @Deprecated
    public Preference findPreference(CharSequence key) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return preferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            getListView().setAdapter(onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        onBindPreferences();
    }

    private void unbindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        onUnbindPreferences();
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    protected void onBindPreferences() {
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    protected void onUnbindPreferences() {
    }

    @Deprecated
    public final RecyclerView getListView() {
        return this.mList;
    }

    @Deprecated
    public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        if (this.mStyledContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") && (recyclerView = (RecyclerView) parent.findViewById(R.id.recycler_view)) != null) {
            return recyclerView;
        }
        RecyclerView recyclerView2 = (RecyclerView) inflater.inflate(R.layout.preference_recyclerview, parent, false);
        recyclerView2.setLayoutManager(onCreateLayoutManager());
        recyclerView2.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(recyclerView2));
        return recyclerView2;
    }

    @Deprecated
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Deprecated
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }

    @Override // androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
    @Deprecated
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment f;
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback) {
            handled = ((OnPreferenceDisplayDialogCallback) getCallbackFragment()).onPreferenceDisplayDialog(this, preference);
        }
        if (!handled && (getActivity() instanceof OnPreferenceDisplayDialogCallback)) {
            handled = ((OnPreferenceDisplayDialogCallback) getActivity()).onPreferenceDisplayDialog(this, preference);
        }
        if (!handled && getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
            if (preference instanceof EditTextPreference) {
                f = EditTextPreferenceDialogFragment.newInstance(preference.getKey());
            } else if (preference instanceof ListPreference) {
                f = ListPreferenceDialogFragment.newInstance(preference.getKey());
            } else if (preference instanceof MultiSelectListPreference) {
                f = MultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
            } else {
                throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
            }
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public Fragment getCallbackFragment() {
        return null;
    }

    @Deprecated
    public void scrollToPreference(String key) {
        scrollToPreferenceInternal(null, key);
    }

    @Deprecated
    public void scrollToPreference(Preference preference) {
        scrollToPreferenceInternal(preference, null);
    }

    private void scrollToPreferenceInternal(final Preference preference, final String key) {
        Runnable r = new Runnable() { // from class: androidx.preference.PreferenceFragment.3
            @Override // java.lang.Runnable
            public void run() {
                int position;
                RecyclerView.Adapter adapter = PreferenceFragment.this.mList.getAdapter();
                if (adapter instanceof PreferenceGroup.PreferencePositionCallback) {
                    Preference preference2 = preference;
                    if (preference2 != null) {
                        position = ((PreferenceGroup.PreferencePositionCallback) adapter).getPreferenceAdapterPosition(preference2);
                    } else {
                        position = ((PreferenceGroup.PreferencePositionCallback) adapter).getPreferenceAdapterPosition(key);
                    }
                    if (position != -1) {
                        PreferenceFragment.this.mList.scrollToPosition(position);
                    } else {
                        adapter.registerAdapterDataObserver(new ScrollToPreferenceObserver(adapter, PreferenceFragment.this.mList, preference, key));
                    }
                } else if (adapter != null) {
                    throw new IllegalStateException("Adapter must implement PreferencePositionCallback");
                }
            }
        };
        if (this.mList == null) {
            this.mSelectPreferenceRunnable = r;
        } else {
            r.run();
        }
    }

    /* loaded from: classes2.dex */
    private static class ScrollToPreferenceObserver extends RecyclerView.AdapterDataObserver {
        private final RecyclerView.Adapter mAdapter;
        private final String mKey;
        private final RecyclerView mList;
        private final Preference mPreference;

        ScrollToPreferenceObserver(RecyclerView.Adapter adapter, RecyclerView list, Preference preference, String key) {
            this.mAdapter = adapter;
            this.mList = list;
            this.mPreference = preference;
            this.mKey = key;
        }

        private void scrollToPreference() {
            int position;
            this.mAdapter.unregisterAdapterDataObserver(this);
            Preference preference = this.mPreference;
            if (preference != null) {
                position = ((PreferenceGroup.PreferencePositionCallback) this.mAdapter).getPreferenceAdapterPosition(preference);
            } else {
                position = ((PreferenceGroup.PreferencePositionCallback) this.mAdapter).getPreferenceAdapterPosition(this.mKey);
            }
            if (position != -1) {
                this.mList.scrollToPosition(position);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            scrollToPreference();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            scrollToPreference();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            scrollToPreference();
        }
    }

    /* loaded from: classes2.dex */
    public class DividerDecoration extends RecyclerView.ItemDecoration {
        private boolean mAllowDividerAfterLastItem = true;
        private Drawable mDivider;
        private int mDividerHeight;

        DividerDecoration() {
            PreferenceFragment.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (this.mDivider != null) {
                int childCount = parent.getChildCount();
                int width = parent.getWidth();
                for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
                    View view = parent.getChildAt(childViewIndex);
                    if (shouldDrawDividerBelow(view, parent)) {
                        int top = ((int) view.getY()) + view.getHeight();
                        this.mDivider.setBounds(0, top, width, this.mDividerHeight + top);
                        this.mDivider.draw(c);
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (shouldDrawDividerBelow(view, parent)) {
                outRect.bottom = this.mDividerHeight;
            }
        }

        private boolean shouldDrawDividerBelow(View view, RecyclerView parent) {
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
            boolean z = false;
            boolean dividerAllowedBelow = (holder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) holder).isDividerAllowedBelow();
            if (!dividerAllowedBelow) {
                return false;
            }
            boolean nextAllowed = this.mAllowDividerAfterLastItem;
            int index = parent.indexOfChild(view);
            if (index >= parent.getChildCount() - 1) {
                return nextAllowed;
            }
            View nextView = parent.getChildAt(index + 1);
            RecyclerView.ViewHolder nextHolder = parent.getChildViewHolder(nextView);
            if ((nextHolder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) nextHolder).isDividerAllowedAbove()) {
                z = true;
            }
            boolean nextAllowed2 = z;
            return nextAllowed2;
        }

        public void setDivider(Drawable divider) {
            if (divider != null) {
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerHeight = 0;
            }
            this.mDivider = divider;
            PreferenceFragment.this.mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int dividerHeight) {
            this.mDividerHeight = dividerHeight;
            PreferenceFragment.this.mList.invalidateItemDecorations();
        }

        public void setAllowDividerAfterLastItem(boolean allowDividerAfterLastItem) {
            this.mAllowDividerAfterLastItem = allowDividerAfterLastItem;
        }
    }
}
