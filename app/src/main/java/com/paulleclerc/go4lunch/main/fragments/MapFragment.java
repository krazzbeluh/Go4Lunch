package com.paulleclerc.go4lunch.main.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paulleclerc.go4lunch.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private static MapFragment INSTANCE;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment getInstance() {
        if (INSTANCE == null) INSTANCE = new MapFragment();
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
}
