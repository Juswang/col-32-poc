package org.libreoffice.androidapp.ui;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.libreoffice.androidapp.debug.R;

/* loaded from: classes.dex */
public class RecentFilesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final long KB = 1024;
    private final long MB = 1048576;
    private LibreOfficeUIActivity mActivity;
    private ArrayList<RecentFile> recentFiles;

    public RecentFilesAdapter(LibreOfficeUIActivity activity, List<Uri> recentUris) {
        this.mActivity = activity;
        initRecentFiles(recentUris);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(this.mActivity.isViewModeList() ? R.layout.file_list_item : R.layout.file_explorer_grid_item, parent, false);
        return new ViewHolder(item);
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0016 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void initRecentFiles(java.util.List<android.net.Uri> r17) {
        /*
            r16 = this;
            r6 = r16
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6.recentFiles = r0
            r0 = 0
            java.lang.String r1 = ""
            java.util.Iterator r7 = r17.iterator()
            r8 = r0
            r9 = r1
        L12:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L5b
            java.lang.Object r0 = r7.next()
            r10 = r0
            android.net.Uri r10 = (android.net.Uri) r10
            org.libreoffice.androidapp.ui.LibreOfficeUIActivity r0 = r6.mActivity
            java.lang.String r11 = getUriFilename(r0, r10)
            if (r11 == 0) goto L58
            org.libreoffice.androidapp.ui.LibreOfficeUIActivity r0 = r6.mActivity
            long r12 = getUriFileLength(r0, r10)
            java.util.ArrayList<org.libreoffice.androidapp.ui.RecentFilesAdapter$RecentFile> r14 = r6.recentFiles
            org.libreoffice.androidapp.ui.RecentFilesAdapter$RecentFile r15 = new org.libreoffice.androidapp.ui.RecentFilesAdapter$RecentFile
            r0 = r15
            r1 = r16
            r2 = r10
            r3 = r11
            r4 = r12
            r0.<init>(r2, r3, r4)
            r14.add(r15)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r10.toString()
            r0.append(r1)
            java.lang.String r1 = "\n"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r9.concat(r0)
            r9 = r0
            goto L5a
        L58:
            r0 = 1
            r8 = r0
        L5a:
            goto L12
        L5b:
            if (r8 == 0) goto L72
            org.libreoffice.androidapp.ui.LibreOfficeUIActivity r0 = r6.mActivity
            android.content.SharedPreferences r0 = r0.getPrefs()
            android.content.SharedPreferences$Editor r0 = r0.edit()
            org.libreoffice.androidapp.ui.LibreOfficeUIActivity r1 = r6.mActivity
            java.lang.String r1 = "RECENT_DOCUMENTS_LIST"
            android.content.SharedPreferences$Editor r0 = r0.putString(r1, r9)
            r0.apply()
        L72:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.libreoffice.androidapp.ui.RecentFilesAdapter.initRecentFiles(java.util.List):void");
    }

    public static String getUriFilename(Activity activity, Uri uri) {
        String filename = "";
        Cursor cursor = null;
        try {
            cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                filename = cursor.getString(cursor.getColumnIndex("_display_name"));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (filename.isEmpty()) {
                return null;
            }
            return filename;
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static long getUriFileLength(Activity activity, Uri uri) {
        long length = 0;
        Cursor cursor = null;
        try {
            cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                length = cursor.getLong(cursor.getColumnIndex("_size"));
            }
            if (cursor != null) {
                cursor.close();
            }
            return length;
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            return 0L;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String size;
        final RecentFile file = this.recentFiles.get(position);
        View.OnClickListener clickListener = new View.OnClickListener() { // from class: org.libreoffice.androidapp.ui.RecentFilesAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RecentFilesAdapter.this.mActivity.open(file.uri);
            }
        };
        holder.filenameView.setOnClickListener(clickListener);
        holder.imageView.setOnClickListener(clickListener);
        holder.fileActionsImageView.setOnClickListener(new View.OnClickListener() { // from class: org.libreoffice.androidapp.ui.RecentFilesAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RecentFilesAdapter.this.mActivity.openContextMenu(view, file.uri);
            }
        });
        String filename = file.filename;
        long length = file.fileLength;
        holder.filenameView.setText(filename);
        int compoundDrawableInt = 0;
        int type = FileUtilities.getType(filename);
        if (type == 0) {
            compoundDrawableInt = R.drawable.writer;
        } else if (type == 1) {
            compoundDrawableInt = R.drawable.calc;
        } else if (type == 2) {
            compoundDrawableInt = R.drawable.impress;
        } else if (type == 3) {
            compoundDrawableInt = R.drawable.draw;
        } else if (type == 4) {
            compoundDrawableInt = R.drawable.pdf;
        }
        if (compoundDrawableInt != 0) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(this.mActivity, compoundDrawableInt));
        }
        if (this.mActivity.isViewModeList()) {
            String unit = "B";
            if (length < 1024) {
                size = Long.toString(length);
            } else if (length < 1048576) {
                size = Long.toString(length / 1024);
                unit = "KB";
            } else {
                size = Long.toString(length / 1048576);
                unit = "MB";
            }
            holder.fileSizeView.setText(size);
            holder.fileSizeUnitView.setText(unit);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.recentFiles.size() == 0) {
            this.mActivity.noRecentItemsTextView.setVisibility(0);
        } else {
            this.mActivity.noRecentItemsTextView.setVisibility(8);
        }
        return this.recentFiles.size();
    }

    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileActionsImageView;
        TextView fileSizeUnitView;
        TextView fileSizeView;
        TextView filenameView;
        ImageView imageView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ViewHolder(View itemView) {
            super(itemView);
            RecentFilesAdapter.this = this$0;
            this.filenameView = (TextView) itemView.findViewById(R.id.file_item_name);
            this.imageView = (ImageView) itemView.findViewById(R.id.file_item_icon);
            this.fileActionsImageView = (ImageView) itemView.findViewById(R.id.file_actions_button);
            if (this$0.mActivity.isViewModeList()) {
                this.fileSizeView = (TextView) itemView.findViewById(R.id.file_item_size);
                this.fileSizeUnitView = (TextView) itemView.findViewById(R.id.file_item_size_unit);
            }
        }
    }

    /* loaded from: classes.dex */
    public class RecentFile {
        public long fileLength;
        public String filename;
        public Uri uri;

        public RecentFile(Uri uri, String filename, long fileLength) {
            RecentFilesAdapter.this = r1;
            this.uri = uri;
            this.filename = filename;
            this.fileLength = fileLength;
        }
    }
}
