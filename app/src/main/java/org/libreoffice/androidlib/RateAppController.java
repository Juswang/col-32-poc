package org.libreoffice.androidlib;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes3.dex */
public class RateAppController {
    private LOActivity mActivity;
    private static String RATE_ASK_COUNTER_KEY = "RATE_ASK_COUNTER";
    private static String RATE_COUNTER_LAST_UPDATE_KEY = "RATE_COUNTER_LAST_UPDATE_DATE";
    private static String RATE_ALREADY_RATED_KEY = "RATE_ALREADY_RATED";

    RateAppController(LOActivity activity) {
        this.mActivity = activity;
    }

    public void openInGooglePlay() {
        String marketUri = String.format("market://details?id=%1$s", this.mActivity.getPackageName());
        String webUri = String.format("https://play.google.com/store/apps/details?id=%1$s", this.mActivity.getPackageName());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(marketUri));
        if (this.mActivity.getPackageManager().queryIntentActivities(intent, 0).size() <= 0) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse(webUri));
            if (this.mActivity.getPackageManager().queryIntentActivities(intent, 0).size() <= 0) {
                intent = null;
            }
        }
        if (intent != null) {
            this.mActivity.getPrefs().edit().putBoolean(RATE_ALREADY_RATED_KEY, true).apply();
            this.mActivity.startActivity(intent);
        }
    }

    public void askUserForRating() {
        if (shouldAsk()) {
            View rateAppLayout = this.mActivity.getLayoutInflater().inflate(R.layout.rate_app_layout, (ViewGroup) null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
            builder.setView(rateAppLayout).setTitle(String.format(this.mActivity.getString(R.string.rate_our_app_title), this.mActivity.getString(R.string.app_name))).setPositiveButton(R.string.rate_now, new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidlib.RateAppController.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    RateAppController.this.openInGooglePlay();
                }
            }).setNegativeButton(R.string.later, (DialogInterface.OnClickListener) null);
            final AlertDialog alertDialog = builder.create();
            RatingBar ratingBar = (RatingBar) rateAppLayout.findViewById(R.id.ratingBar);
            ratingBar.setOnTouchListener(new View.OnTouchListener() { // from class: org.libreoffice.androidlib.RateAppController.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == 1) {
                        RateAppController.this.openInGooglePlay();
                        alertDialog.dismiss();
                    }
                    return true;
                }
            });
            alertDialog.show();
        }
    }

    private boolean shouldAsk() {
        if (this.mActivity.getPrefs().getBoolean(RATE_ALREADY_RATED_KEY, false)) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Date today = new Date();
        long lastDate = this.mActivity.getPrefs().getLong(RATE_COUNTER_LAST_UPDATE_KEY, 0L);
        if (dateFormat.format(today).equals(dateFormat.format(Long.valueOf(lastDate)))) {
            return false;
        }
        boolean ret = false;
        int counter = this.mActivity.getPrefs().getInt(RATE_ASK_COUNTER_KEY, 0);
        if (counter == 4) {
            ret = true;
        }
        this.mActivity.getPrefs().edit().putInt(RATE_ASK_COUNTER_KEY, (counter + 1) % 5).putLong(RATE_COUNTER_LAST_UPDATE_KEY, today.getTime()).apply();
        return ret;
    }
}
