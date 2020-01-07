package practice.und3i2c0v3i2.audiorecordingapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PlaybackService extends Service {
    public PlaybackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
