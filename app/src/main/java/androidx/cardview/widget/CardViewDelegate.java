package androidx.cardview.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface CardViewDelegate {
    Drawable getCardBackground();

    View getCardView();

    boolean getPreventCornerOverlap();

    boolean getUseCompatPadding();

    void setCardBackground(Drawable drawable);

    void setMinWidthHeightInternal(int i, int i2);

    void setShadowPadding(int i, int i2, int i3, int i4);
}
