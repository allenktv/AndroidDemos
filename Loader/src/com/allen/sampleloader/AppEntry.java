package com.allen.sampleloader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Allen on 6/6/2014.
 */
public class AppEntry {
    private final AppListLoader mLoader;
    private final ApplicationInfo mInfo;
    private final File mApkFile;
    private String mLabel;
    private Drawable mIcon;
    private boolean mMounted;

    public AppEntry(AppListLoader loader, ApplicationInfo info){
        mLoader = loader;
        mInfo = info;
        mApkFile = new File(info.sourceDir);
    }

    public ApplicationInfo getApplicationInfo(){
        return mInfo;
    }
    public String getLabel() {
        return mLabel;
    }
    public Drawable getIcon(){
        if(mInfo==null){
            if(mApkFile.exists()){
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }else {
                mMounted = false;
            }
        } else if(!mMounted){
            //if apk file wasnt mounted, but now it is, reload icon
            if (mApkFile.exists()){
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }
        }else {
            return mIcon;
        }
        return mLoader.getContext().getResources()
                .getDrawable(android.R.drawable.sym_def_app_icon);
    }
    @Override
    public String toString() {
        return mLabel;
    }
    void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }
}
