package practice.und3i2c0v3i2.audiorecordingapp.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordItemDao {

    @Insert
    void insert(RecordItem recordItem);
    @Update
    void update(RecordItem recordItem);
    @Delete
    void delete(RecordItem recordItem);

    @Query("SELECT * FROM records_table ORDER BY timestamp DESC")
    LiveData<List<RecordItem>> getAll();

    @Query("SELECT COUNT() FROM records_table WHERE genName LIKE :genName")
    int checkIfExist(String genName);
}
