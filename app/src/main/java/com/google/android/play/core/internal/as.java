package com.google.android.play.core.internal;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitcompat.c;
import com.google.android.play.core.splitcompat.p;
import com.google.android.play.core.splitinstall.d;
import com.google.android.play.core.splitinstall.f;
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class as implements f {
    private final Context a;
    private final c b;
    private final at c;
    private final Executor d;
    private final p e;

    public as(Context context, Executor executor, at atVar, c cVar, p pVar) {
        this.a = context;
        this.b = cVar;
        this.c = atVar;
        this.d = executor;
        this.e = pVar;
    }

    @Nullable
    @SplitInstallErrorCode
    private final Integer a(List<Intent> list) {
        FileLock fileLock;
        try {
            FileChannel channel = new RandomAccessFile(this.b.b(), "rw").getChannel();
            Integer num = null;
            try {
                fileLock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                fileLock = null;
            }
            if (fileLock != null) {
                int i = -11;
                try {
                    Log.i("SplitCompat", "Copying splits.");
                    for (Intent intent : list) {
                        String stringExtra = intent.getStringExtra("split_id");
                        AssetFileDescriptor openAssetFileDescriptor = this.a.getContentResolver().openAssetFileDescriptor(intent.getData(), "r");
                        File a = this.b.a(stringExtra);
                        if ((!a.exists() || a.length() == openAssetFileDescriptor.getLength()) && a.exists()) {
                        }
                        if (!this.b.b(stringExtra).exists()) {
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(openAssetFileDescriptor.createInputStream());
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(a);
                                byte[] bArr = new byte[4096];
                                while (true) {
                                    int read = bufferedInputStream.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                fileOutputStream.close();
                                bufferedInputStream.close();
                            } catch (Throwable th) {
                                try {
                                    bufferedInputStream.close();
                                } catch (Throwable th2) {
                                    ci.a(th, th2);
                                }
                                throw th;
                            }
                        } else {
                            continue;
                        }
                    }
                    Log.i("SplitCompat", "Splits copied.");
                    try {
                        if (!this.c.a()) {
                            Log.e("SplitCompat", "Split verification failed.");
                        } else {
                            Log.i("SplitCompat", "Splits verified.");
                            i = 0;
                        }
                    } catch (Exception e2) {
                        Log.e("SplitCompat", "Error verifying splits.", e2);
                    }
                } catch (Exception e3) {
                    Log.e("SplitCompat", "Error copying splits.", e3);
                    i = -13;
                }
                num = Integer.valueOf(i);
                fileLock.release();
            }
            if (channel != null) {
                channel.close();
            }
            return num;
        } catch (Exception e4) {
            Log.e("SplitCompat", "Error locking files.", e4);
            return -13;
        }
    }

    public static /* synthetic */ void a(as asVar, d dVar) {
        try {
            if (!SplitCompat.a(p.a(asVar.a))) {
                Log.e("SplitCompat", "Emulating splits failed.");
                dVar.a(-12);
                return;
            }
            Log.i("SplitCompat", "Splits installed.");
            dVar.a();
        } catch (Exception e) {
            Log.e("SplitCompat", "Error emulating splits.", e);
            dVar.a(-12);
        }
    }

    public static /* synthetic */ void a(as asVar, List list, d dVar) {
        Integer a = asVar.a(list);
        if (a != null) {
            if (a.intValue() == 0) {
                dVar.b();
            } else {
                dVar.a(a.intValue());
            }
        }
    }

    @Override // com.google.android.play.core.splitinstall.f
    public final void a(List<Intent> list, d dVar) {
        if (SplitCompat.a()) {
            this.d.execute(new ar(this, list, dVar));
            return;
        }
        throw new IllegalStateException("Ingestion should only be called in SplitCompat mode.");
    }
}
