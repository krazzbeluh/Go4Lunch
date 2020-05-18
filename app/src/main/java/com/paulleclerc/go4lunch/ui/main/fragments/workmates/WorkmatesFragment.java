package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.ui.main.fragments.list.RestaurantListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel viewModel;
    private LinearLayoutManager linearLayoutManager;
    private WorkmatesListAdapter adapter;

    @BindView(R.id.workmates_list)
    RecyclerView recyclerView;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WorkmatesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkmatesFragment getInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WorkmatesListAdapter();
        viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        viewModel.fetchWorkmates();

        viewModel.getWorkmates().observe(getViewLifecycleOwner(), workmates -> {
            adapter.setWorkmates(workmates);
            adapter.notifyDataSetChanged();
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
