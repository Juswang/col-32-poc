package org.libreoffice.androidlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.libreoffice.androidlib.lok.LokClipboardData;

/* loaded from: classes4.dex */
public class LOActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String ASSETS_EXTRACTED_GIT_COMMIT = "ASSETS_EXTRACTED_GIT_COMMIT";
    private static final String CLIPBOARD_COOL_SIGNATURE = "cool-clip-magic-4a22437e49a8-";
    private static final String CLIPBOARD_FILE_PATH = "LibreofficeClipboardFile.data";
    public static final String EXPLORER_PREFS_KEY = "EXPLORER_PREFS";
    private static final String KEY_DOCUMENT_URI = "documentUri";
    private static final String KEY_ENABLE_SHOW_DEBUG_INFO = "ENABLE_SHOW_DEBUG_INFO";
    private static final String KEY_INTENT_URI = "intentUri";
    private static final String KEY_IS_EDITABLE = "isEditable";
    private static final String KEY_PROVIDER_ID = "providerID";
    public static final String LO_ACTION_DATA = "LOData";
    public static final String LO_ACTION_EVENT = "LOEvent";
    public static final String LO_ACTIVITY_BROADCAST = "LOActivityBroadcast";
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 777;
    public static final String RECENT_DOCUMENTS_KEY = "RECENT_DOCUMENTS_LIST";
    public static final int REQUEST_COPY = 600;
    public static final int REQUEST_SAVEAS_DOC = 509;
    public static final int REQUEST_SAVEAS_DOCX = 506;
    public static final int REQUEST_SAVEAS_EPUB = 512;
    public static final int REQUEST_SAVEAS_ODP = 504;
    public static final int REQUEST_SAVEAS_ODS = 505;
    public static final int REQUEST_SAVEAS_ODT = 503;
    public static final int REQUEST_SAVEAS_PDF = 501;
    public static final int REQUEST_SAVEAS_PPT = 510;
    public static final int REQUEST_SAVEAS_PPTX = 507;
    public static final int REQUEST_SAVEAS_RTF = 502;
    public static final int REQUEST_SAVEAS_XLS = 511;
    public static final int REQUEST_SAVEAS_XLSX = 508;
    public static final int REQUEST_SELECT_IMAGE_FILE = 500;
    static final String TAG = "LOActivity";
    private static String USER_NAME_KEY = "USER_NAME";
    private ClipData clipData;
    private ClipboardManager clipboardManager;
    @Nullable
    private URI documentUri;
    private Activity mActivity;
    private Handler nativeHandler;
    private Looper nativeLooper;
    private Thread nativeMsgThread;
    private int providerId;
    private RateAppController rateAppController;
    private SharedPreferences sPrefs;
    private Bundle savedInstanceState;
    private String urlToLoad;
    private ValueCallback<Uri[]> valueCallback;
    private File mTempFile = null;
    private long loadDocumentMillis = 0;
    private COWebView mWebView = null;
    private Handler mMainHandler = null;
    private boolean isDocEditable = false;
    private boolean isDocDebuggable = BuildConfig.DEBUG;
    private boolean documentLoaded = false;
    private ProgressDialog mProgressDialog = null;
    private boolean mMobileWizardVisible = false;
    private boolean mIsEditModeActive = false;

    public native void createCOOLWSD(String str, String str2, String str3, AssetManager assetManager, String str4, String str5, String str6);

    public native boolean getClipboardContent(LokClipboardData lokClipboardData);

    public native void paste(String str, byte[] bArr);

    public native void postMobileMessageNative(String str);

    public native void postUnoCommand(String str, String str2, boolean z);

    public native void saveAs(String str, String str2, String str3);

    public native void setClipboardContent(LokClipboardData lokClipboardData);

    static {
        System.loadLibrary("androidapp");
    }

    public static boolean copyFromAssets(AssetManager assetManager, String fromAssetPath, String targetDir) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            boolean res = true;
            for (String file : files) {
                String[] dirOrFile = assetManager.list(fromAssetPath + "/" + file);
                if (dirOrFile.length == 0) {
                    new File(targetDir).mkdirs();
                    res &= copyAsset(assetManager, fromAssetPath + "/" + file, targetDir + "/" + file);
                } else {
                    res &= copyFromAssets(assetManager, fromAssetPath + "/" + file, targetDir + "/" + file);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "copyFromAssets failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            ReadableByteChannel source = Channels.newChannel(assetManager.open(fromAssetPath));
            FileChannel dest = new FileOutputStream(toPath).getChannel();
            long bytesTransferred = 0;
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (source.read(buffer) > 0) {
                buffer.flip();
                bytesTransferred += dest.write(buffer);
                buffer.clear();
            }
            Log.v(TAG, "Success copying " + fromAssetPath + " to " + toPath + " bytes: " + bytesTransferred);
            if (dest != null) {
                dest.close();
            }
            if (source != null) {
                source.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "file " + fromAssetPath + " not found! " + e.getMessage());
            return false;
        } catch (IOException e2) {
            Log.e(TAG, "failed to copy file " + fromAssetPath + " from assets to " + toPath + " - " + e2.getMessage());
            return false;
        }
    }

    private Handler getMainHandler() {
        if (this.mMainHandler == null) {
            this.mMainHandler = new Handler(getMainLooper());
        }
        return this.mMainHandler;
    }

    public static boolean isChromeOS(Context context) {
        return context.getPackageManager().hasSystemFeature("org.chromium.arc.device_management");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        getWindow().addFlags(128);
        this.sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setContentView(R.layout.lolib_activity_main);
        this.mProgressDialog = new ProgressDialog(this);
        this.rateAppController = null;
        this.mActivity = this;
        init();
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [org.libreoffice.androidlib.LOActivity$1] */
    public void init() {
        if (this.sPrefs.getString(ASSETS_EXTRACTED_GIT_COMMIT, "").equals(BuildConfig.GIT_COMMIT)) {
            initUI();
            return;
        }
        this.mProgressDialog.indeterminate(R.string.preparing_for_the_first_start_after_an_update);
        new AsyncTask<Void, Void, Void>() { // from class: org.libreoffice.androidlib.LOActivity.1
            public Void doInBackground(Void... voids) {
                if (!LOActivity.copyFromAssets(LOActivity.this.getAssets(), "unpack", LOActivity.this.getApplicationInfo().dataDir)) {
                    return null;
                }
                LOActivity.this.sPrefs.edit().putString(LOActivity.ASSETS_EXTRACTED_GIT_COMMIT, BuildConfig.GIT_COMMIT).apply();
                return null;
            }

            public void onPostExecute(Void aVoid) {
                LOActivity.this.initUI();
            }
        }.execute(new Void[0]);
    }

    public void initUI() {
        File file;
        this.isDocDebuggable = this.sPrefs.getBoolean(KEY_ENABLE_SHOW_DEBUG_INFO, false) && BuildConfig.DEBUG;
        if (getIntent().getData() != null) {
            if (getIntent().getData().getScheme().equals("content")) {
                this.isDocEditable = true;
                if ((getIntent().getFlags() & 2) == 0) {
                    this.isDocEditable = false;
                    Log.d(TAG, "Disabled editing: Read-only");
                    Toast.makeText(this, getResources().getString(R.string.temp_file_saving_disabled), 0).show();
                }
                if (this.isDocEditable && (getIntent().getData().toString().startsWith("content://org.chromium.arc.chromecontentprovider/externalfile") || getIntent().getData().toString().startsWith("content://org.chromium.arc.volumeprovider/"))) {
                    this.isDocEditable = false;
                    Log.d(TAG, "Disabled editing: Chrome OS unsupported content providers");
                    Toast.makeText(this, getResources().getString(R.string.file_chromeos_read_only), 1).show();
                }
                if (!copyFileToTemp() || (file = this.mTempFile) == null) {
                    Log.e(TAG, "couldn't create temporary file from " + getIntent().getData());
                    Toast.makeText(this, R.string.cant_open_the_document, 0).show();
                    finish();
                } else {
                    this.documentUri = file.toURI();
                    this.urlToLoad = this.documentUri.toString();
                    Log.d(TAG, "SCHEME_CONTENT: getPath(): " + getIntent().getData().getPath());
                }
            } else if (getIntent().getData().getScheme().equals("file")) {
                this.isDocEditable = true;
                this.urlToLoad = getIntent().getData().toString();
                Log.d(TAG, "SCHEME_FILE: getPath(): " + getIntent().getData().getPath());
                this.providerId = getIntent().getIntExtra("org.libreoffice.document_provider_id", 0);
                this.documentUri = (URI) getIntent().getSerializableExtra("org.libreoffice.document_uri");
            }
        } else if (this.savedInstanceState != null) {
            getIntent().setAction("android.intent.action.VIEW").setData(Uri.parse(this.savedInstanceState.getString(KEY_INTENT_URI)));
            this.urlToLoad = getIntent().getData().toString();
            this.providerId = this.savedInstanceState.getInt(KEY_PROVIDER_ID);
            if (this.savedInstanceState.getString(KEY_DOCUMENT_URI) != null) {
                try {
                    this.documentUri = new URI(this.savedInstanceState.getString(KEY_DOCUMENT_URI));
                    this.urlToLoad = this.documentUri.toString();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            this.isDocEditable = this.savedInstanceState.getBoolean(KEY_IS_EDITABLE);
        } else {
            Toast.makeText(this, getString(R.string.failed_to_load_file), 0).show();
            finish();
        }
        if (!canDocumentBeExported()) {
            this.isDocEditable = false;
        }
        if (this.mTempFile != null) {
            this.mWebView = (COWebView) findViewById(R.id.browser);
            WebSettings webSettings = this.mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            this.mWebView.addJavascriptInterface(this, "COOLMessageHandler");
            boolean isChromeDebugEnabled = this.sPrefs.getBoolean("ENABLE_CHROME_DEBUGGING", false);
            if ((getApplicationInfo().flags & 2) != 0 || isChromeDebugEnabled) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            getMainHandler();
            this.clipboardManager = (ClipboardManager) getSystemService("clipboard");
            this.nativeMsgThread = new Thread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    LOActivity.this.nativeLooper = Looper.myLooper();
                    LOActivity lOActivity = LOActivity.this;
                    lOActivity.nativeHandler = new Handler(lOActivity.nativeLooper);
                    Looper.loop();
                }
            });
            this.nativeMsgThread.start();
            this.mWebView.setWebChromeClient(new WebChromeClient() { // from class: org.libreoffice.androidlib.LOActivity.3
                @Override // android.webkit.WebChromeClient
                public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                    if (LOActivity.this.valueCallback != null) {
                        LOActivity.this.valueCallback.onReceiveValue(null);
                        LOActivity.this.valueCallback = null;
                    }
                    LOActivity.this.valueCallback = filePathCallback;
                    Intent intent = fileChooserParams.createIntent();
                    try {
                        intent.setType("image/*");
                        LOActivity.this.startActivityForResult(intent, LOActivity.REQUEST_SELECT_IMAGE_FILE);
                        return true;
                    } catch (ActivityNotFoundException e2) {
                        LOActivity.this.valueCallback = null;
                        LOActivity lOActivity = LOActivity.this;
                        Toast.makeText(lOActivity, lOActivity.getString(R.string.cannot_open_file_chooser), 1).show();
                        return false;
                    }
                }
            });
            if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                Log.i(TAG, "asking for read storage permission");
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, PERMISSION_WRITE_EXTERNAL_STORAGE);
                return;
            }
            loadDocument();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(final Intent intent) {
        Log.i(TAG, "onNewIntent");
        if (this.documentLoaded) {
            postMobileMessageNative("save dontTerminateEdit=true dontSaveIfUnmodified=true");
        }
        this.mProgressDialog.indeterminate(R.string.exiting);
        getMainHandler().post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.4
            @Override // java.lang.Runnable
            public void run() {
                LOActivity.this.documentLoaded = false;
                LOActivity.this.postMobileMessageNative("BYE");
                LOActivity.this.runOnUiThread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.mProgressDialog.dismiss();
                        LOActivity.this.setIntent(intent);
                        LOActivity.this.init();
                    }
                });
            }
        });
        super.onNewIntent(intent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INTENT_URI, getIntent().getData().toString());
        outState.putInt(KEY_PROVIDER_ID, this.providerId);
        URI uri = this.documentUri;
        if (uri != null) {
            outState.putString(KEY_DOCUMENT_URI, uri.toString());
        }
        outState.putBoolean(KEY_IS_EDITABLE, this.isDocEditable);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_WRITE_EXTERNAL_STORAGE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (permissions.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(this, getString(R.string.storage_permission_required), 0).show();
            finishAndRemoveTask();
        } else {
            loadDocument();
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.libreoffice.androidlib.LOActivity$1CopyThread] */
    private boolean copyFileToTemp() {
        final ContentResolver contentResolver = getContentResolver();
        ?? r1 = new Thread() { // from class: org.libreoffice.androidlib.LOActivity.1CopyThread
            private boolean result = false;

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String suffix = null;
                String intentType = LOActivity.this.mActivity.getIntent().getType();
                if (LOActivity.this.mActivity.getIntent().getType() == null) {
                    intentType = LOActivity.this.getMimeType();
                }
                if ("text/comma-separated-values".equals(intentType) || "text/csv".equals(intentType)) {
                    suffix = ".csv";
                } else if ("application/pdf".equals(intentType)) {
                    suffix = ".pdf";
                } else if ("application/vnd.ms-excel".equals(intentType)) {
                    suffix = ".xls";
                } else if ("application/vnd.ms-powerpoint".equals(intentType)) {
                    suffix = ".ppt";
                }
                try {
                    Uri uri = LOActivity.this.mActivity.getIntent().getData();
                    InputStream inputStream = contentResolver.openInputStream(uri);
                    LOActivity.this.mTempFile = File.createTempFile("LibreOffice", suffix, LOActivity.this.mActivity.getCacheDir());
                    OutputStream outputStream = new FileOutputStream(LOActivity.this.mTempFile);
                    byte[] buffer = new byte[1024];
                    long bytes = 0;
                    while (true) {
                        int length = inputStream.read(buffer);
                        if (length == -1) {
                            break;
                        }
                        outputStream.write(buffer, 0, length);
                        bytes += length;
                    }
                    Log.i(LOActivity.TAG, "Success copying " + bytes + " bytes from " + uri + " to " + LOActivity.this.mTempFile);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    outputStream.close();
                    this.result = true;
                } catch (FileNotFoundException e) {
                    Log.e(LOActivity.TAG, "file not found: " + e.getMessage());
                    this.result = false;
                } catch (IOException e2) {
                    Log.e(LOActivity.TAG, "exception: " + e2.getMessage());
                    this.result = false;
                }
            }
        };
        r1.start();
        try {
            r1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ((C1CopyThread) r1).result;
    }

    private void copyTempBackToIntent() {
        if (this.isDocEditable && this.mTempFile != null && getIntent().getData() != null && getIntent().getData().getScheme().equals("content")) {
            final ContentResolver contentResolver = getContentResolver();
            try {
                Thread copyThread = new Thread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.5
                    @Override // java.lang.Runnable
                    public void run() {
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        try {
                            try {
                                inputStream = new FileInputStream(LOActivity.this.mTempFile);
                                int len = inputStream.available();
                                if (len <= 0) {
                                    inputStream.close();
                                    if (0 != 0) {
                                        outputStream.close();
                                        return;
                                    }
                                    return;
                                }
                                Uri uri = LOActivity.this.getIntent().getData();
                                try {
                                    outputStream = contentResolver.openOutputStream(uri, "wt");
                                } catch (FileNotFoundException e) {
                                    Log.i(LOActivity.TAG, "failed with the 'wt' mode, trying without: " + e.getMessage());
                                    outputStream = contentResolver.openOutputStream(uri);
                                }
                                byte[] buffer = new byte[1024];
                                long bytes = 0;
                                while (true) {
                                    int length = inputStream.read(buffer);
                                    if (length == -1) {
                                        break;
                                    }
                                    outputStream.write(buffer, 0, length);
                                    bytes += length;
                                }
                                Log.i(LOActivity.TAG, "Success copying " + bytes + " bytes from " + LOActivity.this.mTempFile + " to " + uri);
                                inputStream.close();
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (Throwable th) {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                                throw th;
                            }
                        } catch (FileNotFoundException e2) {
                            Log.e(LOActivity.TAG, "file not found: " + e2.getMessage());
                        } catch (Exception e3) {
                            Log.e(LOActivity.TAG, "exception: " + e3.getMessage());
                        }
                    }
                });
                copyThread.start();
                copyThread.join();
            } catch (Exception e) {
                Log.i(TAG, "copyTempBackToIntent: " + e.getMessage());
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume..");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        if (this.documentLoaded) {
            postMobileMessageNative("save dontTerminateEdit=true dontSaveIfUnmodified=true");
        }
        super.onPause();
        Log.d(TAG, "onPause() - hinting to save, we might need to return to the doc");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        if (!this.documentLoaded) {
            super.onDestroy();
            return;
        }
        this.nativeLooper.quit();
        ViewGroup viewGroup = (ViewGroup) this.mWebView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this.mWebView);
        }
        this.mWebView.destroy();
        this.mWebView = null;
        this.documentLoaded = false;
        postMobileMessageNative("BYE");
        this.mProgressDialog.dismiss();
        super.onDestroy();
        Log.i(TAG, "onDestroy() - we know we are leaving the document");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        boolean requestCopy;
        int requestCode2;
        File tempFile;
        byte[] buffer;
        String str = null;
        if (resultCode == -1) {
            if (requestCode != 600) {
                requestCopy = false;
                requestCode2 = requestCode;
            } else if (getMimeType().equals("text/plain")) {
                requestCopy = true;
                requestCode2 = 503;
            } else if (getMimeType().equals("text/comma-separated-values")) {
                requestCopy = true;
                requestCode2 = 505;
            } else if (getMimeType().equals("application/vnd.ms-excel.sheet.binary.macroenabled.12")) {
                requestCopy = true;
                requestCode2 = 505;
            } else {
                String filename = getFileName(true);
                String extension = filename.substring(filename.lastIndexOf(46) + 1);
                requestCopy = true;
                requestCode2 = getRequestIDForFormat(extension);
            }
            switch (requestCode2) {
                case REQUEST_SELECT_IMAGE_FILE /* 500 */:
                    ValueCallback<Uri[]> valueCallback = this.valueCallback;
                    if (valueCallback != null) {
                        valueCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                        this.valueCallback = null;
                        return;
                    }
                    return;
                case REQUEST_SAVEAS_PDF /* 501 */:
                case REQUEST_SAVEAS_RTF /* 502 */:
                case REQUEST_SAVEAS_ODT /* 503 */:
                case REQUEST_SAVEAS_ODP /* 504 */:
                case REQUEST_SAVEAS_ODS /* 505 */:
                case REQUEST_SAVEAS_DOCX /* 506 */:
                case REQUEST_SAVEAS_PPTX /* 507 */:
                case REQUEST_SAVEAS_XLSX /* 508 */:
                case REQUEST_SAVEAS_DOC /* 509 */:
                case REQUEST_SAVEAS_PPT /* 510 */:
                case 511:
                case 512:
                    if (intent != null) {
                        String format = getFormatForRequestCode(requestCode2);
                        File _tempFile = null;
                        if (format != null) {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            try {
                                try {
                                    try {
                                        tempFile = File.createTempFile("LibreOffice", "." + format, getCacheDir());
                                        String uri = tempFile.toURI().toString();
                                        if (requestCopy) {
                                            str = "TakeOwnership";
                                        }
                                        saveAs(uri, format, str);
                                        inputStream = new FileInputStream(tempFile);
                                        try {
                                            outputStream = getContentResolver().openOutputStream(intent.getData(), "wt");
                                        } catch (FileNotFoundException e) {
                                            Log.i(TAG, "failed with the 'wt' mode, trying without: " + e.getMessage());
                                            outputStream = getContentResolver().openOutputStream(intent.getData());
                                        }
                                        buffer = new byte[4096];
                                    } catch (Exception e2) {
                                        Toast.makeText(this, "Something went wrong while Saving as: " + e2.getMessage(), 0).show();
                                        e2.printStackTrace();
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (outputStream != null) {
                                            outputStream.close();
                                        }
                                    }
                                } catch (Throwable th) {
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (Exception e3) {
                                            throw th;
                                        }
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    throw th;
                                }
                            } catch (Exception e4) {
                            }
                            while (true) {
                                int len = inputStream.read(buffer);
                                if (len != -1) {
                                    outputStream.write(buffer, 0, len);
                                } else {
                                    outputStream.flush();
                                    _tempFile = tempFile;
                                    inputStream.close();
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    if (requestCopy) {
                                        this.mTempFile = _tempFile;
                                        getIntent().setData(intent.getData());
                                        addIntentToRecents(intent);
                                        callFakeWebsocketOnMessage("'mobile: readonlymode'");
                                        this.isDocEditable = true;
                                        return;
                                    }
                                    return;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                    break;
            }
            Toast.makeText(this, "Unknown request", 1).show();
        } else if (requestCode == 500) {
            this.valueCallback.onReceiveValue(null);
            this.valueCallback = null;
        }
    }

    private void addIntentToRecents(Intent intent) {
        Uri treeFileUri = intent.getData();
        SharedPreferences recentPrefs = getSharedPreferences("EXPLORER_PREFS", 0);
        String recentList = recentPrefs.getString("RECENT_DOCUMENTS_LIST", "");
        recentPrefs.edit().putString("RECENT_DOCUMENTS_LIST", treeFileUri.toString() + "\n" + recentList).apply();
    }

    private String getFormatForRequestCode(int requestCode) {
        switch (requestCode) {
            case REQUEST_SAVEAS_PDF /* 501 */:
                return "pdf";
            case REQUEST_SAVEAS_RTF /* 502 */:
                return "rtf";
            case REQUEST_SAVEAS_ODT /* 503 */:
                return "odt";
            case REQUEST_SAVEAS_ODP /* 504 */:
                return "odp";
            case REQUEST_SAVEAS_ODS /* 505 */:
                return "ods";
            case REQUEST_SAVEAS_DOCX /* 506 */:
                return "docx";
            case REQUEST_SAVEAS_PPTX /* 507 */:
                return "pptx";
            case REQUEST_SAVEAS_XLSX /* 508 */:
                return "xlsx";
            case REQUEST_SAVEAS_DOC /* 509 */:
                return "doc";
            case REQUEST_SAVEAS_PPT /* 510 */:
                return "ppt";
            case 511:
                return "xls";
            case 512:
                return "epub";
            default:
                return null;
        }
    }

    private void finishWithProgress() {
        if (!this.documentLoaded) {
            finishAndRemoveTask();
            return;
        }
        this.mProgressDialog.indeterminate(R.string.exiting);
        getMainHandler().post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.6
            @Override // java.lang.Runnable
            public void run() {
                LOActivity.this.documentLoaded = false;
                LOActivity.this.postMobileMessageNative("BYE");
                LOActivity.this.runOnUiThread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.mProgressDialog.dismiss();
                    }
                });
                LOActivity.this.finishAndRemoveTask();
            }
        });
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.documentLoaded) {
            finishAndRemoveTask();
        } else if (this.mMobileWizardVisible) {
            callFakeWebsocketOnMessage("'mobile: mobilewizardback'");
        } else if (this.mIsEditModeActive) {
            callFakeWebsocketOnMessage("'mobile: readonlymode'");
        } else {
            finishWithProgress();
        }
    }

    private void loadDocument() {
        String finalUrlToLoad;
        this.mProgressDialog.determinate(R.string.loading);
        ApplicationInfo applicationInfo = getApplicationInfo();
        String dataDir = applicationInfo.dataDir;
        Log.i(TAG, String.format("Initializing LibreOfficeKit, dataDir=%s\n", dataDir));
        String cacheDir = getApplication().getCacheDir().getAbsolutePath();
        String apkFile = getApplication().getPackageResourcePath();
        AssetManager assetManager = getResources().getAssets();
        String uiMode = (!isLargeScreen() || isChromeOS()) ? "classic" : "notebookbar";
        String userName = getPrefs().getString(USER_NAME_KEY, "Guest User");
        createCOOLWSD(dataDir, cacheDir, apkFile, assetManager, this.urlToLoad, uiMode, userName);
        String language = getResources().getConfiguration().locale.toLanguageTag();
        Log.i(TAG, "Loading with language:  " + language);
        String finalUrlToLoad2 = ("file:///android_asset/dist/cool.html?file_path=" + this.urlToLoad + "&closebutton=1") + "&lang=" + language;
        if (this.isDocEditable) {
            finalUrlToLoad = finalUrlToLoad2 + "&permission=edit";
        } else {
            finalUrlToLoad = finalUrlToLoad2 + "&permission=readonly";
        }
        if (this.isDocDebuggable) {
            finalUrlToLoad = finalUrlToLoad + "&debug=true";
        }
        if (isLargeScreen() && !isChromeOS()) {
            finalUrlToLoad = finalUrlToLoad + "&userinterfacemode=notebookbar";
        }
        this.mWebView.loadUrl(finalUrlToLoad);
        this.documentLoaded = true;
        this.loadDocumentMillis = SystemClock.uptimeMillis();
    }

    public boolean isLargeScreen() {
        return getResources().getBoolean(R.bool.isLargeScreen);
    }

    public SharedPreferences getPrefs() {
        return this.sPrefs;
    }

    @JavascriptInterface
    public void postMobileMessage(String message) {
        Log.d(TAG, "postMobileMessage: " + message);
        String[] messageAndParameterArray = message.split(" ", 2);
        if (beforeMessageFromWebView(messageAndParameterArray)) {
            postMobileMessageNative(message);
            afterMessageFromWebView(messageAndParameterArray);
        }
    }

    @JavascriptInterface
    public void postMobileError(String message) {
        Log.d(TAG, "postMobileError: " + message);
    }

    @JavascriptInterface
    public void postMobileDebug(String message) {
        Log.d(TAG, "postMobileDebug: " + message);
    }

    @JavascriptInterface
    public boolean isChromeOS() {
        return isChromeOS(this);
    }

    void callFakeWebsocketOnMessage(final String message) {
        COWebView cOWebView = this.mWebView;
        if (cOWebView != null) {
            cOWebView.post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.7
                @Override // java.lang.Runnable
                public void run() {
                    if (LOActivity.this.mWebView == null) {
                        Log.i(LOActivity.TAG, "Skipped forwarding to the WebView: " + message);
                        return;
                    }
                    Log.i(LOActivity.TAG, "Forwarding to the WebView: " + message);
                    COWebView cOWebView2 = LOActivity.this.mWebView;
                    cOWebView2.loadUrl("javascript:window.TheFakeWebSocket.onmessage({'data':" + message + "});");
                }
            });
        }
        if (message.startsWith("'statusindicator") || message.startsWith("'error:")) {
            runOnUiThread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.8
                @Override // java.lang.Runnable
                public void run() {
                    if (message.startsWith("'statusindicatorsetvalue: ")) {
                        int start = "'statusindicatorsetvalue: ".length();
                        int end = message.indexOf("'", start);
                        int progress = 0;
                        try {
                            progress = Integer.parseInt(message.substring(start, end));
                        } catch (Exception e) {
                        }
                        LOActivity.this.mProgressDialog.determinateProgress(progress);
                    } else if (message.startsWith("'statusindicatorfinish:") || message.startsWith("'error:")) {
                        LOActivity.this.mProgressDialog.dismiss();
                    }
                }
            });
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean beforeMessageFromWebView(String[] messageAndParam) {
        char c;
        char c2 = 0;
        String str = messageAndParam[0];
        char c3 = 65535;
        switch (str.hashCode()) {
            case -1909554515:
                if (str.equals("EDITMODE")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1666653149:
                if (str.equals("DIM_SCREEN")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1388439968:
                if (str.equals("REQUESTFILECOPY")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1159470777:
                if (str.equals("loadwithpassword")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -757077946:
                if (str.equals("HYPERLINK")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 66254:
                if (str.equals("BYE")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 115958:
                if (str.equals("uno")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2537853:
                if (str.equals("SAVE")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3882020:
                if (str.equals("hideProgressbar")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 76397197:
                if (str.equals("PRINT")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 922762381:
                if (str.equals("MOBILEWIZARD")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1991043086:
                if (str.equals("SLIDESHOW")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2039141050:
                if (str.equals("downloadas")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 2052920277:
                if (str.equals("LIGHT_SCREEN")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                finishWithProgress();
                return false;
            case 1:
                getMainHandler().post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.9
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.initiatePrint();
                    }
                });
                return false;
            case 2:
                initiateSlideShow();
                return false;
            case 3:
                copyTempBackToIntent();
                sendBroadcast(messageAndParam[0], messageAndParam[1]);
                return false;
            case 4:
                initiateSaveAs(messageAndParam[1]);
                return false;
            case 5:
                String str2 = messageAndParam[1];
                if (str2.hashCode() != -1566815615 || !str2.equals(".uno:Paste")) {
                    c2 = 65535;
                }
                if (c2 == 0) {
                    return performPaste();
                }
                break;
            case 6:
                getMainHandler().post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.10
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.getWindow().clearFlags(128);
                    }
                });
                return false;
            case 7:
                getMainHandler().post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.11
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.getWindow().addFlags(128);
                    }
                });
                return false;
            case '\b':
                String str3 = messageAndParam[1];
                int hashCode = str3.hashCode();
                if (hashCode != 3202370) {
                    if (hashCode == 3529469 && str3.equals("show")) {
                        c3 = 0;
                    }
                } else if (str3.equals("hide")) {
                    c3 = 1;
                }
                if (c3 == 0) {
                    this.mMobileWizardVisible = true;
                } else if (c3 == 1) {
                    this.mMobileWizardVisible = false;
                }
                return false;
            case '\t':
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(messageAndParam[1]));
                startActivity(intent);
                return false;
            case '\n':
                String str4 = messageAndParam[1];
                int hashCode2 = str4.hashCode();
                if (hashCode2 != 3551) {
                    if (hashCode2 == 109935 && str4.equals("off")) {
                        c3 = 1;
                    }
                } else if (str4.equals("on")) {
                    c3 = 0;
                }
                if (c3 == 0) {
                    this.mIsEditModeActive = true;
                    requestForOdf();
                } else if (c3 == 1) {
                    this.mIsEditModeActive = false;
                }
                return false;
            case 11:
                ProgressDialog progressDialog = this.mProgressDialog;
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return false;
            case '\f':
                this.mProgressDialog.determinate(R.string.loading);
                return true;
            case '\r':
                requestForCopy();
                return false;
        }
        return true;
    }

    public static void createNewFileInputDialog(Activity activity, String defaultFileName, String mimeType, int requestCode) {
        Intent i = new Intent("android.intent.action.CREATE_DOCUMENT");
        i.setType(mimeType);
        i.addCategory("android.intent.category.OPENABLE");
        i.putExtra("android.intent.extra.TITLE", defaultFileName);
        Uri documentsUri = Uri.parse("content://com.android.externalstorage.documents/document/home%3A");
        i.putExtra("android.provider.extra.INITIAL_URI", documentsUri);
        activity.startActivityForResult(i, requestCode);
    }

    private AlertDialog.Builder buildPrompt(String mTitle, String mMessage, String mPositiveBtnText, String mNegativeBtnText, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mTitle);
        if (mMessage.length() > 0) {
            builder.setMessage(mMessage);
        }
        builder.setPositiveButton(mPositiveBtnText, callback);
        builder.setNegativeButton(mNegativeBtnText, (DialogInterface.OnClickListener) null);
        builder.setCancelable(false);
        return builder;
    }

    public String getMimeType() {
        ContentResolver cR = getContentResolver();
        return cR.getType(getIntent().getData());
    }

    public String getFileName(boolean withExtension) {
        String filename = null;
        try {
            Cursor cursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                filename = cursor.getString(cursor.getColumnIndex("_display_name"));
            }
            if (!withExtension) {
                return filename.substring(0, filename.lastIndexOf("."));
            }
            return filename;
        } catch (Exception e) {
            return null;
        }
    }

    private void requestForCopy() {
        final boolean canBeExported = canDocumentBeExported();
        buildPrompt(getString(R.string.ask_for_copy), "", getString(canBeExported ? R.string.edit_copy : R.string.use_odf), getString(R.string.view_only), new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidlib.LOActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (canBeExported) {
                    LOActivity.createNewFileInputDialog(LOActivity.this.mActivity, LOActivity.this.getFileName(true), LOActivity.this.getMimeType(), LOActivity.REQUEST_COPY);
                    return;
                }
                LOActivity lOActivity = LOActivity.this;
                String extension = lOActivity.getOdfExtensionForDocType(lOActivity.getMimeType());
                Activity activity = LOActivity.this.mActivity;
                LOActivity.createNewFileInputDialog(activity, LOActivity.this.getFileName(false) + "." + extension, LOActivity.this.getMimeForFormat(extension), LOActivity.REQUEST_COPY);
            }
        }).show();
    }

    private boolean canDocumentBeExported() {
        if (getMimeType().equals("application/vnd.ms-excel.sheet.binary.macroenabled.12")) {
            return false;
        }
        return true;
    }

    public String getOdfExtensionForDocType(String mimeType) {
        if (mimeType.equals("text/plain")) {
            return "odt";
        }
        if (!mimeType.equals("text/comma-separated-values") && !mimeType.equals("application/vnd.ms-excel.sheet.binary.macroenabled.12")) {
            return null;
        }
        return "ods";
    }

    private void requestForOdf() {
        final String extTemp = getOdfExtensionForDocType(getMimeType());
        if (extTemp != null) {
            buildPrompt(getString(R.string.ask_for_convert_odf), getString(R.string.convert_odf_message), getString(R.string.use_odf), getString(R.string.use_text), new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidlib.LOActivity.13
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Activity activity = LOActivity.this.mActivity;
                    LOActivity.createNewFileInputDialog(activity, LOActivity.this.getFileName(false) + "." + extTemp, LOActivity.this.getMimeForFormat(extTemp), LOActivity.REQUEST_COPY);
                }
            }).show();
        }
    }

    private void initiateSaveAs(String optionsString) {
        Map<String, String> optionsMap = new HashMap<>();
        String[] options = optionsString.split(" ");
        for (String option : options) {
            String[] keyValue = option.split("=", 2);
            if (keyValue.length == 2) {
                optionsMap.put(keyValue[0], keyValue[1]);
            }
        }
        String format = optionsMap.get("format");
        String mime = getMimeForFormat(format);
        if (!(format == null || mime == null)) {
            String filename = optionsMap.get("name");
            if (filename == null) {
                filename = "document." + format;
            }
            int requestID = getRequestIDForFormat(format);
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.setType(mime);
            intent.putExtra("android.intent.extra.TITLE", filename);
            intent.putExtra("android.intent.extra.LOCAL_ONLY", false);
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            intent.putExtra("android.provider.extra.INITIAL_URI", Uri.fromFile(folder).toString());
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            startActivityForResult(intent, requestID);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int getRequestIDForFormat(String format) {
        char c;
        switch (format.hashCode()) {
            case 99640:
                if (format.equals("doc")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 109883:
                if (format.equals("odp")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 109886:
                if (format.equals("ods")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 109887:
                if (format.equals("odt")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 110834:
                if (format.equals("pdf")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 111220:
                if (format.equals("ppt")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 113252:
                if (format.equals("rtf")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 118783:
                if (format.equals("xls")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 3088960:
                if (format.equals("docx")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3120248:
                if (format.equals("epub")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 3447940:
                if (format.equals("pptx")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 3682393:
                if (format.equals("xlsx")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return REQUEST_SAVEAS_PDF;
            case 1:
                return REQUEST_SAVEAS_RTF;
            case 2:
                return REQUEST_SAVEAS_ODT;
            case 3:
                return REQUEST_SAVEAS_ODP;
            case 4:
                return REQUEST_SAVEAS_ODS;
            case 5:
                return REQUEST_SAVEAS_DOCX;
            case 6:
                return REQUEST_SAVEAS_PPTX;
            case 7:
                return REQUEST_SAVEAS_XLSX;
            case '\b':
                return REQUEST_SAVEAS_DOC;
            case '\t':
                return REQUEST_SAVEAS_PPT;
            case '\n':
                return 511;
            case 11:
                return 512;
            default:
                return 0;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public String getMimeForFormat(String format) {
        char c;
        switch (format.hashCode()) {
            case 99640:
                if (format.equals("doc")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 109883:
                if (format.equals("odp")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 109886:
                if (format.equals("ods")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 109887:
                if (format.equals("odt")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 110834:
                if (format.equals("pdf")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 111220:
                if (format.equals("ppt")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 113252:
                if (format.equals("rtf")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 118783:
                if (format.equals("xls")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 3088960:
                if (format.equals("docx")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3120248:
                if (format.equals("epub")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 3447940:
                if (format.equals("pptx")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 3682393:
                if (format.equals("xlsx")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "application/pdf";
            case 1:
                return "text/rtf";
            case 2:
                return "application/vnd.oasis.opendocument.text";
            case 3:
                return "application/vnd.oasis.opendocument.presentation";
            case 4:
                return "application/vnd.oasis.opendocument.spreadsheet";
            case 5:
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case 6:
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case 7:
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case '\b':
                return "application/msword";
            case '\t':
                return "application/vnd.ms-powerpoint";
            case '\n':
                return "application/vnd.ms-excel";
            case 11:
                return "application/epub+zip";
            default:
                return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0034, code lost:
        if (r2.equals(".uno:Copy") == false) goto L18;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0044 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void afterMessageFromWebView(java.lang.String[] r7) {
        /*
            r6 = this;
            r0 = 0
            r1 = r7[r0]
            int r2 = r1.hashCode()
            r3 = -1
            r4 = 115958(0x1c4f6, float:1.62492E-40)
            if (r2 == r4) goto Le
        Ld:
            goto L18
        Le:
            java.lang.String r2 = "uno"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Ld
            r1 = 0
            goto L19
        L18:
            r1 = -1
        L19:
            if (r1 == 0) goto L1c
            goto L4c
        L1c:
            r1 = 1
            r2 = r7[r1]
            int r4 = r2.hashCode()
            r5 = -1717841488(0xffffffff999bd1b0, float:-1.6111316E-23)
            if (r4 == r5) goto L37
            r5 = -1713484345(0xffffffff99de4dc7, float:-2.2985676E-23)
            if (r4 == r5) goto L2e
        L2d:
            goto L41
        L2e:
            java.lang.String r4 = ".uno:Copy"
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L2d
            goto L42
        L37:
            java.lang.String r0 = ".uno:Cut"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L2d
            r0 = 1
            goto L42
        L41:
            r0 = -1
        L42:
            if (r0 == 0) goto L47
            if (r0 == r1) goto L47
            goto L4b
        L47:
            r6.populateClipboard()
        L4b:
        L4c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.libreoffice.androidlib.LOActivity.afterMessageFromWebView(java.lang.String[]):void");
    }

    public void initiatePrint() {
        PrintManager printManager = (PrintManager) getSystemService("print");
        PrintDocumentAdapter printAdapter = new PrintAdapter(this);
        printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
    }

    private void initiateSlideShow() {
        this.mProgressDialog.indeterminate(R.string.loading);
        this.nativeHandler.post(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.14
            @Override // java.lang.Runnable
            public void run() {
                Log.v(LOActivity.TAG, "saving svg for slideshow by " + Thread.currentThread().getName());
                final String slideShowFileUri = new File(LOActivity.this.getCacheDir(), "slideShow.svg").toURI().toString();
                LOActivity.this.saveAs(slideShowFileUri, "svg", null);
                LOActivity.this.runOnUiThread(new Runnable() { // from class: org.libreoffice.androidlib.LOActivity.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LOActivity.this.mProgressDialog.dismiss();
                        Intent slideShowActIntent = new Intent(LOActivity.this, SlideShowActivity.class);
                        slideShowActIntent.putExtra("svgUriKey", slideShowFileUri);
                        LOActivity.this.startActivity(slideShowActIntent);
                    }
                });
            }
        });
    }

    public void sendBroadcast(String event, String data) {
        Intent intent = new Intent(LO_ACTIVITY_BROADCAST);
        intent.putExtra(LO_ACTION_EVENT, event);
        intent.putExtra(LO_ACTION_DATA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private final String getClipboardMagic() {
        return CLIPBOARD_COOL_SIGNATURE + Long.toString(this.loadDocumentMillis);
    }

    public final void populateClipboard() {
        File clipboardFile = new File(getApplicationContext().getCacheDir(), CLIPBOARD_FILE_PATH);
        if (clipboardFile.exists()) {
            clipboardFile.delete();
        }
        LokClipboardData clipboardData = new LokClipboardData();
        if (!getClipboardContent(clipboardData)) {
            Log.e(TAG, "no clipboard to copy");
            return;
        }
        clipboardData.writeToFile(clipboardFile);
        String text = clipboardData.getText();
        String html = clipboardData.getHtml();
        if (html != null) {
            int idx = html.indexOf("<meta name=\"generator\" content=\"");
            if (idx < 0) {
                idx = html.indexOf("<meta http-equiv=\"content-type\" content=\"text/html;");
            }
            if (idx >= 0) {
                StringBuffer newHtml = new StringBuffer(html);
                newHtml.insert(idx, "<meta name=\"origin\" content=\"" + getClipboardMagic() + "\"/>\n");
                html = newHtml.toString();
            }
            if (text == null || text.length() == 0) {
                Log.i(TAG, "set text to clipoard with: text '" + text + "' and html '" + html + "'");
            }
            this.clipData = ClipData.newHtmlText("text/html", text, html);
            this.clipboardManager.setPrimaryClip(this.clipData);
        }
    }

    private final boolean performPaste() {
        ClipDescription clipDesc;
        this.clipData = this.clipboardManager.getPrimaryClip();
        ClipData clipData = this.clipData;
        if (clipData == null || (clipDesc = clipData.getDescription()) == null) {
            return false;
        }
        for (int i = 0; i < clipDesc.getMimeTypeCount(); i++) {
            Log.d(TAG, "Pasting mime " + i + ": " + clipDesc.getMimeType(i));
            if (clipDesc.getMimeType(i).equals("text/html")) {
                String html = this.clipData.getItemAt(i).getHtmlText();
                if (!html.contains(CLIPBOARD_COOL_SIGNATURE)) {
                    Log.i(TAG, "foreign html '" + html + "'");
                    byte[] htmlByteArray = html.getBytes(Charset.forName("UTF-8"));
                    paste("text/html", htmlByteArray);
                    return false;
                } else if (html.contains(getClipboardMagic())) {
                    Log.i(TAG, "clipboard comes from us - same instance: short circuit it " + html);
                    return true;
                } else {
                    Log.i(TAG, "clipboard comes from us - other instance: paste from clipboard file");
                    File clipboardFile = new File(getApplicationContext().getCacheDir(), CLIPBOARD_FILE_PATH);
                    LokClipboardData clipboardData = null;
                    if (clipboardFile.exists()) {
                        clipboardData = LokClipboardData.createFromFile(clipboardFile);
                    }
                    if (clipboardData != null) {
                        setClipboardContent(clipboardData);
                        return true;
                    }
                    byte[] htmlByteArray2 = html.getBytes(Charset.forName("UTF-8"));
                    paste("text/html", htmlByteArray2);
                    return false;
                }
            } else {
                if (clipDesc.getMimeType(i).startsWith("image/")) {
                    ClipData.Item item = this.clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(uri);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        byte[] data = new byte[16384];
                        while (true) {
                            int nRead = imageStream.read(data, 0, data.length);
                            if (nRead != -1) {
                                buffer.write(data, 0, nRead);
                            } else {
                                paste(clipDesc.getMimeType(i), buffer.toByteArray());
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to paste image: " + e.getMessage());
                    }
                }
            }
        }
        for (int i2 = 0; i2 < clipDesc.getMimeTypeCount(); i2++) {
            Log.d(TAG, "Plain text paste attempt " + i2 + ": " + clipDesc.getMimeType(i2));
            if (clipDesc.getMimeType(i2).equals("text/plain")) {
                ClipData.Item clipItem = this.clipData.getItemAt(i2);
                String text = clipItem.getText().toString();
                byte[] textByteArray = text.getBytes(Charset.forName("UTF-8"));
                paste("text/plain;charset=utf-8", textByteArray);
            }
        }
        return false;
    }
}
