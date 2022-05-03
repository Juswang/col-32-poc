package org.libreoffice.androidapp.ui;

import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class FileUtilities {
    static final int ALL = -1;
    static final int CALC = 1;
    public static final String DEFAULT_DRAWING_EXTENSION = ".odg";
    public static final String DEFAULT_IMPRESS_EXTENSION = ".odp";
    public static final String DEFAULT_SPREADSHEET_EXTENSION = ".ods";
    public static final String DEFAULT_WRITER_EXTENSION = ".odt";
    static final int DOC = 0;
    static final int DRAWING = 3;
    static final int IMPRESS = 2;
    static final int PDF = 4;
    static final int SORT_AZ = 0;
    static final int SORT_LARGEST = 4;
    static final int SORT_NEWEST = 3;
    static final int SORT_OLDEST = 2;
    static final int SORT_SMALLEST = 5;
    static final int SORT_ZA = 1;
    static final int UNKNOWN = 10;
    private static String LOGTAG = FileUtilities.class.getSimpleName();
    private static final Map<String, Integer> mExtnMap = new HashMap();
    private static final Map<String, String> extensionToMimeTypeMap = new HashMap();

    static {
        mExtnMap.put(DEFAULT_WRITER_EXTENSION, 0);
        mExtnMap.put(DEFAULT_DRAWING_EXTENSION, 3);
        mExtnMap.put(DEFAULT_IMPRESS_EXTENSION, 2);
        mExtnMap.put(DEFAULT_SPREADSHEET_EXTENSION, 1);
        mExtnMap.put(".fodt", 0);
        mExtnMap.put(".fodg", 3);
        mExtnMap.put(".fodp", 2);
        mExtnMap.put(".fods", 1);
        mExtnMap.put(".pdf", 4);
        mExtnMap.put(".ott", 0);
        mExtnMap.put(".otg", 3);
        mExtnMap.put(".otp", 2);
        mExtnMap.put(".ots", 1);
        mExtnMap.put(".rtf", 0);
        mExtnMap.put(".doc", 0);
        mExtnMap.put(".vsd", 3);
        mExtnMap.put(".vsdx", 3);
        mExtnMap.put(".pub", 3);
        mExtnMap.put(".ppt", 2);
        mExtnMap.put(".xls", 1);
        mExtnMap.put(".xlsb", 1);
        mExtnMap.put(".dot", 0);
        mExtnMap.put(".pot", 2);
        mExtnMap.put(".xlt", 1);
        mExtnMap.put(".docx", 0);
        mExtnMap.put(".pptx", 2);
        mExtnMap.put(".xlsx", 1);
        mExtnMap.put(".dotx", 0);
        mExtnMap.put(".potx", 2);
        mExtnMap.put(".xltx", 1);
        mExtnMap.put(".csv", 1);
        mExtnMap.put(".txt", 0);
        mExtnMap.put(".wps", 0);
        mExtnMap.put(".key", 2);
        mExtnMap.put(".abw", 0);
        mExtnMap.put(".pmd", 3);
        mExtnMap.put(".emf", 3);
        mExtnMap.put(".svm", 3);
        mExtnMap.put(".wmf", 3);
        mExtnMap.put(".svg", 3);
        extensionToMimeTypeMap.put("odb", "application/vnd.oasis.opendocument.database");
        extensionToMimeTypeMap.put("odf", "application/vnd.oasis.opendocument.formula");
        extensionToMimeTypeMap.put("odg", "application/vnd.oasis.opendocument.graphics");
        extensionToMimeTypeMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        extensionToMimeTypeMap.put("odi", "application/vnd.oasis.opendocument.image");
        extensionToMimeTypeMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        extensionToMimeTypeMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        extensionToMimeTypeMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        extensionToMimeTypeMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        extensionToMimeTypeMap.put("odt", "application/vnd.oasis.opendocument.text");
        extensionToMimeTypeMap.put("odm", "application/vnd.oasis.opendocument.text-master");
        extensionToMimeTypeMap.put("ott", "application/vnd.oasis.opendocument.text-template");
        extensionToMimeTypeMap.put("oth", "application/vnd.oasis.opendocument.text-web");
        extensionToMimeTypeMap.put("txt", "text/plain");
    }

    public static String getExtension(String filename) {
        int nExt;
        if (filename != null && (nExt = filename.lastIndexOf(46)) >= 0) {
            return filename.substring(nExt);
        }
        return "";
    }

    public static int lookupExtension(String filename) {
        String extn = getExtension(filename);
        if (!mExtnMap.containsKey(extn)) {
            return 10;
        }
        return mExtnMap.get(extn).intValue();
    }

    public static int getType(String filename) {
        int type = lookupExtension(filename);
        String str = LOGTAG;
        Log.d(str, "extn : " + filename + " -> " + type);
        return type;
    }

    static String getMimeType(String filename) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filename);
        String mime = extensionToMimeTypeMap.get(extension);
        if (mime == null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mime;
    }

    public static boolean doAccept(String filename, int byMode, String byFilename) {
        String str = LOGTAG;
        Log.d(str, "doAccept : " + filename + " mode " + byMode + " byFilename " + byFilename);
        if (filename == null) {
            return false;
        }
        if (byMode != -1 && mExtnMap.get(getExtension(filename)).intValue() != byMode) {
            return false;
        }
        byFilename.equals("");
        return true;
    }

    static FileFilter getFileFilter(final int mode) {
        return new FileFilter() { // from class: org.libreoffice.androidapp.ui.FileUtilities.1
            @Override // java.io.FileFilter
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                if (FileUtilities.lookupExtension(pathname.getName()) == 10) {
                    return false;
                }
                return FileUtilities.doAccept(pathname.getName(), mode, "");
            }
        };
    }

    static FilenameFilter getFilenameFilter(final int mode) {
        return new FilenameFilter() { // from class: org.libreoffice.androidapp.ui.FileUtilities.2
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                if (new File(dir, filename).isDirectory()) {
                    return true;
                }
                return FileUtilities.doAccept(filename, mode, "");
            }
        };
    }

    static boolean isHidden(File file) {
        return file.getName().startsWith(".");
    }

    public static boolean isThumbnail(File file) {
        return isHidden(file) && file.getName().endsWith(".png");
    }

    static boolean hasThumbnail(File file) {
        String filename = file.getName();
        if (lookupExtension(filename) == 0) {
            return new File(file.getParent(), getThumbnailName(file)).isFile();
        }
        return true;
    }

    static String getThumbnailName(File file) {
        return "." + file.getName().split("[.]")[0] + ".png";
    }
}
