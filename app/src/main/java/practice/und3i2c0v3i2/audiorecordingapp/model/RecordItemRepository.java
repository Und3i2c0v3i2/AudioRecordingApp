package practice.und3i2c0v3i2.audiorecordingapp.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


public class RecordItemRepository {

    private RecordItemDao recordItemDao;
    private LiveData<List<RecordItem>> allRecords;

    public RecordItemRepository(Application application) {
        RecordsDatabase recordsDatabase = RecordsDatabase.getInstance(application);
        recordItemDao = recordsDatabase.recordItemDao();
        allRecords = recordItemDao.getAll();
    }

    public void insert(RecordItem recordItem) {
        new InsertAsync(recordItemDao).execute(recordItem);
    }

    public void update(RecordItem recordItem) {
        new UpdateAsync(recordItemDao).execute(recordItem);
    }

    public void delete(RecordItem recordItem) {
        new DeleteAsync(recordItemDao).execute(recordItem);
    }

    public LiveData<List<RecordItem>> getAll() {
        return allRecords;
    }

    private static class InsertAsync extends AsyncTask<RecordItem, Void, Void> {

        private RecordItemDao recordItemDao;

        private InsertAsync(RecordItemDao recordItemDao) {
            this.recordItemDao = recordItemDao;
        }

        @Override
        protected Void doInBackground(RecordItem... recordItems) {
            if(recordItemDao.checkIfExist(recordItems[0].getGenName()) == 0) {
                recordItemDao.insert(recordItems[0]);
            }
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<RecordItem, Void, Void> {

        private RecordItemDao recordItemDao;

        private UpdateAsync(RecordItemDao recordItemDao) {
            this.recordItemDao = recordItemDao;
        }

        @Override
        protected Void doInBackground(RecordItem... recordItems) {
            recordItemDao.update(recordItems[0]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<RecordItem, Void, Void> {

        private RecordItemDao recordItemDao;

        private DeleteAsync(RecordItemDao recordItemDao) {
            this.recordItemDao = recordItemDao;
        }

        @Override
        protected Void doInBackground(RecordItem... recordItems) {
            recordItemDao.delete(recordItems[0]);
            return null;
        }
    }
}
