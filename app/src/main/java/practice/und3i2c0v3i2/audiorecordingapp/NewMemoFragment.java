package practice.und3i2c0v3i2.audiorecordingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.service.RecordingService;
import practice.und3i2c0v3i2.audiorecordingapp.ui.RecordsViewModel;

import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_OPEN_SAVE_DIALOG;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_KEY;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_PARCELABLE;

public class NewMemoFragment extends Fragment {


    private static final String ARG_IS_RECORDING = "is_recording";
    private static final String ARG_IS_PLAYING = "is_playing";

    public static final String FOLDER_NAME = "/MyRecordings";

    private OnItemClickListener listener;

    @BindView(R.id.fab_rec)
    FloatingActionButton fabRec;
    @BindView(R.id.btn_play_recording)
    MaterialButton btnPlayRecording;
    @BindView(R.id.btn_save_to_db)
    MaterialButton btnSaveRecording;
    @BindView(R.id.tv_recording_info)
    TextView tvRecordingInfo;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.playback_layout)
    LinearLayout playBackLayout;
    @BindView(R.id.playback_seek_bar)
    SeekBar playbackSeekbar;

    private Timer timer;

    int seekbarMax;
    private boolean isRecording;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private File file;
    private RecordItem recordItem;

    private RecordsViewModel recordsViewModel;



    public NewMemoFragment() {}

    public static NewMemoFragment newInstance(boolean isRecording, boolean isPlaying) {
        NewMemoFragment fragment = new NewMemoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_IS_RECORDING, isRecording);
        bundle.putBoolean(ARG_IS_PLAYING, isPlaying);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.record_memo_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

//        if (savedInstanceState != null) {
//            file = savedInstanceState.getParcelable(RECORD_ITEM);
//        }

        tvRecordingInfo.setText("Press to start recording");

        if (file == null) {
            btnPlayRecording.setEnabled(false);
            btnSaveRecording.setEnabled(false);
            playBackLayout.setVisibility(View.INVISIBLE);
        } else {
            btnPlayRecording.setEnabled(true);
            btnSaveRecording.setEnabled(true);
            playBackLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recordsViewModel = ViewModelProviders.of(getActivity())
                .get(RecordsViewModel.class);
    }


    @OnClick(R.id.fab_rec)
    public void startRecording() {

        isRecording = !isRecording;

        Drawable icon;
        if (isRecording) {
            icon = getResources().getDrawable(R.drawable.ic_stop);
            fabRec.setImageDrawable(icon);
            tvRecordingInfo.setText("Recording ...");
        } else {
            icon = getResources().getDrawable(R.drawable.ic_mic);
            fabRec.setImageDrawable(icon);
            btnPlayRecording.setEnabled(true);
            btnSaveRecording.setEnabled(true);
            tvRecordingInfo.setText("Press to start recording");
        }

        onRecord(isRecording);
    }

    private void onRecord(boolean isRecording) {

        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (isRecording) {

//            // TODO ask for write_external_storage permissions
//            File recordingsFolder = new File(Environment.getExternalStorageDirectory() + FOLDER_NAME);
//            if (!recordingsFolder.exists()) {
//                recordingsFolder.mkdir();
//                //TODO ask for MIC permissions
//
//            }

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            getActivity().startService(intent);

        } else {
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playBackLayout.setVisibility(View.VISIBLE);
            getActivity().stopService(intent);
        }


    }


    //TODO run in foreground service



    @OnClick(R.id.btn_play_recording)
    public void playRecording() {

        isPlaying = !isPlaying;

//        if(file == null) {
//            file = listener.getFile();
//        }
        file = listener.getFile();

        Drawable icon;
        if (isPlaying) {

            if(file != null) {
                mediaPlayer = MediaPlayer.create(getActivity(), Uri.fromFile(file));
            } else {
                return;
            }
//            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.air_horn);
            mediaPlayer.start();
            icon = getResources().getDrawable(R.drawable.ic_stop);
            btnPlayRecording.setIcon(icon);
            btnPlayRecording.setText("Stop");

            seekbarMax = mediaPlayer.getDuration();
            playbackSeekbar.setMax(seekbarMax);

            timer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    playbackSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                }
            };
            timer.scheduleAtFixedRate(tt,0, 100);

            playbackSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
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



        } else {
            mediaPlayer.stop();
            icon = getResources().getDrawable(R.drawable.ic_play);
            btnPlayRecording.setIcon(icon);
            btnPlayRecording.setText("Play");
            timer.cancel();
            playbackSeekbar.setProgress(0);

        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = mp.isPlaying();
                if(!isPlaying) {
                    Drawable icon = getResources().getDrawable(R.drawable.ic_play);
                    btnPlayRecording.setIcon(icon);
                    btnPlayRecording.setText("Play");
                    playbackSeekbar.setProgress(0);
                    timer.cancel();
                }
            }
        });

    }

    private void openSaveDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY, ACTION_OPEN_SAVE_DIALOG);
        bundle.putParcelable(BUNDLE_PARCELABLE, recordItem);
        listener.onItemClick(bundle);
    }


    @OnClick(R.id.btn_save_to_db)
    public void saveRecord() {

        if(isPlaying) {
            playRecording();
        }

        recordItem = listener.getRecord();

        openSaveDialog();

//        // TODO ask for write_external_storage permissions
//        File recordingsFolder = new File(Environment.getExternalStorageDirectory() + FOLDER_NAME);
//        if (!recordingsFolder.exists()) {
//            recordingsFolder.mkdir();
//            //TODO ask for MIC permissions
//
//        }
//
//        File source = new File(recordItem.getPath());
//        file = source;
//        File dest = new File(Environment.getExternalStorageDirectory() + FOLDER_NAME);
//
//        try {
//            copyFile(source, dest);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        recordsViewModel.insert(recordItem);
    }



//    private void copyFile(File src, File dst) throws IOException {
//
//        if (!dst.exists()) {
//            dst.mkdir();
//        }
//
//        file = new File(dst.getPath() + File.separator + recordItem.getName() + ".mp3");
//        FileChannel inChannel = null;
//        FileChannel outChannel = null;
//
//        try {
//            inChannel = new FileInputStream(src).getChannel();
//            outChannel = new FileOutputStream(file).getChannel();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Objects.requireNonNull(inChannel).transferTo(0, inChannel.size(), outChannel);
//            recordItem.setPath(file.getAbsolutePath());
//            openSaveDialog();
//        } finally {
//            if (inChannel != null)
//                try {
//                    inChannel.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            if (outChannel != null)
//                try {
//                    outChannel.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnItemClickListener) {
            listener = (OnItemClickListener) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
//        timer.cancel();
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable(RECORD_ITEM, recordItem);
//    }



}
