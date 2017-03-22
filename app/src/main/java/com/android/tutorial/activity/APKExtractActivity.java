package com.android.tutorial.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.adapters.APKAppAdapter;
import com.android.tutorial.interfaces.APKSelectListener;
import com.android.tutorial.models.AppAPK;
import com.claudiodegio.msv.MaterialSearchView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class APKExtractActivity extends AppCompatActivity {

    ListView listView;
    TextView titleTextView;
    EditText inputSearch;
    List<AppAPK> apkArrayList;
    MaterialSearchView searchView;
    APKAppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apkextract);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.apkListView);
        apkArrayList = new ArrayList<AppAPK>();
        titleTextView = (TextView) findViewById(R.id.aplListViewTitle);
        inputSearch = (EditText) findViewById(R.id.apkInputSearch);
        searchView = (MaterialSearchView) findViewById(R.id.apkListSearchView);

        titleTextView.setText(R.string.title_activity_apkextract);

//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> applicationInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        getApkAppsList();
        adapter = new APKAppAdapter(APKExtractActivity.this, apkArrayList, new APKSelectListener() {
            @Override
            public void onAPKClick(AppAPK appAPK) {
                Toast.makeText(APKExtractActivity.this, appAPK.getAppPackage(), Toast.LENGTH_SHORT).show();
                getApkFiles(appAPK);
            }
        });
        listView.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                APKExtractActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void getApkAppsList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (Object object : pkgAppsList) {

            ResolveInfo info = (ResolveInfo) object;
            File f1 = new File(info.activityInfo.applicationInfo.publicSourceDir);

            AppAPK appAPK = new AppAPK();
            appAPK.setAppLabel(info.loadLabel(getPackageManager()).toString());
            appAPK.setAppPackage(f1.getName().toString());
            appAPK.setAppLoadIcon(info.loadIcon(getPackageManager()));
            apkArrayList.add(appAPK);
            String file_name = info.loadLabel(getPackageManager()).toString();
        }
    }

    public void getApkFiles(AppAPK appAPK) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        int z = 0;
        for (Object object : pkgAppsList) {

            ResolveInfo info = (ResolveInfo) object;

            File f1 = new File(info.activityInfo.applicationInfo.publicSourceDir);

            Log.v("file--", " " + f1.getName().toString() + "----" + info.loadLabel(getPackageManager()));
            try {

                String file_name = info.loadLabel(getPackageManager()).toString();

                if (file_name.equals(appAPK.getAppLabel())) {
                    Log.d("file_name--", " " + file_name);

                    // File f2 = new File(Environment.getExternalStorageDirectory().toString()+"/Folder/"+file_name+".apk");
                    // f2.createNewFile();

                    File f2 = new File(Environment.getExternalStorageDirectory().toString() + "/APKFilesBackup");
                    f2.mkdirs();
                    f2 = new File(f2.getPath() + "/" + file_name + ".apk");
                    f2.createNewFile();

                    InputStream in = new FileInputStream(f1);

                    OutputStream out = new FileOutputStream(f2);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    System.out.println("File copied.");
                    Toast.makeText(this, "File extracted.", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage() + " in the specified directory.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
