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
import com.envisability.catchi.adapters.GroupInviteAdapter;
import com.envisability.catchi.models.Group;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Тарас on 05.06.2017.
 */
public class GroupInviteFragment extends Fragment {

    @BindView(R.id.rwGroupInvites)
    RecyclerView rwGroupInvite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_group_invites, container, false);
        ButterKnife.bind(this, v);
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group("Traxes, Warlord, Warlock, WraithKing, Roshan, Courier","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQu-Bf7zINYhOhVMH5onoU7ed-K5bi04WOEeHFAXe9miCt5Wwidw","gg gg gg gg gg gg gg gg gg",System.currentTimeMillis()-124142));
        groups.add(new Group("Leonardo, Rafael, Michelangelo, Donatello","https://upload.wikimedia.org/wikipedia/ru/a/a7/Red_Donatello.jpg","PIZZA PIZZA Pizza pizza PIZZA PIZZA Pizza pizza PIZZA PIZZA Pizza pizza",System.currentTimeMillis()-15412));
        groups.add(new Group("Veni, Vidi, Vici","https://upload.wikimedia.org/wikipedia/commons/f/fc/Jules_cesar.jpg","In vino veritas",System.currentTimeMillis()-12414255));
        GroupInviteAdapter adapter = new GroupInviteAdapter(getContext(),groups);
        rwGroupInvite.setLayoutManager(new LinearLayoutManager(getContext()));
        rwGroupInvite.setAdapter(adapter);

        return v;
    }

    public static Fragment newInstance()
    {
        return new GroupInviteFragment();
    }
}
