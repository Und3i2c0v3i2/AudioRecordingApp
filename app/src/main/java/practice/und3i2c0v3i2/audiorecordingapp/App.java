package practice.und3i2c0v3i2.audiorecordingapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "CH_1";
    public static final String CHANNEL_NAME = "CH_RECORD_MIC";
    public static final String CHANNEL_DESC = "Notification Channel Description";


    @Override
    public void onCreate() {
        super.onCreate();

        registerNotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESC);

    }


    public void registerNotificationChannel(String channelId, String channelName, String channelDescription) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);

            NotificationManager manager = this.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


}
