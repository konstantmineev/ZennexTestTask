package ru.zennex.zennextesttask.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.zennex.zennextesttask.data.DatabaseHelperFactory;
import ru.zennex.zennextesttask.helpers.LocaleHelper;

/**
 * Created by Kostez on 23.09.2016.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private static SharedPreferences preferences;
    private static final String PREFERENCES = "preferences";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = getSharedPreferences(getPackageName() + PREFERENCES, MODE_PRIVATE);
        LocaleHelper.onCreate(this);
        DatabaseHelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        DatabaseHelperFactory.releaseHelper();
        super.onTerminate();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static boolean hasConnection(final Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getActiveNetworkInfo();

        if (wifiInfo != null && wifiInfo.getType() == ConnectivityManager.TYPE_WIFI && wifiInfo.isConnected()) {
            return true;
        }

        wifiInfo = cm.getActiveNetworkInfo();

        if (wifiInfo != null && wifiInfo.getType() == ConnectivityManager.TYPE_MOBILE && wifiInfo.isConnected()) {
            return true;
        }

        wifiInfo = cm.getActiveNetworkInfo();

        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
