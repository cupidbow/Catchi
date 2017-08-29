package com.envisability.catchi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.envisability.catchi.R;
import com.envisability.catchi.adapters.GroupAdapter;
import com.envisability.catchi.models.Group;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Тарас on 05.06.2017.
 */
public class GroupFragment extends Fragment {

    @BindView(R.id.rwGroups)
    RecyclerView rwGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_groups, container, false);
        ButterKnife.bind(this,v);

        //JUST TEMPLATE------
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group("Veni, Vidi, Vici","https://upload.wikimedia.org/wikipedia/commons/f/fc/Jules_cesar.jpg","In vino veritas",System.currentTimeMillis()-124142));
        groups.add(new Group("Leonardo, Rafael, Michelangelo, Donatello","https://upload.wikimedia.org/wikipedia/ru/a/a7/Red_Donatello.jpg","PIZZA PIZZA Pizza pizza PIZZA PIZZA Pizza pizza PIZZA PIZZA Pizza pizza",System.currentTimeMillis()-15412));
        GroupAdapter adapter = new GroupAdapter(getContext(),groups);
        rwGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rwGroups.setAdapter(adapter);

        return v;
    }

    public static Fragment newInstance()
    {
        return new GroupFragment();
    }
}
