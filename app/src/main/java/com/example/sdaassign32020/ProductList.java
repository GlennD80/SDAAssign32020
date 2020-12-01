package com.example.sdaassign32020;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class ProductList extends Fragment {

    private static final String TAG = "RecyclerViewActivity";
    private ArrayList<FlavorAdapter> mFlavor = new ArrayList<>();

    public ProductList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_product_list, container, false);
        // Create an ArrayList of AndroidFlavor objects
        mFlavor.add(new FlavorAdapter("Alien", "S, M, L, XL, XXL", R.drawable.alien));
        mFlavor.add(new FlavorAdapter("Celtic", "S, M, L, XL, XXL", R.drawable.celtic));
        mFlavor.add(new FlavorAdapter("Warhammer", "S, M, L, XL, XXL", R.drawable.warhammer));
        mFlavor.add(new FlavorAdapter("Jean Luc", "S, M, L, XL, XXL", R.drawable.jeanluc));
        mFlavor.add(new FlavorAdapter("Baby Yoda", "S, M, L, XL, XXL", R.drawable.babyyoda));
        mFlavor.add(new FlavorAdapter("Sloth", "S, M, L, XL, XXL", R.drawable.sloth));
        mFlavor.add(new FlavorAdapter("SimCity", "S, M, L, XL, XXL", R.drawable.simcity));
        mFlavor.add(new FlavorAdapter("Orangutan", "S, M, L, XL, XXL", R.drawable.orang));
        mFlavor.add(new FlavorAdapter("Dog Biscuit", "S, M, L, XL, XXL", R.drawable.dogbiscuit));

        //start it with the view
        Log.d(TAG, "Starting recycler view");
        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView_view);
        final FlavorViewAdapter recyclerViewAdapter = new FlavorViewAdapter(getContext(), mFlavor);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
/*
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFlavor,  ), Toast.LENGTH_LONG).show();
            }
        });*/

    }
}
