package practice.und3i2c0v3i2.audiorecordingapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItemRepository;

public class RecordsViewModel extends AndroidViewModel {


    private RecordItemRepository repository;
    private LiveData<List<RecordItem>> listLiveData;

    public RecordsViewModel(@NonNull Application application) {
        super(application);

        repository = new RecordItemRepository(application);
        listLiveData = repository.getAll();
    }

    public void insert(RecordItem recordItem) {
        repository.insert(recordItem);
    }

    public void update(RecordItem recordItem) {
        repository.update(recordItem);
    }

    public void delete(RecordItem recordItem) {
        repository.delete(recordItem);
    }

    public LiveData<List<RecordItem>> getAll() {
        return listLiveData;
    }

    public RecordItem getItemAt(int position) {
        return listLiveData.getValue().get(position);
    }

}
