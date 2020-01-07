package practice.und3i2c0v3i2.audiorecordingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.ui.RecordsViewModel;
import practice.und3i2c0v3i2.audiorecordingapp.ui.main.SectionsPagerAdapter;

import static practice.und3i2c0v3i2.audiorecordingapp.service.RecordingService.RECORD_ITEM;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String BC_ACTION = "practice.und3i2c0v3i2.audiorecordingapp";
    public static final String MEDIA_PLAYER_SHOWING = "isMediaPlayerDialogShowing";
    public static final String SAVE_RECORD_SHOWING = "isSaveRecordDialogShowing";

    private RecordsViewModel recordsViewModel;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;


    private MyBroadcast broadcast;
    private RecordItem recordItem;
    private String name;
    private File file;

    private SaveRecordDialog saveRecordDialog;
    private MediaPlayerDialog mediaPlayerDialog;

    private boolean isMediaPlayerDialogShowing;
    private boolean isSaveRecordDialogShowing;

    private SaveAsync saveAsync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recordsViewModel =
                ViewModelProviders.of(this)
                        .get(RecordsViewModel.class);


        if (savedInstanceState != null) {
            recordItem = savedInstanceState.getParcelable(RECORD_ITEM);
            isMediaPlayerDialogShowing = savedInstanceState.getBoolean(MEDIA_PLAYER_SHOWING);
            isSaveRecordDialogShowing = savedInstanceState.getBoolean(SAVE_RECORD_SHOWING);
            if(recordItem != null) {
                name = savedInstanceState.getString(USER_INPUT_RECORD_NAME, recordItem.getName());
            }
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        if(isSaveRecordDialogShowing) {
            showSaveRecordDialog();
        }

        if(isMediaPlayerDialogShowing) {
            showMediaPlayerDialog(recordItem);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(BC_ACTION);
        broadcast = new MyBroadcast();
        registerReceiver(broadcast, intentFilter);

        if(isMediaPlayerDialogShowing) {
//            showMediaPlayerDialog(recordItem);
        }
        if(isSaveRecordDialogShowing) {
            showSaveRecordDialog();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcast);
        broadcast = null;

        if(mediaPlayerDialog != null && isMediaPlayerDialogShowing) {
            mediaPlayerDialog.dismiss();
        }
        if(saveRecordDialog != null && isSaveRecordDialogShowing) {
            saveRecordDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(Bundle bundle) {

        int action = bundle.getInt(BUNDLE_KEY);

        switch (action) {

            case ACTION_OPEN_PLAYER_DIALOG:
                int position = bundle.getInt(BUNDLE_ITEM_POSITION);
                recordItem = recordsViewModel.getItemAt(position);
                showMediaPlayerDialog(recordItem);
                break;

            case ACTION_OPEN_SAVE_DIALOG:
                showSaveRecordDialog();
                break;

            case ACTION_SAVE:
                recordItem = bundle.getParcelable(BUNDLE_PARCELABLE);
                isSaveRecordDialogShowing = bundle.getBoolean(IS_SAVE_DIALOG_SHOWING, false);
                saveRecord(recordItem);
                break;

            case ACTION_SAVE_DIALOG_DISMISS:
                isSaveRecordDialogShowing = bundle.getBoolean(IS_SAVE_DIALOG_SHOWING, false);
                break;

            case ACTION_PLAYER_DIALOG_DISMISS:
                isMediaPlayerDialogShowing = bundle.getBoolean(IS_PLAYER_DIALOG_SHOWING, false);
                break;

            case ACTION_RESTORE_USER_INPUT:
                name = bundle.getString(USER_INPUT_RECORD_NAME);
                break;
        }
    }

    private void showSaveRecordDialog() {
        isSaveRecordDialogShowing = true;
        saveRecordDialog = new SaveRecordDialog();
        saveRecordDialog.getData(recordItem);
        saveRecordDialog.getName(name);
        saveRecordDialog.setCancelable(false);
        saveRecordDialog.show(getSupportFragmentManager(), "save record dialog");
    }

    private void showMediaPlayerDialog(RecordItem recordItem) {
        isMediaPlayerDialogShowing = true;
        mediaPlayerDialog = new MediaPlayerDialog();
        mediaPlayerDialog.getData(recordItem);
        mediaPlayerDialog.setCancelable(false);
        mediaPlayerDialog.show(getSupportFragmentManager(), "media player dialog");
    }

    private void saveRecord(RecordItem recordItem) {
        name = "";
        saveAsync = new SaveAsync(recordItem, recordsViewModel);
        saveAsync.execute();
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public RecordItem getRecord() {
        return recordItem;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayerDialog != null && isMediaPlayerDialogShowing) {
            mediaPlayerDialog.dismiss();
        }
        if(saveRecordDialog != null && isSaveRecordDialogShowing) {
            saveRecordDialog.dismiss();
        }

        if(saveAsync != null) {
            saveAsync.cancel(true);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECORD_ITEM, recordItem);
        outState.putBoolean(MEDIA_PLAYER_SHOWING, isMediaPlayerDialogShowing);
        outState.putBoolean(SAVE_RECORD_SHOWING, isSaveRecordDialogShowing);
        outState.putString(USER_INPUT_RECORD_NAME, name);
    }


    public class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            recordItem = intent.getParcelableExtra(RECORD_ITEM);
            file = new File(Objects.requireNonNull(recordItem).getPath());
        }
    }

//    private static class SaveAsync extends AsyncTask<RecordItem, Void, Void> {
//
//        private RecordItem recordItem;
//        private WeakReference<RecordsViewModel> recordsViewModel;
//
//        public SaveAsync(RecordItem recordItem, RecordsViewModel recordsViewModel) {
//            this.recordItem = recordItem;
//            this.recordsViewModel = new WeakReference<>(recordsViewModel);
//        }
//
//        @Override
//        protected Void doInBackground(RecordItem... recordItems) {
//            recordsViewModel.get().insert(recordItem);
//            return null;
//        }
//
//    }


}
