package org.libreoffice.androidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import org.libreoffice.androidapp.debug.R;

/* loaded from: classes.dex */
public class AboutDialogFragment extends DialogFragment {
    private static final String DEFAULT_DOC_PATH = "/assets/example.odt";

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String versionName;
        String onlineVersionHash;
        String coreVersionHash;
        View messageView = getActivity().getLayoutInflater().inflate(R.layout.about, (ViewGroup) null, false);
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);
        textView.setText(getResources().getString(R.string.info_url));
        TextView versionView = (TextView) messageView.findViewById(R.id.about_version);
        try {
            versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            onlineVersionHash = getString(R.string.online_version_hash);
            coreVersionHash = getString(R.string.core_version_hash);
        } catch (PackageManager.NameNotFoundException e) {
            versionView.setText("");
        }
        if (onlineVersionHash.isEmpty() || coreVersionHash.isEmpty() || versionName.isEmpty()) {
            throw new PackageManager.NameNotFoundException();
        }
        String replace = versionView.getText().toString().replace("\n", "<br/>");
        String version = String.format(replace, versionName, "<a href=\"https://github.com/CollaboraOnline/online/commits/" + onlineVersionHash + "\">" + onlineVersionHash + "</a>", "<a href=\"https://hub.libreoffice.org/git-core/" + coreVersionHash + "\">" + coreVersionHash + "</a>");
        Spanned versionString = Html.fromHtml(version);
        versionView.setText(versionString);
        versionView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView descriptionView = (TextView) messageView.findViewById(R.id.about_description);
        String description = descriptionView.getText().toString();
        descriptionView.setText(description.replace("$APP_NAME", getResources().getString(R.string.app_name)));
        TextView vendorView = (TextView) messageView.findViewById(R.id.about_vendor);
        String vendor = vendorView.getText().toString();
        vendorView.setText(vendor.replace("$VENDOR", getResources().getString(R.string.vendor)));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        getResources().getIdentifier("ic_launcher_brand", "drawable", getActivity().getPackageName());
        builder.setIcon(R.drawable.lo_icon).setTitle(R.string.app_name).setView(messageView).setNegativeButton(R.string.about_license, new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.AboutDialogFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(AboutDialogFragment.this.getContext(), ShowHTMLActivity.class);
                intent.putExtra("path", "license.html");
                AboutDialogFragment.this.startActivity(intent);
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.about_notice, new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.AboutDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(AboutDialogFragment.this.getContext(), ShowHTMLActivity.class);
                intent.putExtra("path", "notice.txt");
                AboutDialogFragment.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
