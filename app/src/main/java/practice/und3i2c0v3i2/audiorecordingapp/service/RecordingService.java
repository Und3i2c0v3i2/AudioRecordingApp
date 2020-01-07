package practice.und3i2c0v3i2.audiorecordingapp.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import practice.und3i2c0v3i2.audiorecordingapp.MainActivity;
import practice.und3i2c0v3i2.audiorecordingapp.R;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.util.UtilDateConverter;

import static practice.und3i2c0v3i2.audiorecordingapp.App.CHANNEL_ID;

public class RecordingService extends Service {

    public static final String RECORD_ITEM = "record item";
    private String tempFilePath;

    private MediaRecorder mediaRecorder;
    private long startingMillis;
    private long elapsedMillis;

    private File file;
    private String fileName;



    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = getNotification();
        startForeground(1, notification);
        mediaRecorder = new MediaRecorder();
        tempFilePath = getCacheDir() + File.separator + getPackageName() + "_temp.mp3";
    }

    boolean isRecording;

    private Notification getNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Recording in progress")
                .setSubText("Voice Memo Recorder")
                .setUsesChronometer(true)
                .setProgress(100, 0, true)
                .setSmallIcon(R.drawable.voice_memo_w_shadow)
                .setContentIntent(pendingIntent)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                startRecording();
            }
        };

        if(!isRecording) {
            Thread t = new Thread(r);
            t.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        if (mediaRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startRecording() {

        isRecording = true;

        Long ts = System.currentTimeMillis() / 1000;
        String timestamp = ts.toString();
        fileName = "Audio_" + timestamp;

        file = new File(tempFilePath);
//        file = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator + fileName + "_temp.mp3");

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;

            startingMillis = System.currentTimeMillis();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecording() {

        if(isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();

            isRecording = false;
        }

        elapsedMillis = System.currentTimeMillis() - startingMillis;

        Date created = UtilDateConverter.convertToDateTime(System.currentTimeMillis());
        RecordItem recordItem = new RecordItem(fileName, fileName, elapsedMillis, tempFilePath, UtilDateConverter.convertDateTimeToString(created), false);

        Intent intent = new Intent();
        intent.putExtra(RECORD_ITEM, recordItem);
        intent.setAction(MainActivity.BC_ACTION);
        sendBroadcast(intent);

    }

}
