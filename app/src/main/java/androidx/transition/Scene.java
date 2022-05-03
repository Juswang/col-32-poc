package androidx.transition;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* loaded from: classes2.dex */
public class Scene {
    private Context mContext;
    private Runnable mEnterAction;
    private Runnable mExitAction;
    private View mLayout;
    private int mLayoutId;
    private ViewGroup mSceneRoot;

    @NonNull
    public static Scene getSceneForLayout(@NonNull ViewGroup sceneRoot, @LayoutRes int layoutId, @NonNull Context context) {
        SparseArray<Scene> scenes = (SparseArray) sceneRoot.getTag(R.id.transition_scene_layoutid_cache);
        if (scenes == null) {
            scenes = new SparseArray<>();
            sceneRoot.setTag(R.id.transition_scene_layoutid_cache, scenes);
        }
        Scene scene = scenes.get(layoutId);
        if (scene != null) {
            return scene;
        }
        Scene scene2 = new Scene(sceneRoot, layoutId, context);
        scenes.put(layoutId, scene2);
        return scene2;
    }

    public Scene(@NonNull ViewGroup sceneRoot) {
        this.mLayoutId = -1;
        this.mSceneRoot = sceneRoot;
    }

    private Scene(ViewGroup sceneRoot, int layoutId, Context context) {
        this.mLayoutId = -1;
        this.mContext = context;
        this.mSceneRoot = sceneRoot;
        this.mLayoutId = layoutId;
    }

    public Scene(@NonNull ViewGroup sceneRoot, @NonNull View layout) {
        this.mLayoutId = -1;
        this.mSceneRoot = sceneRoot;
        this.mLayout = layout;
    }

    @NonNull
    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }

    public void exit() {
        Runnable runnable;
        if (getCurrentScene(this.mSceneRoot) == this && (runnable = this.mExitAction) != null) {
            runnable.run();
        }
    }

    public void enter() {
        if (this.mLayoutId > 0 || this.mLayout != null) {
            getSceneRoot().removeAllViews();
            if (this.mLayoutId > 0) {
                LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
            } else {
                this.mSceneRoot.addView(this.mLayout);
            }
        }
        Runnable runnable = this.mEnterAction;
        if (runnable != null) {
            runnable.run();
        }
        setCurrentScene(this.mSceneRoot, this);
    }

    public static void setCurrentScene(View view, Scene scene) {
        view.setTag(R.id.transition_current_scene, scene);
    }

    public static Scene getCurrentScene(View view) {
        return (Scene) view.getTag(R.id.transition_current_scene);
    }

    public void setEnterAction(@Nullable Runnable action) {
        this.mEnterAction = action;
    }

    public void setExitAction(@Nullable Runnable action) {
        this.mExitAction = action;
    }

    public boolean isCreatedFromLayoutResource() {
        return this.mLayoutId > 0;
    }
}
