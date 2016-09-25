package ru.zennex.zennextesttask.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Kostez on 24.09.2016.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private Dao<ItemRecyclerViewTable, Integer> listRecyclerViewTableDao;

    public DatabaseHelper(Context context) {
        super(context, "mydatabasedb.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ItemRecyclerViewTable.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ItemRecyclerViewTable.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        }
    }

    public Dao<ItemRecyclerViewTable, Integer> getListRecyclerViewTableDao() throws SQLException {
        if (listRecyclerViewTableDao == null) {
            listRecyclerViewTableDao = getDao(ItemRecyclerViewTable.class);
        }
        return listRecyclerViewTableDao;
    }
}