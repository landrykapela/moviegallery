package tz.co.neelansoft.cinegallery.library;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class}, version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String TAG = "MovieDatabase";
    private static final String DATABASE_NAME = "diary";
    private static final Object LOCK = new Object();

    private static MovieDatabase sDbInstance;

    public static MovieDatabase getDatabaseInstance(Context context){
        if(sDbInstance == null){
            synchronized (LOCK){
                Log.e(TAG, "Creating database");
                sDbInstance = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .build();

            }
        }
        Log.d(TAG,"Retrieving database instance");
        return sDbInstance;
    }

    public abstract MovieDao movieDao();
}
