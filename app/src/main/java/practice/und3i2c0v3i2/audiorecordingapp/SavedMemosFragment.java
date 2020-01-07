package practice.und3i2c0v3i2.audiorecordingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;
import practice.und3i2c0v3i2.audiorecordingapp.ui.RecordsViewModel;

public class SavedMemosFragment extends Fragment {


    private OnItemClickListener listener;


    @BindView(R.id.recordings_recycler)
    RecyclerView recordingsList;

    private RecordsAdapter recordsAdapter;

    private RecordsViewModel recordsViewModel;
    private LiveData<List<RecordItem>> list;
    private RecordItem recordItem;


    public SavedMemosFragment() {}


    public static SavedMemosFragment newInstance() {

        Bundle args = new Bundle();
        SavedMemosFragment fragment = new SavedMemosFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_memos_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recordingsList = view.findViewById(R.id.recordings_recycler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAdapter();

        recordsViewModel = ViewModelProviders.of(getActivity())
                .get(RecordsViewModel.class);

        recordsViewModel.getAll()
                .observe(getViewLifecycleOwner(), new Observer<List<RecordItem>>() {
                    @Override
                    public void onChanged(List<RecordItem> recordItems) {
                        recordsAdapter.submitList(recordItems);
                    }
                });

//        //TODO fix memory leak with anonymous class
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                recordsViewModel.delete(recordsAdapter.getRecordAt(viewHolder.getAdapterPosition()));
//            }
//        }).attachToRecyclerView(recordingsList);
    }

    private void setupAdapter() {
        recordsAdapter = new RecordsAdapter(listener);
        recordingsList.hasFixedSize();
        recordingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordingsList.setAdapter(recordsAdapter);
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
        recordingsList.setAdapter(null);
    }

}
