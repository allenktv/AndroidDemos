package com.allen.sampleloader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Allen on 6/6/2014.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
    private static final String TAG = "ADP_AppListLoader";
    private static final boolean DEBUG = true;

    final PackageManager mPm;

    // We hold a reference to the Loader's data here.
    private List<AppEntry> mApps;

    public AppListLoader(Context c){
        super(c);
        mPm = getContext().getPackageManager();
    }

    @Override
    public List<AppEntry> loadInBackground() {

        List<ApplicationInfo> apps = mPm.getInstalledApplications(0);

        if(apps == null){
            apps= new ArrayList<ApplicationInfo>();
        }

        //Create corresponding array of entries and load their labels.
        List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
        for(int i=0;i<apps.size();i++){
            AppEntry entry = new AppEntry(this,apps.get(i));
            entry.loadLabel(getContext());
            entries.add(entry);
        }

        //Sort the list
        Collections.sort(entries,ALPHA_COMPARATOR);

        return entries;
    }

    @Override
    public void deliverResult(List<AppEntry> apps) {
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<AppEntry> oldApps = mApps;
        mApps = apps;

        if (isStarted()) {
            if (DEBUG) Log.i(TAG, "+++ Delivering results to the LoaderManager for" +
                    " the ListFragment to display! +++");
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(apps);
        }
    }

    @Override
    protected void onStartLoading() {
        if (DEBUG) Log.i(TAG, "+++ onStartLoading() called! +++");

        if (mApps != null) {
            // Deliver any previously loaded data immediately.
            if (DEBUG) Log.i(TAG, "+++ Delivering previously loaded data to the client...");
            deliverResult(mApps);
        }

        if (takeContentChanged()) {
            // When the observer detects a new installed application, it will call
            // onContentChanged() on the Loader, which will cause the next call to
            // takeContentChanged() to return true. If this is ever the case (or if
            // the current data is null), we force a new load.
            if (DEBUG) Log.i(TAG, "+++ A content change has been detected... so force load! +++");
            forceLoad();
        } else if (mApps == null) {
            // If the current data is null... then we should make it non-null! :)
            if (DEBUG) Log.i(TAG, "+++ The current data is data is null... so force load! +++");
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        if (DEBUG) Log.i(TAG, "+++ onStopLoading() called! +++");

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        if (DEBUG) Log.i(TAG, "+++ onReset() called! +++");

        // Ensure the loader is stopped.
        onStopLoading();
    }

    @Override
    public void onCanceled(List<AppEntry> apps) {
        if (DEBUG) Log.i(TAG, "+++ onCanceled() called! +++");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(apps);
    }

    @Override
    public void forceLoad() {
        if (DEBUG) Log.i(TAG, "+++ forceLoad() called! +++");
        super.forceLoad();
    }


    private static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };
}
