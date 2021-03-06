/*
 * WorkmatesFragment.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/29/20 3:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.ui.chat.ChatActivity;
import com.paulleclerc.go4lunch.ui.restaurant_detail.RestaurantDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment implements WorkmatesListAdapter.OnClickOnItem {

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

        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WorkmatesListAdapter(this);
        viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        viewModel.fetchWorkmates();

        viewModel.getWorkmates().observe(getViewLifecycleOwner(), workmates -> {
            adapter.setWorkmates(workmates);
            adapter.notifyDataSetChanged();
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(Workmate workmate) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(getString(R.string.workmate_serializable_key), workmate);
        startActivity(intent);
    }

    @Override
    public void displayRestaurant(String id) {
        viewModel.fetchRestaurant(id).observe(this, restaurant -> {
            Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
            intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_EXTRA_SERIALIZABLE, restaurant);
            startActivity(intent);
        });
    }
}
