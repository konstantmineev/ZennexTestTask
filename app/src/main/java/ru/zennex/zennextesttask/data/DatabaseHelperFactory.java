package ru.zennex.zennextesttask.data;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Kostez on 24.09.2016.
 */

public class DatabaseHelperFactory {

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static void setHelper(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }

}