package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.appcompat.resources.R;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.LruCache;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* loaded from: classes2.dex */
public final class ResourceManagerInternal {
    private static final boolean DEBUG = false;
    private static ResourceManagerInternal INSTANCE = null;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "ResourceManagerInternal";
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<>(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private ResourceManagerHooks mHooks;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);

    /* loaded from: classes2.dex */
    public interface InflateDelegate {
        Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
    /* loaded from: classes2.dex */
    public interface ResourceManagerHooks {
        Drawable createDrawableFor(@NonNull ResourceManagerInternal resourceManagerInternal, @NonNull Context context, @DrawableRes int i);

        ColorStateList getTintListForDrawableRes(@NonNull Context context, @DrawableRes int i);

        PorterDuff.Mode getTintModeForDrawableRes(int i);

        boolean tintDrawable(@NonNull Context context, @DrawableRes int i, @NonNull Drawable drawable);

        boolean tintDrawableUsingColorFilter(@NonNull Context context, @DrawableRes int i, @NonNull Drawable drawable);
    }

    public static synchronized ResourceManagerInternal get() {
        ResourceManagerInternal resourceManagerInternal;
        synchronized (ResourceManagerInternal.class) {
            if (INSTANCE == null) {
                INSTANCE = new ResourceManagerInternal();
                installDefaultInflateDelegates(INSTANCE);
            }
            resourceManagerInternal = INSTANCE;
        }
        return resourceManagerInternal;
    }

    private static void installDefaultInflateDelegates(@NonNull ResourceManagerInternal manager) {
        if (Build.VERSION.SDK_INT < 24) {
            manager.addDelegate("vector", new VdcInflateDelegate());
            manager.addDelegate("animated-vector", new AvdcInflateDelegate());
            manager.addDelegate("animated-selector", new AsldcInflateDelegate());
        }
    }

    public synchronized void setHooks(ResourceManagerHooks hooks) {
        this.mHooks = hooks;
    }

    public synchronized Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return getDrawable(context, resId, false);
    }

    public synchronized Drawable getDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown) {
        Drawable drawable;
        checkVectorDrawableSetup(context);
        drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = createDrawableIfNeeded(context, resId);
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, resId);
        }
        if (drawable != null) {
            drawable = tintDrawable(context, resId, failIfNotKnown, drawable);
        }
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        return drawable;
    }

    public synchronized void onConfigurationChanged(@NonNull Context context) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
        if (cache != null) {
            cache.clear();
        }
    }

    private static long createCacheKey(TypedValue tv) {
        return (tv.assetCookie << 32) | tv.data;
    }

    private Drawable createDrawableIfNeeded(@NonNull Context context, @DrawableRes int resId) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue tv = this.mTypedValue;
        context.getResources().getValue(resId, tv, true);
        long key = createCacheKey(tv);
        Drawable dr = getCachedDrawable(context, key);
        if (dr != null) {
            return dr;
        }
        ResourceManagerHooks resourceManagerHooks = this.mHooks;
        Drawable dr2 = resourceManagerHooks == null ? null : resourceManagerHooks.createDrawableFor(this, context, resId);
        if (dr2 != null) {
            dr2.setChangingConfigurations(tv.changingConfigurations);
            addDrawableToCache(context, key, dr2);
        }
        return dr2;
    }

    private Drawable tintDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown, @NonNull Drawable drawable) {
        ColorStateList tintList = getTintList(context, resId);
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            Drawable drawable2 = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable2, tintList);
            PorterDuff.Mode tintMode = getTintMode(resId);
            if (tintMode == null) {
                return drawable2;
            }
            DrawableCompat.setTintMode(drawable2, tintMode);
            return drawable2;
        }
        ResourceManagerHooks resourceManagerHooks = this.mHooks;
        if (resourceManagerHooks != null && resourceManagerHooks.tintDrawable(context, resId, drawable)) {
            return drawable;
        }
        boolean tinted = tintDrawableUsingColorFilter(context, resId, drawable);
        if (tinted || !failIfNotKnown) {
            return drawable;
        }
        return null;
    }

    private Drawable loadDrawableFromDelegates(@NonNull Context context, @DrawableRes int resId) {
        int type;
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap == null || arrayMap.isEmpty()) {
            return null;
        }
        SparseArrayCompat<String> sparseArrayCompat = this.mKnownDrawableIdTags;
        if (sparseArrayCompat != null) {
            String cachedTagName = sparseArrayCompat.get(resId);
            if (SKIP_DRAWABLE_TAG.equals(cachedTagName) || (cachedTagName != null && this.mDelegates.get(cachedTagName) == null)) {
                return null;
            }
        } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue tv = this.mTypedValue;
        Resources res = context.getResources();
        res.getValue(resId, tv, true);
        long key = createCacheKey(tv);
        Drawable dr = getCachedDrawable(context, key);
        if (dr != null) {
            return dr;
        }
        if (tv.string != null && tv.string.toString().endsWith(".xml")) {
            try {
                XmlPullParser parser = res.getXml(resId);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                while (true) {
                    type = parser.next();
                    if (type == 2 || type == 1) {
                        break;
                    }
                }
                if (type == 2) {
                    String tagName = parser.getName();
                    this.mKnownDrawableIdTags.append(resId, tagName);
                    InflateDelegate delegate = this.mDelegates.get(tagName);
                    if (delegate != null) {
                        dr = delegate.createFromXmlInner(context, parser, attrs, context.getTheme());
                    }
                    if (dr != null) {
                        dr.setChangingConfigurations(tv.changingConfigurations);
                        addDrawableToCache(context, key, dr);
                    }
                } else {
                    throw new XmlPullParserException("No start tag found");
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while inflating drawable", e);
            }
        }
        if (dr == null) {
            this.mKnownDrawableIdTags.append(resId, SKIP_DRAWABLE_TAG);
        }
        return dr;
    }

    private synchronized Drawable getCachedDrawable(@NonNull Context context, long key) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
        if (cache == null) {
            return null;
        }
        WeakReference<Drawable.ConstantState> wr = cache.get(key);
        if (wr != null) {
            Drawable.ConstantState entry = wr.get();
            if (entry != null) {
                return entry.newDrawable(context.getResources());
            }
            cache.delete(key);
        }
        return null;
    }

    private synchronized boolean addDrawableToCache(@NonNull Context context, long key, @NonNull Drawable drawable) {
        Drawable.ConstantState cs = drawable.getConstantState();
        if (cs == null) {
            return false;
        }
        LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
        if (cache == null) {
            cache = new LongSparseArray<>();
            this.mDrawableCaches.put(context, cache);
        }
        cache.put(key, new WeakReference<>(cs));
        return true;
    }

    public synchronized Drawable onDrawableLoadedFromResources(@NonNull Context context, @NonNull VectorEnabledTintResources resources, @DrawableRes int resId) {
        Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = resources.superGetDrawable(resId);
        }
        if (drawable == null) {
            return null;
        }
        return tintDrawable(context, resId, false, drawable);
    }

    public boolean tintDrawableUsingColorFilter(@NonNull Context context, @DrawableRes int resId, @NonNull Drawable drawable) {
        ResourceManagerHooks resourceManagerHooks = this.mHooks;
        return resourceManagerHooks != null && resourceManagerHooks.tintDrawableUsingColorFilter(context, resId, drawable);
    }

    private void addDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<>();
        }
        this.mDelegates.put(tagName, delegate);
    }

    private void removeDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap != null && arrayMap.get(tagName) == delegate) {
            this.mDelegates.remove(tagName);
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int id : array) {
            if (id == value) {
                return true;
            }
        }
        return false;
    }

    PorterDuff.Mode getTintMode(int resId) {
        ResourceManagerHooks resourceManagerHooks = this.mHooks;
        if (resourceManagerHooks == null) {
            return null;
        }
        return resourceManagerHooks.getTintModeForDrawableRes(resId);
    }

    public synchronized ColorStateList getTintList(@NonNull Context context, @DrawableRes int resId) {
        ColorStateList tint;
        tint = getTintListFromCache(context, resId);
        if (tint == null) {
            tint = this.mHooks == null ? null : this.mHooks.getTintListForDrawableRes(context, resId);
            if (tint != null) {
                addTintListToCache(context, resId, tint);
            }
        }
        return tint;
    }

    private ColorStateList getTintListFromCache(@NonNull Context context, @DrawableRes int resId) {
        SparseArrayCompat<ColorStateList> tints;
        WeakHashMap<Context, SparseArrayCompat<ColorStateList>> weakHashMap = this.mTintLists;
        if (weakHashMap == null || (tints = weakHashMap.get(context)) == null) {
            return null;
        }
        return tints.get(resId);
    }

    private void addTintListToCache(@NonNull Context context, @DrawableRes int resId, @NonNull ColorStateList tintList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<>();
        }
        SparseArrayCompat<ColorStateList> themeTints = this.mTintLists.get(context);
        if (themeTints == null) {
            themeTints = new SparseArrayCompat<>();
            this.mTintLists.put(context, themeTints);
        }
        themeTints.append(resId, tintList);
    }

    /* loaded from: classes2.dex */
    public static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }

        PorterDuffColorFilter get(int color, PorterDuff.Mode mode) {
            return get(Integer.valueOf(generateCacheKey(color, mode)));
        }

        PorterDuffColorFilter put(int color, PorterDuff.Mode mode, PorterDuffColorFilter filter) {
            return put(Integer.valueOf(generateCacheKey(color, mode)), filter);
        }

        private static int generateCacheKey(int color, PorterDuff.Mode mode) {
            int hashCode = (1 * 31) + color;
            return (hashCode * 31) + mode.hashCode();
        }
    }

    public static void tintDrawable(Drawable drawable, TintInfo tint, int[] state) {
        if (!DrawableUtils.canSafelyMutateDrawable(drawable) || drawable.mutate() == drawable) {
            if (tint.mHasTintList || tint.mHasTintMode) {
                drawable.setColorFilter(createTintFilter(tint.mHasTintList ? tint.mTintList : null, tint.mHasTintMode ? tint.mTintMode : DEFAULT_MODE, state));
            } else {
                drawable.clearColorFilter();
            }
            if (Build.VERSION.SDK_INT <= 23) {
                drawable.invalidateSelf();
                return;
            }
            return;
        }
        Log.d(TAG, "Mutated drawable is not the same instance as the input.");
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode, int[] state) {
        if (tint == null || tintMode == null) {
            return null;
        }
        int color = tint.getColorForState(state, 0);
        return getPorterDuffColorFilter(color, tintMode);
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        PorterDuffColorFilter filter;
        synchronized (ResourceManagerInternal.class) {
            filter = COLOR_FILTER_CACHE.get(color, mode);
            if (filter == null) {
                filter = new PorterDuffColorFilter(color, mode);
                COLOR_FILTER_CACHE.put(color, mode, filter);
            }
        }
        return filter;
    }

    private void checkVectorDrawableSetup(@NonNull Context context) {
        if (!this.mHasCheckedVectorDrawableSetup) {
            this.mHasCheckedVectorDrawableSetup = true;
            Drawable d = getDrawable(context, R.drawable.abc_vector_test);
            if (d == null || !isVectorDrawable(d)) {
                this.mHasCheckedVectorDrawableSetup = false;
                throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
            }
        }
    }

    private static boolean isVectorDrawable(@NonNull Drawable d) {
        return (d instanceof VectorDrawableCompat) || PLATFORM_VD_CLAZZ.equals(d.getClass().getName());
    }

    /* loaded from: classes2.dex */
    public static class VdcInflateDelegate implements InflateDelegate {
        VdcInflateDelegate() {
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.InflateDelegate
        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class AvdcInflateDelegate implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.InflateDelegate
        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }

    @RequiresApi(11)
    /* loaded from: classes2.dex */
    public static class AsldcInflateDelegate implements InflateDelegate {
        AsldcInflateDelegate() {
        }

        @Override // androidx.appcompat.widget.ResourceManagerInternal.InflateDelegate
        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return AnimatedStateListDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", e);
                return null;
            }
        }
    }
}
