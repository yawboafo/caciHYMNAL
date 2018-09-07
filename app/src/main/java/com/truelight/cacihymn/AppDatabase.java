package com.truelight.cacihymn;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.truelight.cacihymn.Models.FavoriteHymn;
import com.truelight.cacihymn.Models.Hymn;
import com.truelight.cacihymn.ModelsDAO.FavoriteHymnDao;
import com.truelight.cacihymn.ModelsDAO.HymnDao;

@Database(entities = { Hymn.class,FavoriteHymn.class},
        version = 2)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase db;

    public static AppDatabase getAppDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "cac-hymn")
                    // don't do this on a real app!
                    //.allowMainThreadQueries()
                    .fallbackToDestructiveMigration()

                    .build();
        }
        return db;
    }

    public static void destroyInstance() {
        db = null;
    }

    public abstract HymnDao getHymnDao();

    public abstract FavoriteHymnDao getFavoriteHymnDao();





}
