package practice.und3i2c0v3i2.audiorecordingapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import practice.und3i2c0v3i2.audiorecordingapp.R;

public class PermissionsUtil {

    /* re-used for displaying the message in showRationale() */
    public static final String WRITE_EXTERNAL_STORAGE = "Write external storage";
    public static final String READ_EXTERNAL_STORAGE = "Read external storage";
    public static final String RECORD_AUDIO = "Record audio";

//    <uses-permission android:name="android.permission.RECORD_AUDIO" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;




    public PermissionsUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.permission_preferences), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    /*
    check if permission has been requested before
     */
    public boolean checkPermissionPreferences(String permission) {

        boolean isShown = false;

        switch (permission) {
            case WRITE_EXTERNAL_STORAGE:
//                isShown = sharedPreferences.getBoolean(context.getString(R.string.permission_camera), false);
                break;
            case READ_EXTERNAL_STORAGE:
//                isShown = sharedPreferences.getBoolean(context.getString(R.string.permission_tag_photo), false);
                break;
        }

        return isShown;
    }


    /*
    initial value is false
    when requested for the first time, flag in shared preferences will be changed to 'true'
    so we can decide if we should show Rationale next time we ask for this permission
     */
//    public void updatePermissionPreferences(String permission) {
//
//        switch (permission) {
//            case WRITE_EXTERNAL_STORAGE:
//                editor.putBoolean(context.getString(R.string.permission_camera), true);
//                editor.commit();
//                break;
//            case READ_EXTERNAL_STORAGE:
//                editor.putBoolean(context.getString(R.string.permission_tag_photo), true);
//                editor.commit();
//                break;
//        }
//    }


}
