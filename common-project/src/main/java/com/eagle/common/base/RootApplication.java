package com.eagle.common.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import java.util.ArrayList;
import java.util.List;

public class RootApplication extends Application {

    private static RootApplication instance;
    private List<Activity> activityList = new ArrayList<>();

    public static RootApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
//        if (!getProcessName(this).equalsIgnoreCase("com.ishansong.eagle")) {
//            return;
//        }
//        SSLogManager.d("huashao","RootApplication onCreate");
//        SSMapManager.getInstance().init();
//        AppService.start(this);
//        DBManager.getInstance().init(this);
//        SSPreferenceManager.getInstance().init(this);
//        SSActivityManager.getInstance().init();
//
//        OrderScanManager.getInstance().openScan();
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        activityList.clear();
        activityList = null;
//        VoiceUtil.getInstance().reset();
    }

    public void exitAllActivity(Activity activity) {
//        OrderScanManager.getInstance().closeScan();
        synchronized (this) {
            int size = activityList.size();
            for (int i = size - 1; i >= 0; i--) {
                Activity temp = activityList.get(i);
                if (temp != activity) {
                    temp.finish();
                }
            }
            activityList.clear();

        }
//        SSPreference.getInstance(this).setAppOpenFlag(false);
        System.gc();
    }

    public boolean isStackEmpty(Activity activity) {
        return activityList == null || activityList.size() <= 1;
    }

    public void addActivity(Activity activity) {
        try {
            synchronized (this) {
                if (!activityList.contains(activity)) {
                    activityList.add(activity);
                }
//                SSPreference.getInstance(this).setAppOpenFlag(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeActivity(Activity activity) {
        synchronized (this) {
            if (!activityList.isEmpty() && activityList.contains(activity))
                activityList.remove(activity);
        }
        System.gc();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }



}
