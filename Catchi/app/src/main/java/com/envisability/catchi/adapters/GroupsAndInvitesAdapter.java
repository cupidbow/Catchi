package com.envisability.catchi.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.envisability.catchi.R;
import com.envisability.catchi.fragments.GroupFragment;
import com.envisability.catchi.fragments.GroupInviteFragment;

/**
 * Created by Тарас on 05.06.2017.
 */
public class GroupsAndInvitesAdapter extends FragmentPagerAdapter {
    public Context context;
    public GroupsAndInvitesAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return GroupFragment.newInstance();
            case 1:
                return GroupInviteFragment.newInstance();

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.groups);
            case 1:
                return context.getString(R.string.invites);

        }
        return "";
    }
}
