package androidx.preference;

import android.content.Context;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public final class CollapsiblePreferenceGroupController {
    private final Context mContext;
    private boolean mHasExpandablePreference = false;
    final PreferenceGroupAdapter mPreferenceGroupAdapter;

    public CollapsiblePreferenceGroupController(PreferenceGroup preferenceGroup, PreferenceGroupAdapter preferenceGroupAdapter) {
        this.mPreferenceGroupAdapter = preferenceGroupAdapter;
        this.mContext = preferenceGroup.getContext();
    }

    public List<Preference> createVisiblePreferencesList(PreferenceGroup group) {
        return createInnerVisiblePreferencesList(group);
    }

    private List<Preference> createInnerVisiblePreferencesList(PreferenceGroup group) {
        boolean hasExpandablePreference = false;
        this.mHasExpandablePreference = false;
        int visiblePreferenceCount = 0;
        if (group.getInitialExpandedChildrenCount() != Integer.MAX_VALUE) {
            hasExpandablePreference = true;
        }
        List<Preference> visiblePreferences = new ArrayList<>();
        List<Preference> collapsedPreferences = new ArrayList<>();
        int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            Preference preference = group.getPreference(i);
            if (preference.isVisible()) {
                if (!hasExpandablePreference || visiblePreferenceCount < group.getInitialExpandedChildrenCount()) {
                    visiblePreferences.add(preference);
                } else {
                    collapsedPreferences.add(preference);
                }
                if (!(preference instanceof PreferenceGroup)) {
                    visiblePreferenceCount++;
                } else {
                    PreferenceGroup innerGroup = (PreferenceGroup) preference;
                    if (!innerGroup.isOnSameScreenAsChildren()) {
                        continue;
                    } else {
                        List<Preference> innerList = createInnerVisiblePreferencesList(innerGroup);
                        if (!hasExpandablePreference || !this.mHasExpandablePreference) {
                            for (Preference inner : innerList) {
                                if (!hasExpandablePreference || visiblePreferenceCount < group.getInitialExpandedChildrenCount()) {
                                    visiblePreferences.add(inner);
                                } else {
                                    collapsedPreferences.add(inner);
                                }
                                visiblePreferenceCount++;
                            }
                        } else {
                            throw new IllegalArgumentException("Nested expand buttons are not supported!");
                        }
                    }
                }
            }
        }
        if (hasExpandablePreference && visiblePreferenceCount > group.getInitialExpandedChildrenCount()) {
            ExpandButton expandButton = createExpandButton(group, collapsedPreferences);
            visiblePreferences.add(expandButton);
        }
        this.mHasExpandablePreference |= hasExpandablePreference;
        return visiblePreferences;
    }

    private ExpandButton createExpandButton(final PreferenceGroup group, List<Preference> collapsedPreferences) {
        ExpandButton preference = new ExpandButton(this.mContext, collapsedPreferences, group.getId());
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: androidx.preference.CollapsiblePreferenceGroupController.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference2) {
                group.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
                CollapsiblePreferenceGroupController.this.mPreferenceGroupAdapter.onPreferenceHierarchyChange(preference2);
                PreferenceGroup.OnExpandButtonClickListener listener = group.getOnExpandButtonClickListener();
                if (listener == null) {
                    return true;
                }
                listener.onExpandButtonClick();
                return true;
            }
        });
        return preference;
    }

    /* loaded from: classes2.dex */
    public static class ExpandButton extends Preference {
        private long mId;

        ExpandButton(Context context, List<Preference> collapsedPreferences, long parentId) {
            super(context);
            initLayout();
            setSummary(collapsedPreferences);
            this.mId = 1000000 + parentId;
        }

        private void initLayout() {
            setLayoutResource(R.layout.expand_button);
            setIcon(R.drawable.ic_arrow_down_24dp);
            setTitle(R.string.expand_button_title);
            setOrder(999);
        }

        private void setSummary(List<Preference> collapsedPreferences) {
            CharSequence summary = null;
            List<PreferenceGroup> parents = new ArrayList<>();
            for (Preference preference : collapsedPreferences) {
                CharSequence title = preference.getTitle();
                if ((preference instanceof PreferenceGroup) && !TextUtils.isEmpty(title)) {
                    parents.add((PreferenceGroup) preference);
                }
                if (parents.contains(preference.getParent())) {
                    if (preference instanceof PreferenceGroup) {
                        parents.add((PreferenceGroup) preference);
                    }
                } else if (!TextUtils.isEmpty(title)) {
                    if (summary == null) {
                        summary = title;
                    } else {
                        summary = getContext().getString(R.string.summary_collapsed_preference_list, summary, title);
                    }
                }
            }
            setSummary(summary);
        }

        @Override // androidx.preference.Preference
        public void onBindViewHolder(PreferenceViewHolder holder) {
            super.onBindViewHolder(holder);
            holder.setDividerAllowedAbove(false);
        }

        @Override // androidx.preference.Preference
        public long getId() {
            return this.mId;
        }
    }
}
