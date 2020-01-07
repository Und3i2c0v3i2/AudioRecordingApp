package practice.und3i2c0v3i2.audiorecordingapp;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.ui.RecordsViewModel;

public class SaveAsync extends AsyncTask<RecordItem, Void, Void> {

    private RecordItem recordItem;
    private WeakReference<RecordsViewModel> recordsViewModel;

    public SaveAsync(RecordItem recordItem, RecordsViewModel recordsViewModel) {
        this.recordItem = recordItem;
        this.recordsViewModel = new WeakReference<>(recordsViewModel);
    }

    @Override
    protected Void doInBackground(RecordItem... recordItems) {
//        copyFile();
        recordsViewModel.get().insert(recordItem);
        return null;
    }

}
