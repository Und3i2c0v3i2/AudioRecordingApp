package practice.und3i2c0v3i2.audiorecordingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;

import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.ACTION_OPEN_PLAYER_DIALOG;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_ITEM_POSITION;
import static practice.und3i2c0v3i2.audiorecordingapp.OnItemClickListener.BUNDLE_KEY;


public class RecordsAdapter extends ListAdapter<RecordItem, RecordsAdapter.RecordsHolder> {


    private OnItemClickListener listener;


    private static final DiffUtil.ItemCallback<RecordItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<RecordItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull RecordItem oldItem, @NonNull RecordItem newItem) {
            return oldItem.getId() == oldItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecordItem oldItem, @NonNull RecordItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    public RecordsAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder, parent, false);

        return new RecordsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsHolder holder, int position) {

        RecordItem item = getItem(position);
        holder.recordName.setText(item.getName());
        holder.recordTimestamp.setText(item.getTimestamp());

    }


    public RecordItem getRecordAt(int position) {
        return getItem(position);
    }

    class RecordsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.record_name)
        TextView recordName;
        @BindView(R.id.record_timestamp)
        TextView recordTimestamp;

        public RecordsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(BUNDLE_KEY, ACTION_OPEN_PLAYER_DIALOG);
                        bundle.putInt(BUNDLE_ITEM_POSITION, position);
                        listener.onItemClick(bundle);
                    }
                }
            });
        }

    }
}
