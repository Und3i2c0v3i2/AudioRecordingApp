package practice.und3i2c0v3i2.audiorecordingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;

import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_PLAYER_DIALOG_DISMISS;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_KEY;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.IS_PLAYER_DIALOG_SHOWING;


public class MediaPlayerDialog extends AppCompatDialogFragment {


    private OnItemClickListener listener;

    @BindView(R.id.btn_start_playback)
    MaterialButton btnPlay;
    @BindView(R.id.btn_stop_playback)
    MaterialButton btnStop;
    @BindView(R.id.playback_memo_name)
    TextView playbackMemoName;
    @BindView(R.id.playback_seek_bar)
    SeekBar playbackSeekbar;
    @BindView(R.id.playback_chronometer)
    Chronometer chronometer;

    private MediaPlayer mediaPlayer;
    private RecordItem recordItem;
    private File file;
    private Timer timer;

    private long progressPosition;
    private boolean isTimerPaused;
    private boolean isPlaybackStopped;
    private boolean isPlaying;
    private long timeDiff;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.media_player_layout, null);

        ButterKnife.bind(this, view);


        file = new File(recordItem.getPath());
        if(file != null) {
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.fromFile(file));
        } else {
            mediaPlayer = new MediaPlayer();
        }

        setupViews();

        AlertDialog dialog = getDialog(view).create();

        return dialog;
    }

    private void setupViews() {
        btnStop.setEnabled(false);
        playbackMemoName.setText(recordItem.getName());

        playbackSeekbar.setMax((int) recordItem.getLength());

        playbackSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });


    }


    private AlertDialog.Builder getDialog(View view) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        dialog.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isTimerPaused = true;
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }

                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_KEY, ACTION_PLAYER_DIALOG_DISMISS);
                        bundle.putBoolean(IS_PLAYER_DIALOG_SHOWING, false);
                        listener.onItemClick(bundle);

                        dismiss();
                    }
                });


        return dialog;
    }


    @OnClick(R.id.btn_start_playback)
    public void play() {
        Drawable icon;
        if(!isPlaying) {
            icon = getResources().getDrawable(R.drawable.ic_pause);
            btnPlay.setIcon(icon);
            startPlayback();
        } else {
            icon = getResources().getDrawable(R.drawable.ic_play);
            btnPlay.setIcon(icon);
            pausePlayback();
        }
    }


    public void startPlayback() {

        btnStop.setEnabled(true);

        mediaPlayer = MediaPlayer.create(getActivity(), Uri.fromFile(file));

        if(mediaPlayer == null) {
            Toast.makeText(getActivity(), "File cannot be found", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        mediaPlayer.seekTo((int) progressPosition);
        mediaPlayer.start();

        chronometer.setBase(timeDiff + SystemClock.elapsedRealtime());
        timeDiff = 0;
        progressPosition = 0;
        chronometer.start();
        isTimerPaused = false;
        isPlaybackStopped = false;
        isPlaying = true;

        if (timer == null) {
            timer = new Timer();
        }
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                while (!isTimerPaused) {
                    playbackSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                }
                if (isPlaybackStopped) {
                    playbackSeekbar.setProgress(0);
                }
            }
        };
        timer.scheduleAtFixedRate(tt, 0, 100);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayback();
            }
        });

    }


    public void pausePlayback() {
        progressPosition = mediaPlayer.getCurrentPosition();
        isTimerPaused = true;
        isPlaying = false;
        btnStop.setEnabled(true);
        mediaPlayer.pause();
        timeDiff = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }

    @OnClick(R.id.btn_stop_playback)
    public void stopPlayback() {

        Drawable icon = getResources().getDrawable(R.drawable.ic_play);
        btnPlay.setIcon(icon);
        btnStop.setEnabled(false);

        isTimerPaused = true;
        isPlaybackStopped = true;
        isPlaying = false;

        mediaPlayer.pause();
        playbackSeekbar.setProgress(0);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timeDiff = 0;
        progressPosition = 0;
        chronometer.setBase(timeDiff + SystemClock.elapsedRealtime());
        chronometer.stop();

    }

    public void getData(RecordItem recordItem) {
        this.recordItem = recordItem;
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null) {
            isTimerPaused = true;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        dismiss();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnItemClickListener) {
            listener = (OnItemClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
