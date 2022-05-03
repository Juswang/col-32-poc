package org.libreoffice.androidapp.ui;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.libreoffice.androidapp.AboutDialogFragment;
import org.libreoffice.androidapp.SettingsActivity;
import org.libreoffice.androidapp.SettingsListenerModel;
import org.libreoffice.androidapp.debug.R;
import org.libreoffice.androidlib.LOActivity;

/* loaded from: classes.dex */
public class LibreOfficeUIActivity extends AppCompatActivity implements SettingsListenerModel.OnSettingsPreferenceChangedListener {
    private static final int CREATE_DOCUMENT_REQUEST_CODE = 44;
    private static final int CREATE_PRESENTATION_REQUEST_CODE = 46;
    private static final int CREATE_SPREADSHEET_REQUEST_CODE = 45;
    private static final String CURRENT_DIRECTORY_KEY = "CURRENT_DIRECTORY";
    private static final String DOC_PROVIDER_KEY = "CURRENT_DOCUMENT_PROVIDER";
    private static final String ENABLE_SHOW_HIDDEN_FILES_KEY = "ENABLE_SHOW_HIDDEN_FILES";
    public static final String EXPLORER_PREFS_KEY = "EXPLORER_PREFS";
    public static final String EXPLORER_VIEW_TYPE_KEY = "EXPLORER_VIEW_TYPE";
    private static final String FILTER_MODE_KEY = "FILTER_MODE";
    public static final String GRID_VIEW = "0";
    public static final String LIST_VIEW = "1";
    private static final int LO_ACTIVITY_REQUEST_CODE = 42;
    public static final String NEW_DOC_TYPE_KEY = "NEW_DOC_TYPE_KEY";
    public static final String NEW_FILE_PATH_KEY = "NEW_FILE_PATH_KEY";
    public static final String NIGHT_MODE_KEY = "NIGHT_MODE";
    private static final int OPEN_FILE_REQUEST_CODE = 43;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 0;
    public static final String RECENT_DOCUMENTS_KEY = "RECENT_DOCUMENTS_LIST";
    public static final String SORT_MODE_KEY = "SORT_MODE";
    private ActionBar actionBar;
    private FloatingActionButton calcFAB;
    private LinearLayout calcLayout;
    private Uri currentlySelectedFile;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton editFAB;
    private Animation fabCloseAnimation;
    private Animation fabOpenAnimation;
    FileFilter fileFilter;
    FilenameFilter filenameFilter;
    private FloatingActionButton impressFAB;
    private LinearLayout impressLayout;
    private ImageView mRecentFilesListOrGrid;
    private NavigationView navigationDrawer;
    TextView noRecentItemsTextView;
    private SharedPreferences prefs;
    private RecyclerView recentRecyclerView;
    private boolean showHiddenFiles;
    private int sortMode;
    private FloatingActionButton writerFAB;
    private LinearLayout writerLayout;
    private String LOGTAG = LibreOfficeUIActivity.class.getSimpleName();
    private int filterMode = -1;
    private boolean isFabMenuOpen = false;
    private final BroadcastReceiver mLOActivityReceiver = new BroadcastReceiver() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String event = intent.getStringExtra(LOActivity.LO_ACTION_EVENT);
            String str = LibreOfficeUIActivity.this.LOGTAG;
            Log.d(str, "Received a message from LOActivity: " + event);
            event.equals("SAVE");
        }
    };

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.documentprovider_preferences, false);
        readPreferences();
        int mode = this.prefs.getInt(NIGHT_MODE_KEY, -1);
        AppCompatDelegate.setDefaultNightMode(mode);
        super.onCreate(savedInstanceState);
        SettingsListenerModel.getInstance().setListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mLOActivityReceiver, new IntentFilter(LOActivity.LO_ACTIVITY_BROADCAST));
        createUI();
        this.fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        this.fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close);
    }

    private String[] getRecentDocuments() {
        String joinedStrings = this.prefs.getString("RECENT_DOCUMENTS_LIST", "");
        if (joinedStrings.isEmpty()) {
            return new String[0];
        }
        return joinedStrings.split("\n", 0);
    }

    public void updateRecentFiles() {
        if (isViewModeList()) {
            this.mRecentFilesListOrGrid.setImageResource(R.drawable.ic_view_module_black_24dp);
        } else {
            this.mRecentFilesListOrGrid.setImageResource(R.drawable.ic_list_black_24dp);
        }
        String[] recentFileStrings = getRecentDocuments();
        ArrayList<Uri> recentUris = new ArrayList<>();
        for (String recentFileString : recentFileStrings) {
            try {
                recentUris.add(Uri.parse(recentFileString));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        this.recentRecyclerView.setLayoutManager(isViewModeList() ? new LinearLayoutManager(this) : new GridLayoutManager(this, 2));
        this.recentRecyclerView.setAdapter(new RecentFilesAdapter(this, recentUris));
    }

    public SharedPreferences getPrefs() {
        return this.prefs;
    }

    public void setupNavigationDrawer() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        this.navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.1
            @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_about) {
                    AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
                    aboutDialogFragment.show(LibreOfficeUIActivity.this.getSupportFragmentManager(), "AboutDialogFragment");
                    return true;
                } else if (itemId != R.id.action_settings) {
                    return false;
                } else {
                    LibreOfficeUIActivity libreOfficeUIActivity = LibreOfficeUIActivity.this;
                    libreOfficeUIActivity.startActivity(new Intent(libreOfficeUIActivity.getApplicationContext(), SettingsActivity.class));
                    return true;
                }
            }
        });
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.document_locations, R.string.close_document_locations) { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.2
            @Override // androidx.appcompat.app.ActionBarDrawerToggle, androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                LibreOfficeUIActivity.this.supportInvalidateOptionsMenu();
                LibreOfficeUIActivity.this.navigationDrawer.requestFocus();
                LibreOfficeUIActivity.this.collapseFabMenu();
            }

            @Override // androidx.appcompat.app.ActionBarDrawerToggle, androidx.drawerlayout.widget.DrawerLayout.DrawerListener
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                LibreOfficeUIActivity.this.supportInvalidateOptionsMenu();
            }
        };
        this.drawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerLayout.addDrawerListener(this.drawerToggle);
        this.drawerToggle.syncState();
    }

    public void createUI() {
        setContentView(R.layout.activity_document_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.actionBar = getSupportActionBar();
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupFloatingActionButton();
        this.recentRecyclerView = (RecyclerView) findViewById(R.id.list_recent);
        this.noRecentItemsTextView = (TextView) findViewById(R.id.no_recent_items_msg);
        this.mRecentFilesListOrGrid = (ImageView) findViewById(R.id.recent_list_or_grid);
        this.mRecentFilesListOrGrid.setOnClickListener(new View.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LibreOfficeUIActivity.this.toggleViewMode();
                LibreOfficeUIActivity.this.updateRecentFiles();
            }
        });
        updateRecentFiles();
        registerForContextMenu(this.recentRecyclerView);
        setupNavigationDrawer();
    }

    private void setupFloatingActionButton() {
        this.editFAB = (FloatingActionButton) findViewById(R.id.editFAB);
        if (LOActivity.isChromeOS(this)) {
            int dp = (int) getResources().getDisplayMetrics().density;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.editFAB.getLayoutParams();
            layoutParams.leftToLeft = 0;
            layoutParams.bottomMargin = dp * 24;
            this.editFAB.setCustomSize(dp * 70);
        }
        this.editFAB.setOnClickListener(new View.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (LibreOfficeUIActivity.this.isFabMenuOpen) {
                    LibreOfficeUIActivity.this.collapseFabMenu();
                } else {
                    LibreOfficeUIActivity.this.expandFabMenu();
                }
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.calcLayout /* 2131296335 */:
                    case R.id.newCalcFAB /* 2131296439 */:
                        LibreOfficeUIActivity libreOfficeUIActivity = LibreOfficeUIActivity.this;
                        libreOfficeUIActivity.createNewFileInputDialog(LibreOfficeUIActivity.this.getString(R.string.new_spreadsheet) + FileUtilities.DEFAULT_SPREADSHEET_EXTENSION, "application/vnd.oasis.opendocument.spreadsheet", 45);
                        return;
                    case R.id.impressLayout /* 2131296414 */:
                    case R.id.newImpressFAB /* 2131296441 */:
                        LibreOfficeUIActivity libreOfficeUIActivity2 = LibreOfficeUIActivity.this;
                        libreOfficeUIActivity2.createNewFileInputDialog(LibreOfficeUIActivity.this.getString(R.string.new_presentation) + FileUtilities.DEFAULT_IMPRESS_EXTENSION, "application/vnd.oasis.opendocument.presentation", 46);
                        return;
                    case R.id.newWriterFAB /* 2131296443 */:
                    case R.id.writerLayout /* 2131296577 */:
                        LibreOfficeUIActivity libreOfficeUIActivity3 = LibreOfficeUIActivity.this;
                        libreOfficeUIActivity3.createNewFileInputDialog(LibreOfficeUIActivity.this.getString(R.string.new_textdocument) + FileUtilities.DEFAULT_WRITER_EXTENSION, "application/vnd.oasis.opendocument.text", 44);
                        return;
                    default:
                        return;
                }
            }
        };
        this.writerFAB = (FloatingActionButton) findViewById(R.id.newWriterFAB);
        this.writerFAB.setOnClickListener(clickListener);
        this.calcFAB = (FloatingActionButton) findViewById(R.id.newCalcFAB);
        this.calcFAB.setOnClickListener(clickListener);
        this.impressFAB = (FloatingActionButton) findViewById(R.id.newImpressFAB);
        this.impressFAB.setOnClickListener(clickListener);
        this.writerLayout = (LinearLayout) findViewById(R.id.writerLayout);
        this.writerLayout.setOnClickListener(clickListener);
        this.impressLayout = (LinearLayout) findViewById(R.id.impressLayout);
        this.impressLayout.setOnClickListener(clickListener);
        this.calcLayout = (LinearLayout) findViewById(R.id.calcLayout);
        this.calcLayout.setOnClickListener(clickListener);
    }

    public void expandFabMenu() {
        if (!this.isFabMenuOpen) {
            ViewCompat.animate(this.editFAB).rotation(-45.0f).withLayer().setDuration(300L).setInterpolator(new OvershootInterpolator(0.0f)).start();
            this.impressLayout.startAnimation(this.fabOpenAnimation);
            this.writerLayout.startAnimation(this.fabOpenAnimation);
            this.calcLayout.startAnimation(this.fabOpenAnimation);
            this.writerFAB.setClickable(true);
            this.impressFAB.setClickable(true);
            this.calcFAB.setClickable(true);
            this.isFabMenuOpen = true;
        }
    }

    public void collapseFabMenu() {
        if (this.isFabMenuOpen) {
            ViewCompat.animate(this.editFAB).rotation(0.0f).withLayer().setDuration(300L).setInterpolator(new OvershootInterpolator(0.0f)).start();
            this.writerLayout.startAnimation(this.fabCloseAnimation);
            this.impressLayout.startAnimation(this.fabCloseAnimation);
            this.calcLayout.startAnimation(this.fabCloseAnimation);
            this.writerFAB.setClickable(false);
            this.impressFAB.setClickable(false);
            this.calcFAB.setClickable(false);
            this.isFabMenuOpen = false;
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    private void refreshView() {
        updateRecentFiles();
        this.drawerLayout.closeDrawer(this.navigationDrawer);
        collapseFabMenu();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(this.navigationDrawer)) {
            this.drawerLayout.closeDrawer(this.navigationDrawer);
            collapseFabMenu();
        } else if (this.isFabMenuOpen) {
            collapseFabMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_open /* 2131296354 */:
                open(this.currentlySelectedFile);
                return true;
            case R.id.context_menu_remove_from_list /* 2131296355 */:
                removeFromList(this.currentlySelectedFile);
                return true;
            case R.id.context_menu_share /* 2131296356 */:
                share(this.currentlySelectedFile);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void openContextMenu(View view, Uri uri) {
        this.currentlySelectedFile = uri;
        if (Build.VERSION.SDK_INT >= 24) {
            view.showContextMenu(view.getPivotX(), view.getPivotY());
        } else {
            view.showContextMenu();
        }
    }

    public boolean isViewModeList() {
        return this.prefs.getString(EXPLORER_VIEW_TYPE_KEY, GRID_VIEW).equals(LIST_VIEW);
    }

    public void toggleViewMode() {
        if (isViewModeList()) {
            this.prefs.edit().putString(EXPLORER_VIEW_TYPE_KEY, GRID_VIEW).apply();
        } else {
            this.prefs.edit().putString(EXPLORER_VIEW_TYPE_KEY, LIST_VIEW).apply();
        }
    }

    public Intent getIntentToEdit(Uri uri) {
        Intent i = new Intent("android.intent.action.EDIT", uri);
        i.addFlags(1);
        i.addFlags(2);
        String packageName = getApplicationContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName, LOActivity.class.getName());
        i.setComponent(componentName);
        return i;
    }

    public void open(Uri uri) {
        if (uri != null) {
            addDocumentToRecents(uri);
            Intent i = getIntentToEdit(uri);
            startActivityForResult(i, 42);
        }
    }

    public void createNewFileInputDialog(String defaultFileName, String mimeType, int requestCode) {
        collapseFabMenu();
        LOActivity.createNewFileInputDialog(this, defaultFileName, mimeType, requestCode);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.libreoffice.androidapp.ui.LibreOfficeUIActivity$1CreateThread] */
    private void createNewFile(final Uri uri, final String extension) {
        ?? r0 = new Thread() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.1CreateThread
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                InputStream templateFileStream = null;
                OutputStream newFileStream = null;
                try {
                    try {
                        try {
                            AssetManager assets = LibreOfficeUIActivity.this.getAssets();
                            templateFileStream = assets.open("templates/untitled." + extension);
                            newFileStream = LibreOfficeUIActivity.this.getContentResolver().openOutputStream(uri);
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int length = templateFileStream.read(buffer);
                                if (length > 0) {
                                    newFileStream.write(buffer, 0, length);
                                } else {
                                    templateFileStream.close();
                                    newFileStream.close();
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        templateFileStream.close();
                        newFileStream.close();
                    }
                } catch (Throwable th) {
                    try {
                        templateFileStream.close();
                        newFileStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    throw th;
                }
            }
        };
        r0.run();
        try {
            r0.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void open(int position) {
    }

    private void share(Uri uri) {
        if (uri != null) {
            Intent intentShareFile = new Intent("android.intent.action.SEND");
            intentShareFile.putExtra("android.intent.extra.STREAM", uri);
            intentShareFile.addFlags(1);
            intentShareFile.setDataAndType(uri, getContentResolver().getType(uri));
            startActivity(Intent.createChooser(intentShareFile, getString(R.string.share_document)));
        }
    }

    private void removeFromList(Uri uri) {
        if (uri != null) {
            String[] recentFileStrings = getRecentDocuments();
            String joined = "";
            ArrayList<Uri> recentUris = new ArrayList<>();
            for (String recentFileString : recentFileStrings) {
                try {
                    if (!uri.toString().equals(recentFileString)) {
                        recentUris.add(Uri.parse(recentFileString));
                        joined = joined.concat(recentFileString + "\n");
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            this.prefs.edit().putString("RECENT_DOCUMENTS_LIST", joined).apply();
            this.recentRecyclerView.setAdapter(new RecentFilesAdapter(this, recentUris));
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }

    private void openDocument() {
        collapseFabMenu();
        Intent i = new Intent();
        i.addCategory("android.intent.category.OPENABLE");
        i.setType("*/*");
        if (!LOActivity.isChromeOS(this)) {
            String[] mimeTypes = {"application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.graphics", "application/vnd.oasis.opendocument.presentation", "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.oasis.opendocument.text-flat-xml", "application/vnd.oasis.opendocument.graphics-flat-xml", "application/vnd.oasis.opendocument.presentation-flat-xml", "application/vnd.oasis.opendocument.spreadsheet-flat-xml", "application/vnd.oasis.opendocument.text-template", "application/vnd.oasis.opendocument.spreadsheet-template", "application/vnd.oasis.opendocument.graphics-template", "application/vnd.oasis.opendocument.presentation-template", "application/rtf", "text/rtf", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel", "application/vnd.visio", "application/vnd.visio.xml", "application/x-mspublisher", "application/vnd.ms-excel.sheet.binary.macroenabled.12", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/vnd.openxmlformats-officedocument.presentationml.slideshow", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.wordprocessingml.template", "application/vnd.openxmlformats-officedocument.spreadsheetml.template", "application/vnd.openxmlformats-officedocument.presentationml.template", "text/csv", "text/plain", "text/comma-separated-values", "application/vnd.ms-works", "application/vnd.apple.keynote", "application/x-abiword", "application/x-pagemaker", "image/x-emf", "image/x-svm", "image/x-wmf", "image/svg+xml", "application/pdf"};
            i.putExtra("android.intent.extra.MIME_TYPES", mimeTypes);
        }
        try {
            i.setAction("android.intent.action.OPEN_DOCUMENT");
            startActivityForResult(i, 43);
        } catch (ActivityNotFoundException e) {
            Log.w(this.LOGTAG, "Start of activity with ACTION_OPEN_DOCUMENT failed (no activity found). Trying the fallback.");
            i.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(i, 43);
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() != R.id.action_open_file) {
            return super.onOptionsItemSelected(item);
        }
        openDocument();
        return true;
    }

    public void readPreferences() {
        this.prefs = getSharedPreferences("EXPLORER_PREFS", 0);
        this.sortMode = this.prefs.getInt(SORT_MODE_KEY, 0);
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.filterMode = Integer.valueOf(defaultPrefs.getString(FILTER_MODE_KEY, "-1")).intValue();
        this.showHiddenFiles = defaultPrefs.getBoolean(ENABLE_SHOW_HIDDEN_FILES_KEY, false);
        Intent i = getIntent();
        if (i.hasExtra(CURRENT_DIRECTORY_KEY)) {
            Log.d(this.LOGTAG, CURRENT_DIRECTORY_KEY);
        }
        if (i.hasExtra(FILTER_MODE_KEY)) {
            this.filterMode = i.getIntExtra(FILTER_MODE_KEY, -1);
            Log.d(this.LOGTAG, FILTER_MODE_KEY);
        }
    }

    @Override // org.libreoffice.androidapp.SettingsListenerModel.OnSettingsPreferenceChangedListener
    public void settingsPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        readPreferences();
        refreshView();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FILTER_MODE_KEY, this.filterMode);
        outState.putBoolean(ENABLE_SHOW_HIDDEN_FILES_KEY, this.showHiddenFiles);
        Log.d(this.LOGTAG, "savedInstanceState");
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (!savedInstanceState.isEmpty()) {
            this.filterMode = savedInstanceState.getInt(FILTER_MODE_KEY, -1);
            this.showHiddenFiles = savedInstanceState.getBoolean(ENABLE_SHOW_HIDDEN_FILES_KEY, false);
            Log.d(this.LOGTAG, "onRestoreInstanceState");
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 42:
                Log.d(this.LOGTAG, "LOActivity has finished.");
                return;
            case 43:
                Log.d(this.LOGTAG, "File open chooser has finished, starting the LOActivity.");
                if (resultCode == -1 && data != null && (uri = data.getData()) != null) {
                    getContentResolver().takePersistableUriPermission(uri, 3);
                    open(uri);
                    return;
                }
                return;
            case 44:
            case 45:
            case 46:
                if (resultCode == -1 && data != null) {
                    Uri uri2 = data.getData();
                    getContentResolver().takePersistableUriPermission(uri2, 3);
                    String extension = requestCode == 44 ? "odt" : requestCode == 45 ? "ods" : "odp";
                    createNewFile(uri2, extension);
                    open(uri2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Log.d(this.LOGTAG, "onPause");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Log.d(this.LOGTAG, "onResume");
        String str = this.LOGTAG;
        Log.d(str, "sortMode=" + this.sortMode + " filterMode=" + this.filterMode);
        createUI();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            Log.i(this.LOGTAG, "no permission to read external storage - asking for permission");
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        }
        Log.d(this.LOGTAG, "onStart");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        Log.d(this.LOGTAG, "onStop");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mLOActivityReceiver);
        Log.d(this.LOGTAG, "onDestroy");
    }

    private int dpToPx(int dp) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    private void addDocumentToRecents(Uri uri) {
        int i;
        String newRecent = uri.toString();
        ArrayList<String> recentsArrayList = new ArrayList<>(Arrays.asList(getRecentDocuments()));
        recentsArrayList.remove(newRecent);
        recentsArrayList.add(0, newRecent);
        while (true) {
            i = 1;
            if (recentsArrayList.size() <= 30) {
                break;
            }
            recentsArrayList.remove(recentsArrayList.size() - 1);
        }
        String joined = TextUtils.join("\n", recentsArrayList);
        this.prefs.edit().putString("RECENT_DOCUMENTS_LIST", joined).apply();
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager shortcutManager = (ShortcutManager) getSystemService(ShortcutManager.class);
            shortcutManager.removeAllDynamicShortcuts();
            ArrayList<ShortcutInfo> shortcuts = new ArrayList<>();
            Iterator<String> it = recentsArrayList.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                String pathString = it.next();
                if (!pathString.isEmpty()) {
                    if (i2 < 3 && i2 < ShortcutManagerCompat.getMaxShortcutCountPerActivity(this)) {
                        i2++;
                        int drawable = 0;
                        int type = FileUtilities.getType(pathString);
                        if (type == 0) {
                            drawable = R.drawable.writer;
                        } else if (type == i) {
                            drawable = R.drawable.calc;
                        } else if (type == 2) {
                            drawable = R.drawable.impress;
                        } else if (type == 3) {
                            drawable = R.drawable.draw;
                        }
                        Uri shortcutUri = Uri.parse(pathString);
                        String filename = RecentFilesAdapter.getUriFilename(this, shortcutUri);
                        if (filename != null) {
                            Intent intent = getIntentToEdit(shortcutUri);
                            ShortcutInfo.Builder builder = new ShortcutInfo.Builder(this, filename).setShortLabel(filename).setLongLabel(filename).setIntent(intent);
                            if (drawable != 0) {
                                builder.setIcon(Icon.createWithResource(this, drawable));
                            }
                            shortcuts.add(builder.build());
                        }
                        i = 1;
                    }
                }
            }
            try {
                shortcutManager.setDynamicShortcuts(shortcuts);
            } catch (Exception e) {
                Log.e(this.LOGTAG, "Failed to set the dynamic shortcuts: " + e.getMessage());
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 0) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if ((permissions.length <= 0 || grantResults[0] != 0) && Build.VERSION.SDK_INT >= 23) {
            boolean showRationale = shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");
            AlertDialog.Builder rationaleDialogBuilder = new AlertDialog.Builder(this).setCancelable(false).setTitle(getString(R.string.title_permission_required)).setMessage(getString(R.string.reason_required_to_read_documents));
            if (showRationale) {
                rationaleDialogBuilder.setPositiveButton(getString(R.string.positive_ok), new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LibreOfficeUIActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
                    }
                }).setNegativeButton(getString(R.string.negative_im_sure), new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        LibreOfficeUIActivity.this.finish();
                    }
                }).create().show();
            } else {
                rationaleDialogBuilder.setPositiveButton(getString(R.string.positive_ok), new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.10
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        Uri uri = Uri.fromParts("package", LibreOfficeUIActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        LibreOfficeUIActivity.this.startActivity(intent);
                    }
                }).setNegativeButton(R.string.negative_cancel, new DialogInterface.OnClickListener() { // from class: org.libreoffice.androidapp.ui.LibreOfficeUIActivity.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        LibreOfficeUIActivity.this.finish();
                    }
                }).create().show();
            }
        }
    }
}
