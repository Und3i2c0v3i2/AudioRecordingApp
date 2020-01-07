package practice.und3i2c0v3i2.audiorecordingapp;

import android.os.Bundle;

import java.io.File;

import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;

public interface OnItemClickListener {

    public static final String BUNDLE_KEY = "bundle key";
    public static final String BUNDLE_PARCELABLE = "bundle parcelable";
    public static final String BUNDLE_ITEM_POSITION = "bundle item position";
    public static final String IS_SAVE_DIALOG_SHOWING = "isSaveDialogShowing";
    public static final String IS_PLAYER_DIALOG_SHOWING = "isPlayerDialogShowing";

    public static final String USER_INPUT_RECORD_NAME = "user input record name";


    public static final int ACTION_OPEN_PLAYER_DIALOG = 1001;
    public static final int ACTION_OPEN_SAVE_DIALOG = 1002;
    public static final int ACTION_SAVE = 1003;
    public static final int ACTION_SAVE_DIALOG_DISMISS = 1004;
    public static final int ACTION_PLAYER_DIALOG_DISMISS = 1005;
    public static final int ACTION_RESTORE_USER_INPUT = 1006;


    void onItemClick(Bundle bundle);
    File getFile();
    RecordItem getRecord();
}
