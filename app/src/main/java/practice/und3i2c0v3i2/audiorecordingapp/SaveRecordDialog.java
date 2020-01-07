package practice.und3i2c0v3i2.audiorecordingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;

import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_RESTORE_USER_INPUT;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_SAVE;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_SAVE_DIALOG_DISMISS;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_KEY;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_PARCELABLE;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.IS_SAVE_DIALOG_SHOWING;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.USER_INPUT_RECORD_NAME;

public class SaveRecordDialog extends AppCompatDialogFragment {


    @BindView(R.id.save_memo_location)
    TextView memoLocation;
    @BindView(R.id.save_memo_timestamp)
    TextView memoTimestamp;
    @BindView(R.id.save_memo_length)
    TextView memoLength;
    @BindView(R.id.save_memo_name)
    TextInputEditText memoName;

    private RecordItem recordItem;
    private String name;
    private OnItemClickListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.save_record_layout, null);

        ButterKnife.bind(this, view);

        if (name == null) {
            name = recordItem.getName();
        }

        setupViews();

        return getDialog(view).create();
    }

    private void setupViews() {
        long s = recordItem.getLength() / 1000;
        memoLocation.setText(recordItem.getPath());
        memoLength.setText(s + "s");
        memoTimestamp.setText(recordItem.getTimestamp());

        memoName.setText(name);

        memoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private AlertDialog.Builder getDialog(View view) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        dialog.setView(view)
                .setTitle("Save new memo")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            String newName = memoName.getText().toString();
                            recordItem.setName(newName);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BUNDLE_KEY, ACTION_SAVE);
                            bundle.putParcelable(BUNDLE_PARCELABLE, recordItem);
                            bundle.putBoolean(IS_SAVE_DIALOG_SHOWING, false);
                            listener.onItemClick(bundle);
                            name = null;
                            dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_KEY, ACTION_SAVE_DIALOG_DISMISS);
                        bundle.putBoolean(IS_SAVE_DIALOG_SHOWING, false);
                        listener.onItemClick(bundle);
                        name = null;
                        dismiss();
                    }
                });


        return dialog;
    }


    public void getData(RecordItem recordItem) {
        this.recordItem = recordItem;
    }

    public void getName(String name) {
        this.name = name;
    }


    @Override
    public void onPause() {

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY, ACTION_RESTORE_USER_INPUT);
        bundle.putString(USER_INPUT_RECORD_NAME, name);
        listener.onItemClick(bundle);
        dismiss();
        super.onPause();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener) {
            listener = (OnItemClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        memoName.addTextChangedListener(null);
        super.onDetach();
    }

}
