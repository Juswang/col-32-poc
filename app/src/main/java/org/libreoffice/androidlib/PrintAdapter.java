package org.libreoffice.androidlib;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/* loaded from: classes6.dex */
public class PrintAdapter extends PrintDocumentAdapter {
    private LOActivity mainActivity;
    private File printDocFile;

    public PrintAdapter(LOActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override // android.print.PrintDocumentAdapter
    public void onStart() {
        super.onStart();
        this.printDocFile = new File(this.mainActivity.getCacheDir(), "print.pdf");
        this.mainActivity.saveAs(this.printDocFile.toURI().toString(), "pdf", null);
    }

    @Override // android.print.PrintDocumentAdapter
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("finalPrint.pdf");
        builder.setContentType(0).setPageCount(-1).build();
        callback.onLayoutFinished(builder.build(), !newAttributes.equals(oldAttributes));
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0054 -> B:20:0x0071). Please submit an issue!!! */
    @Override // android.print.PrintDocumentAdapter
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
        try {
            try {
                try {
                    InputStream in = new FileInputStream(this.printDocFile);
                    OutputStream out = new FileOutputStream(destination.getFileDescriptor());
                    byte[] buf = new byte[16384];
                    while (true) {
                        int size = in.read(buf);
                        if (size < 0 || cancellationSignal.isCanceled()) {
                            break;
                        }
                        out.write(buf, 0, size);
                    }
                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                    } else {
                        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    }
                    ((InputStream) Objects.requireNonNull(in)).close();
                    ((OutputStream) Objects.requireNonNull(out)).close();
                } catch (IOException e) {
                    e.printStackTrace();
                    ((InputStream) Objects.requireNonNull(null)).close();
                    ((OutputStream) Objects.requireNonNull(null)).close();
                }
            } catch (IOException | NullPointerException e2) {
                while (true) {
                    e2.printStackTrace();
                    return;
                }
            }
        } catch (Throwable th) {
            try {
                ((InputStream) Objects.requireNonNull(null)).close();
                ((OutputStream) Objects.requireNonNull(null)).close();
            } catch (IOException | NullPointerException e3) {
                e3.printStackTrace();
            }
            throw th;
        }
    }
}
