package androidx.transition;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

/* loaded from: classes2.dex */
public class ViewGroupOverlayApi14 extends ViewOverlayApi14 implements ViewGroupOverlayImpl {
    public ViewGroupOverlayApi14(Context context, ViewGroup hostView, View requestingView) {
        super(context, hostView, requestingView);
    }

    public static ViewGroupOverlayApi14 createFrom(ViewGroup viewGroup) {
        return (ViewGroupOverlayApi14) ViewOverlayApi14.createFrom(viewGroup);
    }

    @Override // androidx.transition.ViewGroupOverlayImpl
    public void add(@NonNull View view) {
        this.mOverlayViewGroup.add(view);
    }

    @Override // androidx.transition.ViewGroupOverlayImpl
    public void remove(@NonNull View view) {
        this.mOverlayViewGroup.remove(view);
    }
}