package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.R;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import androidx.core.view.GravityCompat;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();

    public ActionMenuPresenter(Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void initForMenu(@NonNull Context context, @Nullable MenuBuilder menu) {
        super.initForMenu(context, menu);
        Resources res = context.getResources();
        ActionBarPolicy abp = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = abp.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = abp.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = abp.getMaxActionButtons();
        }
        int width = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int spec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(spec, spec);
            }
            width -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = width;
        this.mMinCellSize = (int) (res.getDisplayMetrics().density * 56.0f);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    public void setWidthLimit(int width, boolean strict) {
        this.mWidthLimit = width;
        this.mStrictWidthLimit = strict;
        this.mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
        this.mReserveOverflowSet = true;
    }

    public void setItemLimit(int itemCount) {
        this.mMaxItems = itemCount;
        this.mMaxItemsSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean isExclusive) {
        this.mExpandedActionViewsExclusive = isExclusive;
    }

    public void setOverflowIcon(Drawable icon) {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            overflowMenuButton.setImageDrawable(icon);
            return;
        }
        this.mPendingOverflowIconSet = true;
        this.mPendingOverflowIcon = icon;
    }

    public Drawable getOverflowIcon() {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            return overflowMenuButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public MenuView getMenuView(ViewGroup root) {
        MenuView oldMenuView = this.mMenuView;
        MenuView result = super.getMenuView(root);
        if (oldMenuView != result) {
            ((ActionMenuView) result).setPresenter(this);
        }
        return result;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public View getItemView(MenuItemImpl item, View convertView, ViewGroup parent) {
        View actionView = item.getActionView();
        if (actionView == null || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? 8 : 0);
        ActionMenuView menuParent = (ActionMenuView) parent;
        ViewGroup.LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public void bindItemView(MenuItemImpl item, MenuView.ItemView itemView) {
        itemView.initialize(item, 0);
        ActionMenuView menuView = (ActionMenuView) this.mMenuView;
        ActionMenuItemView actionItemView = (ActionMenuItemView) itemView;
        actionItemView.setItemInvoker(menuView);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(this.mPopupCallback);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        return item.isActionButton();
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean cleared) {
        super.updateMenuView(cleared);
        ((View) this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            int count = actionItems.size();
            for (int i = 0; i < count; i++) {
                ActionProvider provider = actionItems.get(i).getSupportActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        boolean hasOverflow = false;
        if (this.mReserveOverflow && nonActionItems != null) {
            int count2 = nonActionItems.size();
            boolean z = false;
            if (count2 == 1) {
                hasOverflow = !nonActionItems.get(0).isActionViewExpanded();
            } else {
                if (count2 > 0) {
                    z = true;
                }
                hasOverflow = z;
            }
        }
        if (hasOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            ViewGroup parent = (ViewGroup) this.mOverflowButton.getParent();
            if (parent != this.mMenuView) {
                if (parent != null) {
                    parent.removeView(this.mOverflowButton);
                }
                ActionMenuView menuView = (ActionMenuView) this.mMenuView;
                menuView.addView(this.mOverflowButton, menuView.generateOverflowButtonLayoutParams());
            }
        } else {
            OverflowMenuButton overflowMenuButton = this.mOverflowButton;
            if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView) {
                ((ViewGroup) this.mMenuView).removeView(this.mOverflowButton);
            }
        }
        ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(parent, childIndex);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != this.mMenu) {
            topSubMenu = (SubMenuBuilder) topSubMenu.getParentMenu();
        }
        View anchor = findViewForItem(topSubMenu.getItem());
        if (anchor == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        boolean preserveIconSpacing = false;
        int count = subMenu.size();
        int i = 0;
        while (true) {
            if (i >= count) {
                break;
            }
            MenuItem childItem = subMenu.getItem(i);
            if (childItem.isVisible() && childItem.getIcon() != null) {
                preserveIconSpacing = true;
                break;
            }
            i++;
        }
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenu, anchor);
        this.mActionButtonPopup.setForceShowIcon(preserveIconSpacing);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    private View findViewForItem(MenuItem item) {
        ViewGroup parent = (ViewGroup) this.mMenuView;
        if (parent == null) {
            return null;
        }
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if ((child instanceof MenuView.ItemView) && ((MenuView.ItemView) child).getItemData() == item) {
                return child;
            }
        }
        return null;
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null || this.mMenu.getNonActionItems().isEmpty()) {
            return false;
        }
        OverflowPopup popup = new OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true);
        this.mPostedOpenRunnable = new OpenOverflowRunnable(popup);
        ((View) this.mMenuView).post(this.mPostedOpenRunnable);
        super.onSubMenuSelected(null);
        return true;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable == null || this.mMenuView == null) {
            MenuPopupHelper popup = this.mOverflowPopup;
            if (popup == null) {
                return false;
            }
            popup.dismiss();
            return true;
        }
        ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
        this.mPostedOpenRunnable = null;
        return true;
    }

    public boolean dismissPopupMenus() {
        boolean result = hideOverflowMenu();
        return result | hideSubMenus();
    }

    public boolean hideSubMenus() {
        ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
        if (actionButtonSubmenu == null) {
            return false;
        }
        actionButtonSubmenu.dismiss();
        return true;
    }

    public boolean isOverflowMenuShowing() {
        OverflowPopup overflowPopup = this.mOverflowPopup;
        return overflowPopup != null && overflowPopup.isShowing();
    }

    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public boolean flagActionItems() {
        int itemsSize;
        ArrayList<MenuItemImpl> visibleItems;
        ArrayList<MenuItemImpl> visibleItems2;
        int requiredItems;
        int maxActions;
        boolean isAction;
        int widthLimit;
        boolean z;
        ActionMenuPresenter actionMenuPresenter = this;
        if (actionMenuPresenter.mMenu != null) {
            visibleItems = actionMenuPresenter.mMenu.getVisibleItems();
            itemsSize = visibleItems.size();
        } else {
            visibleItems = null;
            itemsSize = 0;
        }
        int maxActions2 = actionMenuPresenter.mMaxItems;
        int widthLimit2 = actionMenuPresenter.mActionItemWidthLimit;
        int querySpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup parent = (ViewGroup) actionMenuPresenter.mMenuView;
        int requiredItems2 = 0;
        int requestedItems = 0;
        int firstActionWidth = 0;
        boolean hasOverflow = false;
        for (int i = 0; i < itemsSize; i++) {
            MenuItemImpl item = visibleItems.get(i);
            if (item.requiresActionButton()) {
                requiredItems2++;
            } else if (item.requestsActionButton()) {
                requestedItems++;
            } else {
                hasOverflow = true;
            }
            if (actionMenuPresenter.mExpandedActionViewsExclusive && item.isActionViewExpanded()) {
                maxActions2 = 0;
            }
        }
        if (actionMenuPresenter.mReserveOverflow && (hasOverflow || requiredItems2 + requestedItems > maxActions2)) {
            maxActions2--;
        }
        int maxActions3 = maxActions2 - requiredItems2;
        SparseBooleanArray seenGroups = actionMenuPresenter.mActionButtonGroups;
        seenGroups.clear();
        int cellSize = 0;
        int cellsRemaining = 0;
        if (actionMenuPresenter.mStrictWidthLimit) {
            int i2 = actionMenuPresenter.mMinCellSize;
            cellsRemaining = widthLimit2 / i2;
            int cellSizeRemaining = widthLimit2 % i2;
            cellSize = i2 + (cellSizeRemaining / cellsRemaining);
        }
        int i3 = 0;
        while (i3 < itemsSize) {
            MenuItemImpl item2 = visibleItems.get(i3);
            int itemsSize2 = itemsSize;
            if (item2.requiresActionButton()) {
                View v = actionMenuPresenter.getItemView(item2, null, parent);
                requiredItems = requiredItems2;
                if (actionMenuPresenter.mStrictWidthLimit) {
                    cellsRemaining -= ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                } else {
                    v.measure(querySpec, querySpec);
                }
                int measuredWidth = v.getMeasuredWidth();
                int widthLimit3 = widthLimit2 - measuredWidth;
                if (firstActionWidth == 0) {
                    firstActionWidth = measuredWidth;
                }
                int groupId = item2.getGroupId();
                if (groupId != 0) {
                    widthLimit = widthLimit3;
                    z = true;
                    seenGroups.put(groupId, true);
                } else {
                    widthLimit = widthLimit3;
                    z = true;
                }
                item2.setIsActionButton(z);
                visibleItems2 = visibleItems;
                widthLimit2 = widthLimit;
            } else {
                requiredItems = requiredItems2;
                if (item2.requestsActionButton()) {
                    int groupId2 = item2.getGroupId();
                    boolean inGroup = seenGroups.get(groupId2);
                    boolean isAction2 = (maxActions3 > 0 || inGroup) && widthLimit2 > 0 && (!actionMenuPresenter.mStrictWidthLimit || cellsRemaining > 0);
                    if (isAction2) {
                        boolean isAction3 = isAction2;
                        View v2 = actionMenuPresenter.getItemView(item2, null, parent);
                        maxActions = maxActions3;
                        if (actionMenuPresenter.mStrictWidthLimit) {
                            int cells = ActionMenuView.measureChildForCells(v2, cellSize, cellsRemaining, querySpec, 0);
                            cellsRemaining -= cells;
                            if (cells == 0) {
                                isAction = false;
                            } else {
                                isAction = isAction3;
                            }
                        } else {
                            v2.measure(querySpec, querySpec);
                            isAction = isAction3;
                        }
                        int measuredWidth2 = v2.getMeasuredWidth();
                        widthLimit2 -= measuredWidth2;
                        if (firstActionWidth == 0) {
                            firstActionWidth = measuredWidth2;
                        }
                        if (actionMenuPresenter.mStrictWidthLimit) {
                            isAction2 = (widthLimit2 >= 0) & isAction;
                        } else {
                            isAction2 = (widthLimit2 + firstActionWidth > 0) & isAction;
                        }
                    } else {
                        maxActions = maxActions3;
                    }
                    if (isAction2 && groupId2 != 0) {
                        seenGroups.put(groupId2, true);
                        visibleItems2 = visibleItems;
                    } else if (inGroup) {
                        seenGroups.put(groupId2, false);
                        int j = 0;
                        while (j < i3) {
                            MenuItemImpl areYouMyGroupie = visibleItems.get(j);
                            ArrayList<MenuItemImpl> visibleItems3 = visibleItems;
                            if (areYouMyGroupie.getGroupId() == groupId2) {
                                if (areYouMyGroupie.isActionButton()) {
                                    maxActions++;
                                }
                                areYouMyGroupie.setIsActionButton(false);
                            }
                            j++;
                            visibleItems = visibleItems3;
                        }
                        visibleItems2 = visibleItems;
                    } else {
                        visibleItems2 = visibleItems;
                    }
                    if (isAction2) {
                        maxActions--;
                    }
                    item2.setIsActionButton(isAction2);
                    maxActions3 = maxActions;
                } else {
                    visibleItems2 = visibleItems;
                    item2.setIsActionButton(false);
                }
            }
            i3++;
            actionMenuPresenter = this;
            requiredItems2 = requiredItems;
            itemsSize = itemsSize2;
            visibleItems = visibleItems2;
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState();
        state.openSubMenuId = this.mOpenSubMenuId;
        return state;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable state) {
        MenuItem item;
        if (state instanceof SavedState) {
            SavedState saved = (SavedState) state;
            if (saved.openSubMenuId > 0 && (item = this.mMenu.findItem(saved.openSubMenuId)) != null) {
                SubMenuBuilder subMenu = (SubMenuBuilder) item.getSubMenu();
                onSubMenuSelected(subMenu);
            }
        }
    }

    @Override // androidx.core.view.ActionProvider.SubUiVisibilityListener
    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            super.onSubMenuSelected(null);
        } else if (this.mMenu != null) {
            this.mMenu.close(false);
        }
    }

    public void setMenuView(ActionMenuView menuView) {
        this.mMenuView = menuView;
        menuView.initialize(this.mMenu);
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes2.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.appcompat.widget.ActionMenuPresenter.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel in) {
            this.openSubMenuId = in.readInt();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.openSubMenuId);
        }
    }

    /* loaded from: classes2.dex */
    public class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
        private final float[] mTempPts = new float[2];

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OverflowMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            ActionMenuPresenter.this = r3;
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            TooltipCompat.setTooltipText(this, getContentDescription());
            setOnTouchListener(new ForwardingListener(this) { // from class: androidx.appcompat.widget.ActionMenuPresenter.OverflowMenuButton.1
                @Override // androidx.appcompat.widget.ForwardingListener
                public ShowableListMenu getPopup() {
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        return null;
                    }
                    return ActionMenuPresenter.this.mOverflowPopup.getPopup();
                }

                @Override // androidx.appcompat.widget.ForwardingListener
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }

                @Override // androidx.appcompat.widget.ForwardingListener
                public boolean onForwardingStopped() {
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        return false;
                    }
                    ActionMenuPresenter.this.hideOverflowMenu();
                    return true;
                }
            });
        }

        @Override // android.view.View
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }

        @Override // androidx.appcompat.widget.ActionMenuView.ActionMenuChildView
        public boolean needsDividerBefore() {
            return false;
        }

        @Override // androidx.appcompat.widget.ActionMenuView.ActionMenuChildView
        public boolean needsDividerAfter() {
            return false;
        }

        @Override // android.widget.ImageView
        protected boolean setFrame(int l, int t, int r, int b) {
            boolean changed = super.setFrame(l, t, r, b);
            Drawable d = getDrawable();
            Drawable bg = getBackground();
            if (!(d == null || bg == null)) {
                int width = getWidth();
                int height = getHeight();
                int halfEdge = Math.max(width, height) / 2;
                int offsetX = getPaddingLeft() - getPaddingRight();
                int offsetY = getPaddingTop() - getPaddingBottom();
                int centerX = (width + offsetX) / 2;
                int centerY = (height + offsetY) / 2;
                DrawableCompat.setHotspotBounds(bg, centerX - halfEdge, centerY - halfEdge, centerX + halfEdge, centerY + halfEdge);
            }
            return changed;
        }
    }

    /* loaded from: classes2.dex */
    public class OverflowPopup extends MenuPopupHelper {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OverflowPopup(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, R.attr.actionOverflowMenuStyle);
            ActionMenuPresenter.this = r7;
            setGravity(GravityCompat.END);
            setPresenterCallback(r7.mPopupPresenterCallback);
        }

        @Override // androidx.appcompat.view.menu.MenuPopupHelper
        public void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }

    /* loaded from: classes2.dex */
    public class ActionButtonSubmenu extends MenuPopupHelper {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ActionButtonSubmenu(Context context, SubMenuBuilder subMenu, View anchorView) {
            super(context, subMenu, anchorView, false, R.attr.actionOverflowMenuStyle);
            ActionMenuPresenter.this = r7;
            MenuItemImpl item = (MenuItemImpl) subMenu.getItem();
            if (!item.isActionButton()) {
                setAnchorView(r7.mOverflowButton == null ? (View) r7.mMenuView : r7.mOverflowButton);
            }
            setPresenterCallback(r7.mPopupPresenterCallback);
        }

        @Override // androidx.appcompat.view.menu.MenuPopupHelper
        public void onDismiss() {
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            actionMenuPresenter.mActionButtonPopup = null;
            actionMenuPresenter.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    /* loaded from: classes2.dex */
    public class PopupPresenterCallback implements MenuPresenter.Callback {
        PopupPresenterCallback() {
            ActionMenuPresenter.this = r1;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) subMenu).getItem().getItemId();
            MenuPresenter.Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                return cb.onOpenSubMenu(subMenu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof SubMenuBuilder) {
                menu.getRootMenu().close(false);
            }
            MenuPresenter.Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class OpenOverflowRunnable implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup popup) {
            ActionMenuPresenter.this = r1;
            this.mPopup = popup;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            View menuView = (View) ActionMenuPresenter.this.mMenuView;
            if (!(menuView == null || menuView.getWindowToken() == null || !this.mPopup.tryShow())) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    /* loaded from: classes2.dex */
    private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
        ActionMenuPopupCallback() {
            ActionMenuPresenter.this = r1;
        }

        @Override // androidx.appcompat.view.menu.ActionMenuItemView.PopupCallback
        public ShowableListMenu getPopup() {
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                return ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            return null;
        }
    }
}
