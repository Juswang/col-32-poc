package com.google.android.play.core.assetpacks;

import android.content.Context;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;
import com.google.android.play.core.internal.av;
import com.google.android.play.core.internal.ci;
import com.google.android.play.core.splitcompat.p;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipException;

/* loaded from: classes2.dex */
public final class db {
    private static a a;

    static int a(byte[] bArr, int i) {
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    @Nullable
    public static AssetLocation a(String str, String str2) throws IOException {
        Long l;
        int c;
        av.a(str != null, "Attempted to get file location from a null apk path.");
        av.a(str2 != null, String.format("Attempted to get file location in apk %s with a null file path.", str));
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "r");
        byte[] bArr = new byte[22];
        randomAccessFile.seek(randomAccessFile.length() - 22);
        randomAccessFile.readFully(bArr);
        bj a2 = a(bArr, 0) == 1347093766 ? a(bArr) : null;
        byte b = 5;
        if (a2 == null) {
            long length = randomAccessFile.length() - 22;
            long j = (-65536) + length;
            if (j < 0) {
                j = 0;
            }
            int min = (int) Math.min(1024L, randomAccessFile.length());
            byte[] bArr2 = new byte[min];
            byte[] bArr3 = new byte[22];
            loop0: while (true) {
                long max = Math.max(3 + (length - min), j);
                randomAccessFile.seek(max);
                randomAccessFile.readFully(bArr2);
                for (int i = min - 4; i >= 0; i -= 4) {
                    byte b2 = bArr2[i];
                    int i2 = b2 != b ? b2 != 6 ? b2 != 75 ? b2 != 80 ? -1 : 0 : 1 : 3 : 2;
                    if (i2 >= 0 && i >= i2 && a(bArr2, i - i2) == 1347093766) {
                        randomAccessFile.seek((max + i) - i2);
                        randomAccessFile.readFully(bArr3);
                        a2 = a(bArr3);
                        break loop0;
                    }
                    b = 5;
                }
                if (max != j) {
                    length = max;
                } else {
                    throw new ZipException(String.format("End Of Central Directory signature not found in APK %s", str));
                }
            }
        }
        long j2 = a2.a;
        byte[] bytes = str2.getBytes("UTF-8");
        byte[] bArr4 = new byte[46];
        byte[] bArr5 = new byte[str2.length()];
        int i3 = 0;
        while (true) {
            if (i3 >= a2.b) {
                l = null;
                break;
            }
            randomAccessFile.seek(j2);
            randomAccessFile.readFully(bArr4);
            int a3 = a(bArr4, 0);
            if (a3 == 1347092738) {
                randomAccessFile.seek(j2 + 28);
                if (c(bArr4, 28) == str2.length()) {
                    randomAccessFile.seek(46 + j2);
                    randomAccessFile.read(bArr5);
                    if (Arrays.equals(bArr5, bytes)) {
                        l = Long.valueOf(b(bArr4, 42));
                        break;
                    }
                }
                j2 += c + 46 + c(bArr4, 30) + c(bArr4, 32);
                i3++;
            } else {
                throw new ZipException(String.format("Missing central directory file header signature when looking for file %s in APK %s. Read %d entries out of %d. Found %d instead of the header signature %d.", str2, str, Integer.valueOf(i3), Integer.valueOf(a2.b), Integer.valueOf(a3), 1347092738));
            }
        }
        if (l == null) {
            return null;
        }
        long longValue = l.longValue();
        byte[] bArr6 = new byte[8];
        randomAccessFile.seek(22 + longValue);
        randomAccessFile.readFully(bArr6);
        return AssetLocation.a(str, longValue + 30 + c(bArr6, 4) + c(bArr6, 6), b(bArr6, 0));
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (db.class) {
            if (a == null) {
                bq bqVar = new bq(null);
                bqVar.a(new n(p.a(context)));
                a = bqVar.a();
            }
            aVar = a;
        }
        return aVar;
    }

    private static bj a(byte[] bArr) {
        int c = c(bArr, 10);
        b(bArr, 12);
        return new bj(b(bArr, 16), c);
    }

    public static String a(List<File> list) throws NoSuchAlgorithmException, IOException {
        int read;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
        byte[] bArr = new byte[8192];
        for (File file : list) {
            FileInputStream fileInputStream = new FileInputStream(file);
            do {
                try {
                    read = fileInputStream.read(bArr);
                    if (read > 0) {
                        messageDigest.update(bArr, 0, read);
                    }
                } catch (Throwable th) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th2) {
                        ci.a(th, th2);
                    }
                    throw th;
                }
            } while (read != -1);
            fileInputStream.close();
        }
        return Base64.encodeToString(messageDigest.digest(), 11);
    }

    public static boolean a(@AssetPackStatus int i) {
        return i == 1 || i == 7 || i == 2 || i == 3;
    }

    public static boolean a(@AssetPackStatus int i, @AssetPackStatus int i2) {
        if (i == 5) {
            if (i2 != 5) {
                return true;
            }
            i = 5;
        }
        if (i == 6 && i2 != 6 && i2 != 5) {
            return true;
        }
        if (i == 4 && i2 != 4) {
            return true;
        }
        if (i == 3 && (i2 == 2 || i2 == 7 || i2 == 1 || i2 == 8)) {
            return true;
        }
        if (i == 2) {
            return i2 == 1 || i2 == 8;
        }
        return false;
    }

    public static long b(byte[] bArr, int i) {
        return ((c(bArr, i + 2) << 16) | c(bArr, i)) & 4294967295L;
    }

    public static boolean b(@AssetPackStatus int i) {
        return i == 5 || i == 6 || i == 4;
    }

    public static int c(byte[] bArr, int i) {
        return ((bArr[i + 1] & 255) << 8) | (bArr[i] & 255);
    }

    public static boolean c(@AssetPackStatus int i) {
        return i == 2 || i == 7 || i == 3;
    }
}
