package practice.und3i2c0v3i2.audiorecordingapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = RecordItem.class, version = 1)
public abstract class RecordsDatabase extends RoomDatabase {


    public static final String RECORDS_DATABASE = "records_database";
    private static RecordsDatabase instance;

    public abstract RecordItemDao recordItemDao();

    public static synchronized RecordsDatabase getInstance(Context context) {

        if(instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(),
                            RecordsDatabase.class, RECORDS_DATABASE)
                    .fallbackToDestructiveMigration()
            .build();
        }

        return instance;
    }

}
