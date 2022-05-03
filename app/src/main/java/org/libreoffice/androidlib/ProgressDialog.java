package org.libreoffice.androidlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/* loaded from: classes5.dex */
public class ProgressDialog {
    Activity mActivity;
    ProgressBar mDeterminateProgress;
    ProgressBar mIndeterminateProgress;
    TextView mTextView;
    AlertDialog mProgressDialog = null;
    int mProgress = 0;

    public ProgressDialog(Activity activity) {
        this.mActivity = activity;
    }

    private void create() {
        if (this.mProgressDialog == null) {
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            View loadingView = inflater.inflate(R.layout.lolib_dialog_loading, (ViewGroup) null);
            this.mTextView = (TextView) loadingView.findViewById(R.id.progress_dialog_text);
            this.mIndeterminateProgress = (ProgressBar) loadingView.findViewById(R.id.progress_indeterminate);
            this.mDeterminateProgress = (ProgressBar) loadingView.findViewById(R.id.progress_determinate);
            this.mProgressDialog = new AlertDialog.Builder(this.mActivity).setView(loadingView).setCancelable(false).create();
        }
    }

    public void indeterminate(int messageId) {
        create();
        this.mIndeterminateProgress.setVisibility(0);
        this.mDeterminateProgress.setVisibility(4);
        this.mTextView.setText(this.mActivity.getText(messageId));
        this.mProgressDialog.show();
    }

    public void determinate(int messageId) {
        create();
        this.mIndeterminateProgress.setVisibility(4);
        this.mDeterminateProgress.setVisibility(0);
        this.mTextView.setText(this.mActivity.getText(messageId));
        this.mProgress = 0;
        this.mDeterminateProgress.setProgress(this.mProgress);
        this.mProgressDialog.show();
    }

    public void determinateProgress(int progress) {
        if (this.mProgressDialog != null && this.mProgress <= progress) {
            this.mProgress = progress;
            this.mDeterminateProgress.setProgress(this.mProgress);
        }
    }

    public void dismiss() {
        AlertDialog alertDialog = this.mProgressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mProgressDialog = null;
        }
    }
}
